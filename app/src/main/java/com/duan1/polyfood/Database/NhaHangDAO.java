package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NhaHang;
import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NhaHangDAO {
    private DatabaseReference database;

    public NhaHangDAO() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<NhaHang> nhaHangList);
        void onCallback(NhaHang nhaHang);
    }

    public void getAllNhaHang(FirebaseCallback callback) {
        database.child("NhaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NhaHang> nhaHangList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NhaHang nhaHang = data.getValue(NhaHang.class);
                    nhaHangList.add(nhaHang);
                }
                callback.onCallback(nhaHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNhaHang(NhaHang nhaHang) {
        String idNh = nhaHang.getId_nh();
        database.child("NhaHang").child(idNh).setValue(nhaHang);
    }

    public void updateNhaHang(NhaHang nhaHang) {
        database.child("NhaHang").child(nhaHang.getId_nh()).setValue(nhaHang);
    }

    public void deleteNhaHang(String id) {
        database.child("NhaHang").child(id).removeValue();
    }


    public void getThongTin(FirebaseCallback callback) {
        database.child("NhaHang").child("ThongTin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                callback.onCallback(nhaHang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
