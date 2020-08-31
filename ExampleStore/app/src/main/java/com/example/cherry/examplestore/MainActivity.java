package com.example.cherry.examplestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    StorageReference reference;
    DatabaseReference dreference;
    Uri uri;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        reference = FirebaseStorage.getInstance().getReference();
        dreference = FirebaseDatabase.getInstance().getReference("Storage");
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                //for  pdf
                //i.setType("application/pdf");
                i.setAction(Intent.ACTION_GET_CONTENT);
                //This code is to select an image from your device
                startActivityForResult(Intent
                        .createChooser(i,"Select Image"),0);
            }
        });
        dreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String link  = dataSnapshot.getValue(String.class);
                    Picasso.with(MainActivity.this).load(link).into(iv);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            iv.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void upload(View view) {
        final StorageReference ref = reference.child("images/pic.jpg");
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "uploaded",
                        Toast.LENGTH_SHORT).show();
                //To make the image view empty again
                String link = ref.getDownloadUrl().toString();
                String id = dreference.push().getKey();
                dreference.child(id).setValue(link);
                Toast.makeText(MainActivity.this, ""+link,
                        Toast.LENGTH_SHORT).show();
                iv.setImageBitmap(null);
            }
        });
    }
}