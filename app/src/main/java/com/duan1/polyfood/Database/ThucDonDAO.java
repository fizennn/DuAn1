package com.duan1.polyfood.Database;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThucDonDAO {
    private DatabaseReference database;
    private StorageReference storageReference;
    private AuthenticationFireBaseHelper authen;

    public ThucDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
        storageReference = FirebaseStorage.getInstance().getReference("ThucDonImages");
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<ThucDon> thucDonList);
    }

    public void getAllThucDon(FirebaseCallback callback) {
        database.child("ThucDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ThucDon> thucDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ThucDon thucDon = data.getValue(ThucDon.class);
                    thucDonList.add(thucDon);
                }
                callback.onCallback(thucDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void addThucDon(ThucDon thucDon) {
//        String key = database.child("ThucDon").push().getKey();
//        thucDon.setId_td(key);
//        database.child(authen.getUID()).child("ThucDon").child(key).setValue(thucDon);
//    }

    public void addThucDon(ThucDon thucDon, String imageName) {
        String key = database.child("ThucDon").push().getKey();
        if (key != null) {
            thucDon.setId_td(key);

            StorageReference imgRef = storageReference.child(imageName + ".jpg");

            // Lấy URL của ảnh từ Storage và lưu vào Realtime Database
            imgRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        thucDon.setHinhAnh(uri.toString());
                        database.child("ThucDon").child(key).setValue(thucDon);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Image not found in Storage: " + e.getMessage());
                        // Optionally handle missing images here
                    });
        } else {
            Log.e("Firebase", "Không thể tạo khóa cho thực đơn");
        }
    }

    public void updateThucDon(ThucDon thucDon) {
        database.child(authen.getUID()).child("ThucDon").child(thucDon.getId_td()).setValue(thucDon);
    }

    public void deleteThucDon(String id) {
        database.child(authen.getUID()).child("ThucDon").child(id).removeValue();
    }
}
