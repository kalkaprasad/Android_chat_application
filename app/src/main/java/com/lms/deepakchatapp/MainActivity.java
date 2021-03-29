package com.lms.deepakchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ice.restring.Restring;
import com.lms.deepakchatapp.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Restring.init(getBaseContext());
        firebasemessage();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        RootRef= FirebaseDatabase.getInstance().getReference();
        mToolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chatting");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        String str = getResources().getString(R.string.app_name);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }
        if (item.getItemId()==R.id.settings){
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.create_group){
//            startActivity(new Intent(MainActivity.this,CreateGroupActivity.class));
            Toast.makeText(this, "Create group", Toast.LENGTH_SHORT).show();
          /*  AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
            builder.setTitle("Enter Group Name");
            final EditText groupNameField=new EditText(MainActivity.this);
            groupNameField.setHint("e.g. ssb");
            builder.setView(groupNameField);
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String groupName=groupNameField.getText().toString();
                    if (TextUtils.isEmpty(groupName)){
                        Toast.makeText(MainActivity.this, "Please Write your group name", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        CreateNewGroup(groupName);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            Toast.makeText(this, "create group", Toast.LENGTH_SHORT).show();

           */
        }
        if (item.getItemId()==R.id.notification){
            Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.request_people){
            Toast.makeText(this, "request people", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    private void CreateNewGroup(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, groupName+" group is created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



   public  void firebasemessage()
    {

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {

            NotificationChannel channel= new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }




        FirebaseMessaging.getInstance().subscribeToTopic("paid")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
