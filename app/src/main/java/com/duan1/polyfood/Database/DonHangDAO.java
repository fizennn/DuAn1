package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DonHangDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public DonHangDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public void getAllDonHang(ArrayList<DonHang> donHangList, Runnable onComplete) {
        database.child("DonHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                donHangList.add(donHang);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                for (int i = 0; i < donHangList.size(); i++) {
                    if (donHangList.get(i).getId_dh().equals(donHang.getId_dh())) {
                        donHangList.set(i, donHang);  // Cập nhật đơn hàng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                donHangList.removeIf(d -> d.getId_dh().equals(donHang.getId_dh()));  // Xóa đơn hàng khỏi danh sách
                onComplete.run();  // Cập nhật giao diện khi dữ liệu bị xóa
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Không cần xử lý nếu không quan tâm đến thứ tự
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    public void addDonHang(DonHang donHang) {
//        tao tu dong id khi them
//        String key = database.child("DonHang").push().getKey();
//        donHang.setId_dh(key);
//        database.child("DonHang").child(key).setValue(donHang);

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
