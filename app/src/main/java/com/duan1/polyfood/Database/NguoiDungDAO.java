package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NguoiDung;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NguoiDungDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public NguoiDungDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public void getAllNguoiDung(ArrayList<NguoiDung> nguoiDungList, Runnable onComplete) {
        database.child("NguoiDung").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                nguoiDungList.add(nguoiDung);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                for (int i = 0; i < nguoiDungList.size(); i++) {
                    if (nguoiDungList.get(i).getId_nd().equals(nguoiDung.getId_nd())) {
                        nguoiDungList.set(i, nguoiDung);  // Cập nhật người dùng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                nguoiDungList.removeIf(nd -> nd.getId_nd().equals(nguoiDung.getId_nd()));  // Xóa người dùng khỏi danh sách
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

    public void addNguoiDung(NguoiDung nguoiDung) {
        String key = database.child("NguoiDung").push().getKey();
        nguoiDung.setId_nd(key);
        database.child(authen.getUID()).child("NguoiDung").child(key).setValue(nguoiDung);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        database.child(authen.getUID()).child("NguoiDung").child(nguoiDung.getId_nd()).setValue(nguoiDung);
    }

    public void deleteNguoiDung(String id) {
        database.child(authen.getUID()).child("NguoiDung").child(id).removeValue();
    }
}
