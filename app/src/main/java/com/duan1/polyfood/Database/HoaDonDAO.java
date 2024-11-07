package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.HoaDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HoaDonDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public HoaDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public void getAllHoaDon(ArrayList<HoaDon> hoaDonList, Runnable onComplete) {
        database.child("HoaDon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                hoaDonList.add(hoaDon);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                for (int i = 0; i < hoaDonList.size(); i++) {
                    if (hoaDonList.get(i).getId_tt().equals(hoaDon.getId_tt())) {
                        hoaDonList.set(i, hoaDon);  // Cập nhật hóa đơn trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                hoaDonList.removeIf(hd -> hd.getId_tt().equals(hoaDon.getId_tt()));  // Xóa hóa đơn khỏi danh sách
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

    public void addHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").push().getKey();
        hoaDon.setId_tt(key);
        database.child(authen.getUID()).child("HoaDon").child(key).setValue(hoaDon);
    }

    public void updateHoaDon(HoaDon hoaDon) {
        database.child(authen.getUID()).child("HoaDon").child(hoaDon.getId_tt()).setValue(hoaDon);
    }

    public void deleteHoaDon(String id) {
        database.child(authen.getUID()).child("HoaDon").child(id).removeValue();
    }
}
