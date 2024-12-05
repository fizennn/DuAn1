package com.duan1.polyfood.Database;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.NguoiDung;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NguoiDungDAO {
    String TAG = "zzzzzzzzzzzzz";
    private DatabaseReference database;
    private AuthenticationFireBaseHelper auth;
    private ArrayList<NguoiDung> nguoiDungList;
    private StorageReference storageReference;


    public interface FirebaseCallback {
        void onCallback(NguoiDung nguoiDung);
    }
    public interface onSuccess{
        void success(boolean success);
    }

    public interface NotiCallBack{
        void onCallBack(int noti);
    }

    public NguoiDungDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        auth = new AuthenticationFireBaseHelper();
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");
    }

    public void getAllNguoiDung(FirebaseCallback callback) {
        Log.d(TAG, "getAllNguoiDung: "+auth.getUID());
            if (auth.getUID()==null){
                callback.onCallback(null);
            }
            database.child("NguoiDung").child(auth.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                    callback.onCallback(nguoidung);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void getNoti(NotiCallBack callBack){
    }
    public void getNoti1(NotiCallBack callBack){
    }
    public void addNoti(String uidNN){
    }

    public void getAllNguoiDungByID(String UID,FirebaseCallback callback) {
        Log.d(TAG, "getAllNguoiDung: "+auth.getUID());
        if (auth.getUID()==null){
            callback.onCallback(null);
        }
        database.child("NguoiDung").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                callback.onCallback(nguoidung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void check(FirebaseCallback callback,String UID) {


        database.child("NguoiDung").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                callback.onCallback(nguoidung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void getAllNguoiDungRealTime(FirebaseCallback callback) {

        database.child("NguoiDung").child(auth.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoidung = snapshot.getValue(NguoiDung.class);
                callback.onCallback(nguoidung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void  addNguoiDungImg(NguoiDung nguoiDung, Uri img){

        if (img==null){
            database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung);
            return;
        }


        StorageReference imgRef = storageReference.child(nguoiDung.getHoTen() + ".jpg");

        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            nguoiDung.setimgUrl(uri.toString());

                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
    }

    public void  update(NguoiDung nguoiDung, Uri img,onSuccess success){


        if (img==null){
            database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    success.success(true);
                }
            });

            return;
        }


        StorageReference imgRef = storageReference.child(nguoiDung.getHoTen() + ".jpg");

        imgRef.putFile(img).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            nguoiDung.setimgUrl(uri.toString());
                            database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    success.success(true);
                                }
                            });
                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
    }



    public void addNguoiDung(NguoiDung nguoiDung) {
        String key = database.child("NguoiDung").push().getKey();
        nguoiDung.setId_nd(key);
        database.child("NguoiDung").child(auth.getUID()).setValue(nguoiDung);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        database.child("NguoiDung").child(auth.getUID()).child(nguoiDung.getId_nd()).setValue(nguoiDung);
    }

    public void deleteNguoiDung(String id) {
        database.child("NguoiDung").child(auth.getUID()).child(id).removeValue();
    }

    public void checkMonAnYeuThich(String UIDMonAn,FirebaseCallback callback) {
        Log.d(TAG, "getAllNguoiDung: "+auth.getUID());
        if (auth.getUID()==null){
            callback.onCallback(null);
        }
        database.child("NhaHang").child("FavouriteDish").child(auth.getUID()).child(UIDMonAn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = new NguoiDung();
                if (snapshot.exists()){
                    nguoiDung.setHoTen("huy");
                    Log.d(TAG, "onDataChange: Da Lay Dc Mon An");
                }
                callback.onCallback(nguoiDung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setImg(){
        database.child("NguoiDung").child(auth.getUID()).child("imgUrl").setValue("https://firebasestorage.googleapis.com/v0/b/shopcake-528de.appspot.com/o/ProfileImages%2Fdefault.jpg?alt=media&token=6d2979a1-ee9c-4fb4-b6a8-574566ef31f3");
    }







}
