package com.duan1.polyfood.Database;

import android.net.Uri;
import android.util.Log;

import com.duan1.polyfood.Models.NguoiDung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.duan1.polyfood.Models.Sticker;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StickerDao {

    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    public StickerDao() {
        // Khởi tạo tham chiếu đến Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("NhaHang").child("Nhan");
        storageReference = FirebaseStorage.getInstance().getReference("StickerImages");
    }

    // Thêm Sticker mới
    public void addSticker(Sticker sticker) {
        // Lấy ID tự động tạo cho mỗi sticker
        String id = mDatabase.push().getKey();
        if (id != null) {
            // Gán sticker với ID
            sticker.setId(id);
            // Thêm sticker vào Firebase
            mDatabase.child(id).setValue(sticker)
                    .addOnSuccessListener(aVoid -> {
                        // Xử lý nếu thêm thành công
                        Log.d("StickerDao", "Sticker added successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý nếu thất bại
                        Log.e("StickerDao", "Error adding sticker: " + e.getMessage());
                    });
        }
    }

    public void  addStickerImg(Sticker sticker, Uri img){
        String id = mDatabase.push().getKey();
        StorageReference imgRef = storageReference.child(id + ".jpg");


        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            sticker.setId(id);
                            sticker.setImageUri(uri.toString());
                            mDatabase.child(id).setValue(sticker);
                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
    }

    // Cập nhật Sticker
    public void updateSticker(Sticker sticker,Uri img) {
        String stickerId = String.valueOf(sticker.getId());
        // Cập nhật sticker với ID tương ứng


        if (img == null){
            Log.d("zzzzzzzzzz", "updateSticker: "+stickerId);
            mDatabase.child(stickerId).setValue(sticker);
            return;
        }

        StorageReference imgRef = storageReference.child(stickerId + ".jpg");




        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            sticker.setImageUri(uri.toString());
                            mDatabase.child(stickerId).setValue(sticker);
                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
    }

    public void getAll(final StickerCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sticker> stickerList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sticker sticker = snapshot.getValue(Sticker.class);
                    stickerList.add(sticker);
                }
                // Gọi callback và truyền danh sách sticker
                callback.onSuccess(stickerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi
                Log.e("StickerDao", "Error fetching stickers: " + databaseError.getMessage());
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public void getStickerById(String stickerId, final StickerCallback callback) {
        mDatabase.child(stickerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Chuyển đổi DataSnapshot thành Sticker object
                    Sticker sticker = dataSnapshot.getValue(Sticker.class);
                    callback.onSuccess(sticker);
                } else {
                    // Không tìm thấy đối tượng
                    callback.onFailure("Sticker not found with ID: " + stickerId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi
                Log.e("StickerDao", "Error fetching sticker: " + databaseError.getMessage());
                callback.onFailure(databaseError.getMessage());
            }
        });
    }



    // Callback interface sửa lại để hỗ trợ cả Sticker và danh sách Sticker
    public interface StickerCallback {
        void onSuccess(Sticker sticker); // Cho trường hợp lấy 1 đối tượng
        void onSuccess(List<Sticker> stickerList); // Cho trường hợp lấy nhiều đối tượng
        void onFailure(String error);
    }
}
