package com.example.cherry.examplestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    RecyclerView rv;
    StorageReference ref;
    DatabaseReference dreference;
    ProgressDialog progressDialog;
    Uri uri;
    ArrayList<Upload> list;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        rv = findViewById(R.id.rv);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("uploading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ref = FirebaseStorage.getInstance()
                .getReference();

        dreference = FirebaseDatabase.getInstance().getReference("Storage");
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                //for  pdf : i.setType("application/pdf");
                //to select multiple files : i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                //This code is to select an image from your device
                startActivityForResult(Intent
                        .createChooser(i,"Select Image"),0);
            }
        });
        dreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    list.add(upload);
                    MyAdapter adapter = new MyAdapter(list,MainActivity.this);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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

        progressDialog.show();
        final StorageReference reference = ref.child("images/"+ UUID.randomUUID().toString());
       reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String url = uri.toString();
                       Upload upload = new Upload(url);
                       dreference.child(dreference.push().getKey()).setValue(upload);
                       progressDialog.dismiss();
                   }
               });
           }
       });
    }
}