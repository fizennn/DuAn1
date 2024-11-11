package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.HoaDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HoaDonDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public HoaDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<HoaDon> hoaDonList);
    }

    public void getAllHoaDon(FirebaseCallback callback) {
        database.child(authen.getUID()).child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    HoaDon hoaDon = data.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                }
                callback.onCallback(hoaDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").push().getKey();
        database.child(authen.getUID()).child("HoaDon").child(key).setValue(hoaDon);
    }

    public void updateHoaDon(HoaDon hoaDon) {
        database.child(authen.getUID()).child("HoaDon").child(hoaDon.getId_hd()).setValue(hoaDon);
    }

    public void deleteHoaDon(String id) {
        database.child(authen.getUID()).child("HoaDon").child(id).removeValue();
    }
}
