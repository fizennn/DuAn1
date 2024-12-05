package com.duan1.polyfood.Database;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duan1.polyfood.Models.BinhLuan;
import com.duan1.polyfood.Models.ThucDon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class BinhLuanDao {
    private DatabaseReference database;
    private StorageReference storageReference;
    private boolean check;


    public BinhLuanDao(){
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("BinhLuanImages");
        ThucDonDAO thucDonDAO = new ThucDonDAO();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<BinhLuan> binhLuanList);
    }

    public void addBinhLuan(BinhLuan binhLuan, String uidThucDon, Uri img){

        String key = database.child("NhaHang").child("ThucDon").child(uidThucDon).push().getKey();
        StorageReference imgRef = storageReference.child(key + ".jpg");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = sdf.format(new Date());

        if (img==null){
            binhLuan.setNgay(currentDate);
            binhLuan.setIdbl(key);
            database.child("NhaHang").child("ThucDon").child(uidThucDon).child("BinhLuan").child(binhLuan.getIdbl()).setValue(binhLuan);
            return;
        }

        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            binhLuan.setNgay(currentDate);
                            binhLuan.setIdbl(key);
                            binhLuan.setAnh(uri.toString());
                            database.child("NhaHang").child("ThucDon").child(uidThucDon).child("BinhLuan").child(binhLuan.getIdbl()).setValue(binhLuan);

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    public void getBinhLuan(String uidThucDon, FirebaseCallback callback) {
        database.child("NhaHang").child("ThucDon").child(uidThucDon).child("BinhLuan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<BinhLuan> binhLuanList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            BinhLuan binhLuan = data.getValue(BinhLuan.class);
                            if (binhLuan != null) {
                                binhLuanList.add(binhLuan);
                            }
                        }

                        Collections.sort(binhLuanList, new Comparator<BinhLuan>() {
                            @Override
                            public int compare(BinhLuan o1, BinhLuan o2) {
                                // So sánh ngày tháng trực tiếp vì định dạng yyyy-MM-dd có thể so sánh đúng
                                return o1.getNgay().compareTo(o2.getNgay());
                            }
                        });

                        callback.onCallback(binhLuanList);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Failed to retrieve comments: " + error.getMessage());
                        callback.onCallback(new ArrayList<>()); // Return empty list on failure
                    }
                });
    }
}
