package com.duan1.polyfood.Database;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NhaHang;
import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThucDonDAO {
    private DatabaseReference database;
    private StorageReference storageReference;


    public ThucDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("ThucDonImages");
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<ThucDon> thucDonList);
    }

    public void getAllThucDon(FirebaseCallback callback) {
        database.child("NhaHang").child("ThucDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ThucDon> thucDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        ThucDon thucDon = data.getValue(ThucDon.class);
                        thucDonList.add(thucDon);
                    } catch (DatabaseException e) {
                        Log.e("Firebase", "Failed to convert data: " + e.getMessage());
                    }
                }
                if (thucDonList.isEmpty()) {
                    Log.d("Firebase", "Data snapshot is empty");
                } else {
                    Log.d("Firebase", "Retrieved data: " + thucDonList.toString());
                }
                callback.onCallback(thucDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }


    public void addThucDon(ThucDon thucDon, Uri imageUri) {
        String key = database.child("NhaHang").child("ThucDon").push().getKey();
        if (key != null) {
            thucDon.setId_td(key);
            StorageReference imgRef = storageReference.child(key + ".jpg");

            imgRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                thucDon.setHinhAnh(uri.toString());
                                database.child("NhaHang").child("ThucDon").child(key).setValue(thucDon);
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
        }
    }


    public void updateThucDon(ThucDon thucDon) {
        database.child("NhaHang").child("ThucDon").child(thucDon.getId_td()).setValue(thucDon);
    }

    public void deleteThucDon(String id) {
        database.child("NhaHang").child("ThucDon").child(id).removeValue();
    }

}
