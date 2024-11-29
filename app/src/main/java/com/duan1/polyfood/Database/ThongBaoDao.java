package com.duan1.polyfood.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ThongBaoDao {

    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;
    private NguoiDungDAO nguoiDungDAO;

    public ThongBaoDao() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
        nguoiDungDAO = new NguoiDungDAO();
    }
    public interface FirebaseCallback {
        void onCallback(ArrayList<ThongBao> thongBaoList);
    }


    public void guiThongBao(ThongBao thongBao){
        String key = database.child("ThongBao").child(thongBao.getId_nn()).push().getKey();
        if (key != null) {
            thongBao.setNgayThang(getCurrentDateTime());
            thongBao.setId_ng(authen.getUID());
           thongBao.setId_tb(key);
            database.child("ThongBao").child(thongBao.getId_nn()).child(thongBao.getId_tb()).setValue(thongBao)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm thong bao thành công"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Thêm thong bao thất bại", e));
            nguoiDungDAO.addNoti(thongBao.getId_nn());
        } else {
            Log.e("Firebase", "Không thể tạo key cho thong bao mới");
        }
    }

    public void getAllThongBao(FirebaseCallback callback){
        String uid = authen.getUID();

        if (uid!=null){
            database.child("ThongBao").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ThongBao> list = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()){
                        ThongBao thongBao = data.getValue(ThongBao.class);
                        list.add(thongBao);
                    }
                    callback.onCallback(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





    }

    public void getNoti(FirebaseCallback callback){
        String uid = authen.getUID();

        if (uid!=null){
            database.child("ThongBao").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ThongBao> list = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()){
                        ThongBao thongBao = data.getValue(ThongBao.class);
                        if (thongBao.getRead()==null){
                            list.add(thongBao);
                        }
                    }
                    callback.onCallback(list);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





    }

    public void setReaded(ThongBao thongBao){
        database.child("ThongBao").child(thongBao.getId_nn()).child(thongBao.getId_tb()).child("read").setValue("readed");
    }

    public static String getCurrentDateTime() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Định dạng thời gian (ví dụ: dd/MM/yyyy HH:mm:ss)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Trả về chuỗi định dạng ngày giờ
        return dateFormat.format(calendar.getTime());
    }
}
