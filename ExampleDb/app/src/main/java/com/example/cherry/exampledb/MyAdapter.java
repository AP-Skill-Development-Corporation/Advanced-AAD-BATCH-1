package com.example.cherry.exampledb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.HoldView> {
    Context ct;
    ArrayList<MyModel> list;
    DatabaseReference reference;
    public MyAdapter(Context ct, ArrayList<MyModel> list) {
        this.ct = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public HoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoldView(LayoutInflater.from(ct).inflate(R.layout.row,
                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoldView holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.roll.setText(list.get(position).getRoll());
        holder.number.setText(list.get(position).getNumber());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("Data");
                //Code to delete the data from the firebase database
                reference.child(list.get(position).getRoll()).removeValue();
                Toast.makeText(ct, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ct);
                ViewGroup group = view.findViewById(R.id.content);
                View v = LayoutInflater.from(ct).inflate(R.layout.updata,
                        group,false);
                builder.setView(v);
                builder.setCancelable(false);
                final EditText sname = v.findViewById(R.id.name);
                EditText sroll = v.findViewById(R.id.roll);
                final EditText snum = v.findViewById(R.id.number);

                sname.setText(list.get(position).getName());
                sroll.setText(list.get(position).getRoll());
                sroll.setEnabled(false);
                snum.setText(list.get(position).getNumber());

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("Data").orderByChild("roll")
                                .equalTo(list.get(position).getRoll());
                        final HashMap<String,Object> map = new HashMap<>();
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    map.put("name",sname.getText().toString());
                                    map.put("number",snum.getText().toString());
                                    dataSnapshot.getRef().updateChildren(map);
                                    Toast.makeText(ct, "Data Updated",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HoldView extends RecyclerView.ViewHolder {
        TextView name,roll,number;
        ImageView edit,del;
        public HoldView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            roll = itemView.findViewById(R.id.roll);
            number = itemView.findViewById(R.id.number);
            edit = itemView.findViewById(R.id.edit);
            del = itemView.findViewById(R.id.del);
        }
    }
}
