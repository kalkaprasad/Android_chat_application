package com.lms.deepakchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.lms.deepakchatapp.RegisterActivity.mypreference;

public class SettingsActivity extends AppCompatActivity {
    private Button saveBtn;
    private EditText userNameET,userBioET;
    private ImageView profileImageView;
    private static int GalleryPick=1;
    private Uri ImageUri;
    private StorageReference userProfileImageReference;
    private String downloadUri;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        userProfileImageReference= FirebaseStorage.getInstance().getReference().child("Profile Images");
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        rq= Volley.newRequestQueue(getApplicationContext());
        saveBtn=findViewById(R.id.save_settings_btn);
        userNameET=findViewById(R.id.username_settings);
        userBioET=findViewById(R.id.bio_settings);
        profileImageView=findViewById(R.id.settings_profile_image);
        firebaseToken();
        progressDialog=new ProgressDialog(this);
        final String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, "token"+token, Toast.LENGTH_SHORT).show();
        Log.e("token",token);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        retrieveUserInfo();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick&&resultCode==RESULT_OK&&data!=null){
            ImageUri=data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }

    private void saveUserData() {
        final String getUserName=userNameET.getText().toString();
        final String getUserStatus=userBioET.getText().toString();
        if (ImageUri==null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                        saveInfoOnlyWithoutImage();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "user image not selected", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (getUserName.equals("")) {
            Toast.makeText(this, "username is mondetory", Toast.LENGTH_SHORT).show();
        } else if (getUserStatus.equals("")) {
            Toast.makeText(this, "bio is mondetory", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Account Setting");
            progressDialog.setMessage("Please Wait ...");
            progressDialog.show();
            final StorageReference filePath=userProfileImageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask=filePath.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    downloadUri=filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                     SharedPreferences sharedpreferences = getSharedPreferences("UserPhone",
                                Context.MODE_PRIVATE);
                        final String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final String userphone=sharedpreferences.getString("contactnum","null"); /// userphone number
                        downloadUri=task.getResult().toString();
                        HashMap<String,Object> profileMap=new HashMap<>();
                        profileMap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("username",getUserName);
                        profileMap.put("status",getUserStatus);
                        profileMap.put("imageURL",downloadUri);
                        profileMap.put("search",getUserName);
                       profileMap.put("Contactnumber","8127826696");
//                        profileMap.put("Contact",sharedpreferences.getString("contactnum","8124826696"));

                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    SenduserdatatoFirebase(userid,getUserName,getUserStatus,downloadUri,getUserName,userphone);

                                    startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                                    finish();
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingsActivity.this, "profile setting updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }



    }


    private  void firebaseToken()
    {

        final String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, "token"+token, Toast.LENGTH_SHORT).show();

        String url="http://kalkaprasad.com/songsapi/tokengen.php";
        StringRequest sr=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Log.e("response:--->", response );
                if (response.equals("success"))
                {
                    Log.e("response:--->", "success" );
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getApplicationContext(), "Network Error"+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("token",token);
                hashMap.put("phone","6239911854");

                return hashMap;
            }
        };
        rq.add(sr);
    }


    private void saveInfoOnlyWithoutImage() {
        final String getUserName=userNameET.getText().toString();
        final String getUserStatus=userBioET.getText().toString();


        if (getUserName.equals("")) {
            Toast.makeText(this, "username is mondetory", Toast.LENGTH_SHORT).show();
        } else if (getUserStatus.equals("")) {
            Toast.makeText(this, "bio is mondetory", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Account Setting");
            progressDialog.setMessage("Please Wait ...");
            progressDialog.show();
            HashMap<String,Object> profileMap=new HashMap<>();
            profileMap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("username",getUserName);
            profileMap.put("status",getUserStatus);
            profileMap.put("search",getUserName);
            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "profile setting updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
    private void retrieveUserInfo(){
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String imageDb=dataSnapshot.child("imageURL").getValue().toString();
                    String nameDb=dataSnapshot.child("username").getValue().toString();
                    String bioDb=dataSnapshot.child("status").getValue().toString();

                    userNameET.setText(nameDb);
                    userBioET.setText(bioDb);
                    Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(profileImageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // here send user to data base into the  firestore database..
    public void SenduserdatatoFirebase(String userid, String username, String status, String imgurl, String search, String Contactnumber)
    {



    }

    {




    }
    }
