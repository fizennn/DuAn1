package com.duan1.polyfood.Database;

import android.content.Context;
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
import java.util.Collections;
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


    public void guiThongBao(ThongBao thongBao, Context context){
        String key = database.child("ThongBao").child(thongBao.getId_nn()).push().getKey();
        if (key != null) {
            thongBao.setNgayThang(getCurrentDateTime());
            thongBao.setId_ng(authen.getUID());
           thongBao.setId_tb(key);
            database.child("ThongBao").child(thongBao.getId_nn()).child(thongBao.getId_tb()).setValue(thongBao)
                    .addOnSuccessListener(aVoid -> {
                        FirebaseNotification notification = new FirebaseNotification();
                        notification.senNoti(context,thongBao.getRole(),thongBao.getNoidung(),thongBao.getId_nn());
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Thêm thong bao thất bại", e));
            nguoiDungDAO.addNoti(thongBao.getId_nn());
        } else {
            Log.e("Firebase", "Không thể tạo key cho thong bao mới");
        }
    }

    public void getAllThongBao(FirebaseCallback callback){
        String uid = authen.getUID();

        if (uid!=null){
            database.child("ThongBao").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ThongBao> list = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()){
                        ThongBao thongBao = data.getValue(ThongBao.class);
                        if (thongBao.getGone()==null){
                            list.add(thongBao);
                        }

                    }

                    list = addDateChangeText(list);
                    Collections.reverse(list);
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
    public void setGone(ThongBao thongBao){
        database.child("ThongBao").child(thongBao.getId_nn()).child(thongBao.getId_tb()).child("gone").setValue("gone");
    }

    public static String getCurrentDateTime() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Định dạng thời gian (ví dụ: dd/MM/yyyy HH:mm:ss)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Trả về chuỗi định dạng ngày giờ
        return dateFormat.format(calendar.getTime());
    }

    public static ArrayList<ThongBao> addDateChangeText(ArrayList<ThongBao> dateList) {
        ArrayList<ThongBao> resultList = new ArrayList<>();

        // Biến để theo dõi ngày trước đó
        String previousDate = null;

        Log.e("zzzzzzzzz", "addDateChangeText: Bat Dau");



        for (ThongBao thongBao : dateList) {
            // Lấy phần ngày của chuỗi (dùng substring để lấy "yyyy-MM-dd")
            String currentDate = thongBao.getNgayThang().substring(0, 10);  // Lấy "yyyy-MM-dd"

            // Kiểm tra xem ngày có thay đổi không
            if (previousDate != null && !currentDate.equals(previousDate)) {
                // Nếu ngày thay đổi, thêm text "Chuyển ngày" vào
                ThongBao thongBaoChuyen = new ThongBao();
                thongBaoChuyen.setChuyenNgay(previousDate);
                resultList.add(thongBaoChuyen);
                resultList.add(thongBao);
            } else {
                resultList.add(thongBao);
            }

            // Cập nhật ngày trước đó
            previousDate = currentDate;
        }

        ThongBao thongBaoChuyen1 = new ThongBao();
        thongBaoChuyen1.setChuyenNgay(previousDate);
        resultList.add(thongBaoChuyen1);



        return resultList;
    }


}
