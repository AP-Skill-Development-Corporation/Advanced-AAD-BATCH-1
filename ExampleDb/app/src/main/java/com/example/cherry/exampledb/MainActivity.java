package com.example.cherry.exampledb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cherry.exampledb.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        reference = FirebaseDatabase.getInstance().getReference("Data");
    }


    public void save(View view) {
        String sname = binding.name.getText().toString();
        String sroll = binding.roll.getText().toString();
        String snumber = binding.number.getText().toString();
        MyModel model = new MyModel(sname,sroll,snumber);
       //String id = reference.push().getKey();
        reference.child(sroll).setValue(model);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
    }
}