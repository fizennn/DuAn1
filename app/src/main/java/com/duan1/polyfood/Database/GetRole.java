package com.duan1.polyfood.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetRole {

    private CALLBACK callback;
    private AuthenticationFireBaseHelper auth;

    public interface CALLBACK{
        void getRole(int role);
    }

    public GetRole() {
        auth = new AuthenticationFireBaseHelper();
    }

    public void getRole(CALLBACK callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("NguoiDung").child(auth.getUID());

        // Lấy giá trị của "role"
        userRef.child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int role = dataSnapshot.getValue(Integer.class);
                    callback.getRole(role);
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
