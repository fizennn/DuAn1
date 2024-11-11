package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonHangDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public DonHangDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<DonHang> donHangList);
    }

    public void getAllDonHang(FirebaseCallback callback) {
        database.child(authen.getUID()).child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DonHang> donHangList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DonHang donHang = data.getValue(DonHang.class);
                    donHangList.add(donHang);
                }
                callback.onCallback(donHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addDonHang(DonHang donHang) {
        String idDh = donHang.getId_dh(); // Lấy id_dh từ đối tượng
        database.child(authen.getUID()).child("DonHang").child(idDh).setValue(donHang); // Sử dụng id_dh từ donHang
    }

    public void updateDonHang(DonHang donHang) {
        database.child(authen.getUID()).child("DonHang").child(donHang.getId_dh()).setValue(donHang);
    }

    public void deleteDonHang(String id) {
        database.child(authen.getUID()).child("DonHang").child(id).removeValue();
    }
}
