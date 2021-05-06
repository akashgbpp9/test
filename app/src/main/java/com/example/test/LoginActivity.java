package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText number,otp;
    Button phnnumberbtn,otpbtn;
    Snackbar snackbar;
    View view;
    private String verificationID;
    FirebaseAuth firebaseAuth;
    final String lexicon = "abcdefghijklmnopqrstuvwxyz12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<>();
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        view = findViewById(android.R.id.content);
        number=findViewById(R.id.phnnumber);
        otp=findViewById(R.id.otp);
        firebaseAuth = FirebaseAuth.getInstance();
        phnnumberbtn=findViewById(R.id.phnnumberbtn);
        otpbtn=findViewById(R.id.otpbtn);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("Username","");
        phnnumberbtn.setOnClickListener(v -> {
            String strnumber = "+91"+number.getText().toString().trim();
            if (strnumber.isEmpty() || strnumber.length()<10){
                snackbar = Snackbar.make(view, "Invalid Number", Snackbar.LENGTH_LONG);
                setSnackbar();
            }else {
                sendverificationcode(strnumber);
            }
        });
        otpbtn.setOnClickListener(v -> {
            String strotp = otp.getText().toString().trim();
            if (strotp.isEmpty() || strotp.length()<6){
                snackbar = Snackbar.make(view, "Invalid Code", Snackbar.LENGTH_LONG);
                setSnackbar();
            }else {
                PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,strotp);
                verifycode(credential);
            }
        });


    }
    private void verifycode(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }else {
                snackbar = Snackbar.make(view, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_LONG);
                setSnackbar();
            }
        });
    }
    private void sendverificationcode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;
            Log.i("codesent",s);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            snackbar = Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG);
            Log.i("phnmsg",e.getMessage());
            setSnackbar();
        }
    };
    private void setSnackbar () {
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        snackbar.getView().setBackgroundColor(Color.parseColor("#000000"));
        snackbar.show();
    }
}
