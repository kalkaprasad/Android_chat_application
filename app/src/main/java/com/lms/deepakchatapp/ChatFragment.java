package com.lms.deepakchatapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> mUser;
    EditText search_user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;




    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser=new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
//        readUsers();
        search_user=view.findViewById(R.id.search_user);
        if(firebaseUser!=null)
        {
            getchatlist(firebaseUser);
        }
        else
        {
            Toast.makeText(getContext(), "firebaseuser not found", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void getchatlist(FirebaseUser firbaecurrentuser) {
        databaseReference.child("Chatlist").child(firbaecurrentuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                for(DataSnapshot datasnap:dataSnapshot.getChildren())
                {
                    String userid= Objects.requireNonNull(datasnap.child("id").getValue().toString());
                    Log.e("snap","dnapvalue"+userid);

                    getUserdata(userid); // here this id come from the chat list here sender of the message....

                }

                }
                else {

                    Toast.makeText(getContext(), "database not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public  void getUserdata(final String Userid)
    {

        Toast.makeText(getContext(), ""+Userid, Toast.LENGTH_SHORT).show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").orderByChild("id").equalTo(Userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                if(dataSnapshot.exists())
                {mUser.clear();
                    for(DataSnapshot userdatasnap:dataSnapshot.getChildren())
                    {
                        User user = userdatasnap.getValue(User.class);
                            mUser.add(user);

                    }
                    userAdapter = new UserAdapter(getContext(), mUser, false);
                    recyclerView.setAdapter(userAdapter);

                }
                else{

                    Toast.makeText(getContext(), "Data not in Userdata", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
