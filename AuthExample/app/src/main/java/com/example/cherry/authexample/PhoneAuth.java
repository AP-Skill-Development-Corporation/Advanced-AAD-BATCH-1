package com.example.cherry.authexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    EditText number,code;
    FirebaseAuth auth;

    // To get the otp code,auto verification and failure messages
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    //This token is used to perform resend otp
    PhoneAuthProvider.ForceResendingToken token;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        number = findViewById(R.id.number);
        code = findViewById(R.id.code);
        auth = FirebaseAuth.getInstance();

        //Instantiate the callbacks here
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //Verification id
                id = s;
                //forced token
                token = forceResendingToken;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //This is to perform auto verification
                signPhoneAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneAuth.this, "Code Sending failed",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void signPhoneAuth(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(PhoneAuth.this,
                                    HomeActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(PhoneAuth.this, "failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendotp(View view) {
        String num = number.getText().toString();
        //To send otp by capturing the phone number in the edit text
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber("+91"+num,60, TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        callbacks);
    }

    public void verify(View view) {
        String rcode = code.getText().toString();
        //To authenticate the code manually
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,rcode);
        signPhoneAuth(credential);
    }

    public void resend(View view) {
        String num = number.getText().toString();
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber("+91"+num,60,TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        callbacks,token);
    }
}