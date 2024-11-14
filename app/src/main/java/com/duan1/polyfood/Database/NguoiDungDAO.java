package com.duan1.polyfood.Database;

import android.net.Uri;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NguoiDung;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NguoiDungDAO {
    String TAG = "zzzzzzzzzzzzz";
    private DatabaseReference database;
    private AuthenticationFireBaseHelper auth;
    private ArrayList<NguoiDung> nguoiDungList;
    private StorageReference storageReference;



    public interface FirebaseCallback {
        void onCallback(NguoiDung nguoiDung);
    }

    public NguoiDungDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        auth = new AuthenticationFireBaseHelper();
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");
    }

    public void getAllNguoiDung(FirebaseCallback callback) {
        Log.d(TAG, "getAllNguoiDung: "+auth.getUID());
            if (auth.getUID()==null){
                callback.onCallback(null);
            }
            database.child("NguoiDung").child(auth.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                    callback.onCallback(nguoidung);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void check(FirebaseCallback callback,String UID) {


        database.child("NguoiDung").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                callback.onCallback(nguoidung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void getAllNguoiDungRealTime(FirebaseCallback callback) {

        database.child("NguoiDung").child(auth.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                callback.onCallback(nguoidung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void  addNguoiDungImg(NguoiDung nguoiDung, Uri img){
        StorageReference imgRef = storageReference.child(nguoiDung.getHoTen() + ".jpg");

        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            nguoiDung.setimgUrl(uri.toString());
                            database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung);
                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
    }

    public void addNguoiDung(NguoiDung nguoiDung) {
        String key = database.child("NguoiDung").push().getKey();
        nguoiDung.setId_nd(key);
        database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        database.child("NguoiDung").child(auth.getUID()).child(nguoiDung.getId_nd()).setValue(nguoiDung);
    }

    public void deleteNguoiDung(String id) {
        database.child("NguoiDung").child(auth.getUID()).child(id).removeValue();
    }


}
