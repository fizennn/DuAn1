package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ThucDonDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public ThucDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public void getAllThucDon(ArrayList<ThucDon> thucDonList, Runnable onComplete) {
        database.child("ThucDon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ThucDon thucDon = snapshot.getValue(ThucDon.class);
                thucDonList.add(thucDon);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ThucDon thucDon = snapshot.getValue(ThucDon.class);
                for (int i = 0; i < thucDonList.size(); i++) {
                    if (thucDonList.get(i).getId_td().equals(thucDon.getId_td())) {
                        thucDonList.set(i, thucDon);  // Cập nhật thực đơn trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ThucDon thucDon = snapshot.getValue(ThucDon.class);
                thucDonList.removeIf(td -> td.getId_td().equals(thucDon.getId_td()));  // Xóa thực đơn khỏi danh sách
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

    public void addThucDon(ThucDon thucDon) {
        String key = database.child("ThucDon").push().getKey();
        thucDon.setId_td(key);
        database.child(authen.getUID()).child("ThucDon").child(key).setValue(thucDon);
    }

    public void updateThucDon(ThucDon thucDon) {
        database.child(authen.getUID()).child("ThucDon").child(thucDon.getId_td()).setValue(thucDon);
    }

    public void deleteThucDon(String id) {
        database.child(authen.getUID()).child("ThucDon").child(id).removeValue();
    }
}
