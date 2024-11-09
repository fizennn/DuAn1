package com.duan1.polyfood.Database;

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

import java.util.ArrayList;

public class NguoiDungDAO {
    String TAG = "zzzzzzzzzzzzz";
    private DatabaseReference database;
    private AuthenticationFireBaseHelper auth;
    private ArrayList<NguoiDung> nguoiDungList;



    public interface FirebaseCallback {
        void onCallback(NguoiDung nguoiDung);
    }

    public NguoiDungDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        auth = new AuthenticationFireBaseHelper();
    }

    public void getAllNguoiDung(FirebaseCallback callback) {
        Log.d(TAG, "getAllNguoiDung: "+auth.getUID());
            database.child(auth.getUID()).child("NguoiDung").addListenerForSingleValueEvent(new ValueEventListener() {
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

        database.child(auth.getUID()).child("NguoiDung").addValueEventListener(new ValueEventListener() {
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

    public void addNguoiDung(NguoiDung nguoiDung) {
        String key = database.child("NguoiDung").push().getKey();
        nguoiDung.setId_nd(key);
        database.child(auth.getUID()).child("NguoiDung").setValue(nguoiDung);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        database.child(auth.getUID()).child("NguoiDung").child(nguoiDung.getId_nd()).setValue(nguoiDung);
    }

    public void deleteNguoiDung(String id) {
        database.child(auth.getUID()).child("NguoiDung").child(id).removeValue();
    }


}
