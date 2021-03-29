package com.lms.deepakchatapp;

import android.Manifest;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lms.deepakchatapp.main.ContactAdapter;
import com.lms.deepakchatapp.main.ContactModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.content.ContentValues.TAG;


public class Contact extends Fragment {
    ArrayList<String> Contactlist;
    ArrayList<String>FirebaseContact;
    TextView cntdetials;
    FirebaseUser fuser;
    DatabaseReference reference;
    FirebaseDatabase database;
    HashSet<String> set;
    int sizes;
    RecyclerView contectRc;
    List<ContactModel> contactModels;
    ContactAdapter contAd;
    public Contact() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
      database = FirebaseDatabase.getInstance();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_contact, container, false);
        Contactlist= new ArrayList<>(); // this is the Arraylist
        contectRc=view.findViewById(R.id.Contact_recy);
        contactModels = new ArrayList<>();
        set= new HashSet<>();
        FirebaseContact = new ArrayList<>();

// Adapter initlization

//        end..
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        firebasecontact();
                        getContact();

                        // Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                        Toast.makeText(getContext(), "PermissionDenied", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {token.continuePermissionRequest();}
                }).check();

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public  void  getContact()
    {

        Cursor cursor=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contactlist.add(phone);
            //Toast.makeText(getContext(), "Total length of the contact is"+Contactlist.lastIndexOf(phone), Toast.LENGTH_SHORT).show();
         //   cntdetials.setText(Contactlist.toString());


        }
        Log.d(TAG, "localtotalsize: "+Contactlist.size());
        Log.d(TAG,"Firebase Contact"+FirebaseContact.size());



    }

    public  void  firebasecontact()
    {

        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                contactModels.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    FirebaseContact.add(user.getContactnumber());
//                  cntdetials.setText(FirebaseContact.toString());

                }
                Log.d(TAG, "FirebaseContact size: "+FirebaseContact.size());


                for (int i = 0; i <Contactlist.size(); i++)
                {
                    for (int j = 0; j<FirebaseContact.size(); j++)
                    {
                        if(Contactlist.get(i).equals(FirebaseContact.get(j)))
                        {

                        // Main Logic Here...
                            set.add(Contactlist.get(i));
                        }
                    }

                }
                Log.d(TAG,"Matched Contact"+set.toString());
//                cntdetials.setText(set.toString());

                // Here Main Logic for Fetch data from Firebase database....

                /*
                 * Convert HashSet to an array
                 */
                final String[] array = set.toArray( new String[set.size()] );
                for(int p=0;p<array.length;p++)
                {

                    Log.d(TAG,"Loop set Data"+array[p]);

                    // here we get data according  to phone number..

                    final int finalP = p;
                    database.getReference("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot data:dataSnapshot.getChildren())
                            {

                                User user = data.getValue(User.class);
                                if(array[finalP].toString().equalsIgnoreCase(user.getContactnumber()))
                                {


                                    String ImageUrl= user.getImageURL();
                                    String names=user.getUsername();
                                    String Phonenum=user.getContactnumber();
                                    String Statusvalue= user.getStatus();
                                    //contactModels.clear();
                                    ContactModel cnmd= new ContactModel(names,ImageUrl,Statusvalue);
                                    contactModels.add(cnmd);
                                    Log.d(TAG,"USername"+names);
                                    Log.d(TAG,"Contactnum"+Phonenum);
                                    Log.d(TAG,"ImageUrl"+ImageUrl);
                                    Log.d(TAG,"StatusValue"+Statusvalue);
                                  //  contactModels.clear();

                                }

                            }

                          contAd=new ContactAdapter(getActivity(),contactModels);
        contectRc.setLayoutManager(new LinearLayoutManager(getContext()));
        contectRc.setAdapter(contAd);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG,"Something Error");
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}