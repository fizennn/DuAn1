package com.duan1.polyfood.Database;

import android.util.Log;

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
import java.util.Map;

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
        database.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    HoaDon hoaDon = data.getValue(HoaDon.class);
                    hoaDon.setId_hd(data.getKey());
                    hoaDonList.add(hoaDon);
                }
                callback.onCallback(hoaDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getHoaDonByStatus(String status, FirebaseCallback callback) {
        database.child("HoaDon")
                .orderByChild("trangThai").equalTo(status)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                            hoaDon.setId_hd(snapshot.getKey());
                            hoaDonList.add(hoaDon);
                        }
                        callback.onCallback(hoaDonList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read hoa don by status", databaseError.toException());
                    }
                });
    }


    public void addHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").push().getKey();
        if (key != null) {
            hoaDon.setId_nd(authen.getUID());
            hoaDon.setId_hd(key);
            database.child("HoaDon").child(key).setValue(hoaDon)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm hóa đơn thành công"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Thêm hóa đơn thất bại", e));
        } else {
            Log.e("Firebase", "Không thể tạo key cho hóa đơn mới");
        }
    }

    public void updateHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").getKey();

        Map<String, Object> hoaDonValues = hoaDon.toMap();

        database.child("HoaDon").child(hoaDon.getId_hd())
                .updateChildren(hoaDonValues)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Cập nhật hóa đơn thành công"))
                .addOnFailureListener(e -> Log.e("Firebase", "Cập nhật hóa đơn thất bại", e));
    }

    public void deleteHoaDon(String id) {
        database.child("HoaDon").child(id).removeValue();
    }

    public void getAllHoaDonChoXuLy(FirebaseCallback callback) {
        // Truy vấn đến tất cả các UID và lọc theo trạng thái "Chờ xử lý"
        database.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HoaDon> hoaDonList = new ArrayList<>();

                // Duyệt qua tất cả các UID
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Duyệt qua tất cả các hóa đơn của UID hiện tại
                    for (DataSnapshot hoaDonSnapshot : userSnapshot.getChildren()) {
                        HoaDon hoaDon = hoaDonSnapshot.getValue(HoaDon.class);

                        // Kiểm tra trạng thái "Chờ xử lý"
                        if (hoaDon != null && "Chờ xử lý".equals(hoaDon.getTrangThai())) {
                            hoaDon.setId_hd(hoaDonSnapshot.getKey());
                            hoaDonList.add(hoaDon);
                        }
                    }
                }

                // Gọi lại callback với danh sách các hóa đơn "Chờ xử lý"
                callback.onCallback(hoaDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching HoaDon for processing", error.toException());
            }
        });
    }
}
