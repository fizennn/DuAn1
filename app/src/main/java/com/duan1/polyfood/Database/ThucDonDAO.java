package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThucDonDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public ThucDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<ThucDon> thucDonList);
    }

    public void getAllThucDon(FirebaseCallback callback) {
        database.child("ThucDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ThucDon> thucDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ThucDon thucDon = data.getValue(ThucDon.class);
                    thucDonList.add(thucDon);
                }
                callback.onCallback(thucDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
