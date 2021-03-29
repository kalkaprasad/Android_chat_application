package com.lms.deepakchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText phoneText;
    private EditText codeText;
    private Button continueNextButton;
    private String checker="",phoneNumber="";
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "UserPhone";
    public static final String Phone = "nameKey";
    StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);
        phoneText=findViewById(R.id.phoneText);
        codeText=findViewById(R.id.codeText);
        continueNextButton=findViewById(R.id.continueNextButton);
        relativeLayout=findViewById(R.id.phoneAuth);
        ccp=findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        continueNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continueNextButton.getText().equals("Submit")||checker.equals("Code Sent")){
                    String verificationCode=codeText.getText().toString();
                    if (verificationCode.equals("")){
                        Toast.makeText(RegisterActivity.this, "please write verification code", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("Please Wait..,while we are verifying your code number");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                        signInWithPhoneAuthCredential(credential);
                    }
                }else {
                    phoneNumber=ccp.getFullNumberWithPlus();
                    if (!phoneNumber.equals("")){
                        loadingBar.setTitle("Phone Number Verification");
                        loadingBar.setMessage("Please Wait..,while we are verifying your phone number");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,RegisterActivity.this,callbacks);
                    }else {
                        Toast.makeText(RegisterActivity.this, "please valid phone number", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                continueNextButton.setText("Continue");
                codeText.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId=s;
                mResendToken=forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker="Code Sent";
                continueNextButton.setText("Submit");
                codeText.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
                Toast.makeText(RegisterActivity.this, "Code has been sent,check your phone", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent homeIntent=new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                  //  firebaseToken();
                    Toast.makeText(RegisterActivity.this, "Congratulations", Toast.LENGTH_SHORT).show();
                    // store her user phone number
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    String phonenumber= "8127826696";
//                    editor.putString("contactnum", phonenumber);
//                    editor.commit();
                    sendUserToMainActivity();
                    //  FirebaseUser user=task.getResult().getUser();
                }else {
                    loadingBar.dismiss();
                    String e=task.getException().toString();
                    Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendUserToMainActivity(){
        Intent i=new Intent(RegisterActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
    }




    }
