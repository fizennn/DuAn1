package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NhaHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NhaHangDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public NhaHangDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }


    public void getAllNhaHang(ArrayList<NhaHang> nhaHangList, Runnable onComplete) {
        database.child("NhaHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                nhaHangList.add(nhaHang);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                for (int i = 0; i < nhaHangList.size(); i++) {
                    if (nhaHangList.get(i).getId_nh().equals(nhaHang.getId_nh())) {
                        nhaHangList.set(i, nhaHang);  // Cập nhật nhà hàng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                nhaHangList.removeIf(nh -> nh.getId_nh().equals(nhaHang.getId_nh()));  // Xóa nhà hàng khỏi danh sách
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

    public void addNhaHang(NhaHang nhaHang) {
        String idNh = nhaHang.getId_nh();
        database.child(authen.getUID()).child("NhaHang").child(idNh).setValue(nhaHang);
    }

    public void updateNhaHang(NhaHang nhaHang) {
        database.child(authen.getUID()).child("NhaHang").child(nhaHang.getId_nh()).setValue(nhaHang);
    }

    public void deleteNhaHang(String id) {
        database.child(authen.getUID()).child("NhaHang").child(id).removeValue();
    }
}
