package com.lms.deepakchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  com.lms.deepakchatapp.Permissions.*;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    private Permissions permissions;
    RecyclerView recyclerView;

    ImageButton btn_send,btn_send_file;
    EditText text_send;
    String userid;
    RequestQueue rq;
    ValueEventListener seenListener;
    private static int GalleryPick=1;
    private Uri ImageUri;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


rq= Volley.newRequestQueue(getApplicationContext());
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        btn_send_file=findViewById(R.id.btn_send_file);
        text_send=findViewById(R.id.text_send);
        intent=getIntent();
    userid=intent.getStringExtra("userid");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        btn_send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGalleryImage();
                
            }
        });

//        btn_send_file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(MessageActivity.this);
//                final View customDialog=getLayoutInflater().inflate(R.layout.custom_dialog,null);
//                builder.setView(customDialog);
//                AlertDialog alert = builder.create();
//                alert.getWindow().setDimAmount(0f);
//                WindowManager.LayoutParams wmlp=alert.getWindow().getAttributes();
//                wmlp.gravity= Gravity.BOTTOM|Gravity.START;
//                ColorDrawable back=new ColorDrawable(Color.TRANSPARENT);
//                InsetDrawable insetDrawable=new InsetDrawable(back,50);
//                alert.show();
//                ImageView btn_camera=customDialog.findViewById(R.id.btn_camera);
//                ImageView btn_document=customDialog.findViewById(R.id.btn_document);
//                ImageView btn_video=customDialog.findViewById(R.id.btn_video);
//                btn_camera.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent galleryIntent=new Intent();
//                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                        galleryIntent.setType("image/*");
//                        startActivityForResult(galleryIntent,GalleryPick);
//                    }
//                });
//                btn_document.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent documentIntent=new Intent();
//                        documentIntent.setAction(Intent.ACTION_GET_CONTENT);
//                        documentIntent.setType("document/*");
//                        startActivityForResult(documentIntent,GalleryPick);
//                    }
//                });
//                btn_video.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent videoIntent=new Intent();
//                        videoIntent.setAction(Intent.ACTION_GET_CONTENT);
//                        videoIntent.setType("video/*");
//                        startActivityForResult(videoIntent,GalleryPick);
//                    }
//                });
//            }
//        });

        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);

                }else {
                    //ch

                    Picasso.get().load(user.getImageURL()).placeholder(R.drawable.profile_image).into(profile_image);
                }
                readMessages(fuser.getUid(),userid,user.getImageURL());
                readImages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessages(userid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 300) {
            if (data != null) {
                ArrayList<String> selectedImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

                if (fuser == null)
                    Toast.makeText(this, "Send simple message first", Toast.LENGTH_SHORT).show();
                else {

//                    Intent intent = new Intent(MessageActivity.this, SendMediaService.class);
//                    intent.putExtra("hisID", hisID);
//                    intent.putExtra("chatID", chatID);
//                    intent.putStringArrayListExtra("media", selectedImages);
//
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
//                        startForegroundService(intent);
//                    else startService(intent);
                    sendImage(fuser.getUid(),userid,selectedImages.get(0));
                    Toast.makeText(this, "go messsae"+selectedImages, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGalleryImage();
                } else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }


    private void getGalleryImage() {

        Options options = Options.init()
                .setRequestCode(300)                                           //Request code for activity results
                .setCount(5)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/ChatMe/Media");                                       //Custom Path For media Storage


        Pix.start(this, options);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        final String userid=intent.getStringExtra("userid");
//        if (requestCode==GalleryPick&&resultCode==RESULT_OK&&data!=null){
//            ImageUri=data.getData();
//            if(!ImageUri.equals("")) {
//                sendImage(fuser.getUid(), userid, ImageUri);
//            }
//        }
//    }

    private void sendImage(String sender, String receiver, String imageUri) {
        final String userid=intent.getStringExtra("userid");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("image",imageUri);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef= FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readImages(final String myid, final String userid, final String imageurl) {
        mChat=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        mChat.add(chat);
                        try {
                            Badges.setBadge(MessageActivity.this,1);
                        } catch (BadgesNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessages(final String userid){
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid())&&chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender,final String receiver,String message){
        final String userid=intent.getStringExtra("userid");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef= FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);

                    sendchatNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // chat notification
    private void sendchatNotification() {



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
                hashMap.put("title","title");
                hashMap.put("message","message");
                hashMap.put("imgurl","imgurl");

                return hashMap;
            }
        };
        rq.add(sr);





    }

    private void readMessages(final String myid, final String userid, final String imageurl){
        mChat=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        mChat.add(chat);
                        try {
                            Badges.setBadge(MessageActivity.this,1);
                        } catch (BadgesNotSupportedException e) {
                            e.printStackTrace();
                        }

                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void status(String status){
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }

}
