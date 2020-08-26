package com.example.cherry.authexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv = findViewById(R.id.tv);
        auth = FirebaseAuth.getInstance();
        tv.setText("Welcome "+auth.getCurrentUser().getEmail());
    }

    public void signout(View view) {
        auth.signOut();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}