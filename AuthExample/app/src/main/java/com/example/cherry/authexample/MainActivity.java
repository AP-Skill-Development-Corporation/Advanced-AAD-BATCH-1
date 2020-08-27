package com.example.cherry.authexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText mail, pass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    public void login(View view) {
        String umail = mail.getText().toString();
        String upass = pass.getText().toString();
        if (umail.isEmpty() | upass.isEmpty()) {
            Toast.makeText(this, "Fill all the details",
                    Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(umail, upass).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,
                                        "Successfully Logged in",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void reset(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.reset, null);
        builder.setView(v);
        final EditText email = v.findViewById(R.id.umail);
        builder.setCancelable(false);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String mail = email.getText().toString();
                auth.sendPasswordResetEmail(mail).addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this,"Reset email sent",
                                            Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void phoneauth(View view) {
        startActivity(new Intent(this,PhoneAuth.class));
    }
}