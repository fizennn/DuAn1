package com.duan1.polyfood.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HoaDonDAO {
    private DatabaseReference database;
    private AuthenticationFireBaseHelper authen;

    public HoaDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        authen = new AuthenticationFireBaseHelper();
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<HoaDon> hoaDonList);
        void onCallback(HoaDon hoaDon);
    }

    public void getAllHoaDon(FirebaseCallback callback) {
        database.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    HoaDon hoaDon = data.getValue(HoaDon.class);
                    hoaDon.setId_hd(data.getKey());
                    hoaDonList.add(hoaDon);
                }
                ArrayList<HoaDon> list = new ArrayList<>();
                for (HoaDon don: hoaDonList){
                    if (don.getDisabale()==null){
                        list.add(don);
                    }
                }

                Collections.sort(list, new Comparator<HoaDon>() {
                    @Override
                    public int compare(HoaDon o1, HoaDon o2) {
                        // So sánh ngày tháng trực tiếp vì định dạng yyyy-MM-dd có thể so sánh đúng
                        return o2.getNgayDatHang().compareTo(o1.getNgayDatHang());
                    }
                });
                if(list != null) {
                    callback.onCallback(list);
                }else {
                    Log.d("TAG", "onDataChange: " + null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getHoaDonByStatus(String status, FirebaseCallback callback) {
        database.child("HoaDon")
                .orderByChild("trangThai").equalTo(status)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                            hoaDon.setId_hd(snapshot.getKey());
                            hoaDonList.add(hoaDon);
                        }

                        ArrayList<HoaDon> list = new ArrayList<>();
                        for (HoaDon don: hoaDonList){
                            if (don.getDisabale()==null){
                                list.add(don);
                            }
                        }
                        Collections.sort(list, new Comparator<HoaDon>() {
                            @Override
                            public int compare(HoaDon o1, HoaDon o2) {
                                // So sánh ngày tháng trực tiếp vì định dạng yyyy-MM-dd có thể so sánh đúng
                                return o2.getNgayDatHang().compareTo(o1.getNgayDatHang());
                            }
                        });
                        callback.onCallback(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read hoa don by status", databaseError.toException());
                    }
                });
    }

    public void getHoaDonById(String id, FirebaseCallback callback) {

        try {
            if (id==null){
                return;
            }
            database.child("HoaDon").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);

                    callback.onCallback(hoaDon);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.e("zzzzz", "getHoaDonById: "+e );
        }


    }



    public void addHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").push().getKey();
        if (key != null) {
            hoaDon.setId_nd(authen.getUID());
            hoaDon.setId_hd(key);
            database.child("HoaDon").child(key).setValue(hoaDon)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm hóa đơn thành công"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Thêm hóa đơn thất bại", e));
        } else {
            Log.e("Firebase", "Không thể tạo key cho hóa đơn mới");
        }
    }

    public void updateHoaDon(HoaDon hoaDon) {
        String key = database.child("HoaDon").getKey();

        Map<String, Object> hoaDonValues = hoaDon.toMap();

        database.child("HoaDon").child(hoaDon.getId_hd())
                .updateChildren(hoaDonValues)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Cập nhật hóa đơn thành công"))
                .addOnFailureListener(e -> Log.e("Firebase", "Cập nhật hóa đơn thất bại", e));
    }

    public void deleteHoaDon(String id) {
        database.child("HoaDon").child(id).removeValue();
    }

    public void getAllHoaDonChoXuLy(FirebaseCallback callback) {
        // Truy vấn đến tất cả các UID và lọc theo trạng thái "Chờ xử lý"
        database.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HoaDon> hoaDonList = new ArrayList<>();

                // Duyệt qua tất cả các UID
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Duyệt qua tất cả các hóa đơn của UID hiện tại
                    for (DataSnapshot hoaDonSnapshot : userSnapshot.getChildren()) {
                        HoaDon hoaDon = hoaDonSnapshot.getValue(HoaDon.class);

                        // Kiểm tra trạng thái "Chờ xử lý"
                        if (hoaDon != null && "Chờ xử lý".equals(hoaDon.getTrangThai())) {
                            hoaDon.setId_hd(hoaDonSnapshot.getKey());
                            hoaDonList.add(hoaDon);
                        }
                    }
                }

                // Gọi lại callback với danh sách các hóa đơn "Chờ xử lý"
                callback.onCallback(hoaDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching HoaDon for processing", error.toException());
            }
        });
    }

    public void getTongDoanhThu(FirebaseCallback callback) {
        database.child("HoaDon")
                .orderByChild("trangThai").equalTo("Hoàn thành")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int tongDoanhThu = 0;
                        for (DataSnapshot hoaDonSnapshot : snapshot.getChildren()) {
                            HoaDon hoaDon = hoaDonSnapshot.getValue(HoaDon.class);
                            if (hoaDon != null) {
                                tongDoanhThu += hoaDon.getTongTien(); // Assumes `tongTien` is a property in HoaDon
                            }
                        }
                        // Gửi kết quả về thông qua callback
                        HoaDon doanhThuResult = new HoaDon();
                        doanhThuResult.setTongTien(tongDoanhThu); // Temporary object to send result
                        callback.onCallback(doanhThuResult);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to calculate total revenue", error.toException());
                    }
                });
    }

    public void getDoanhThuByDateRange(String status, String startDate, String endDate, FirebaseCallback callback) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Lọc hóa đơn theo trạng thái và khoảng ngày
        database.child("HoaDon")
                .orderByChild("trangThai").equalTo(status)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<HoaDon> hoaDonList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);

                            // Lọc hóa đơn theo ngày đặt hàng
                            try {
                                String ngayDatHang = hoaDon.getNgayDatHang();
                                if (ngayDatHang != null && isWithinDateRange(ngayDatHang, startDate, endDate, sdf)) {
                                    hoaDon.setId_hd(dataSnapshot.getKey());
                                    hoaDonList.add(hoaDon);
                                }
                            } catch (Exception e) {
                                Log.e("Firebase", "Lỗi khi xử lý ngày đặt hàng", e);
                            }
                        }

                        ArrayList<HoaDon> list = new ArrayList<>();
                        for (HoaDon don: hoaDonList){
                            if (don.getDisabale()==null){
                                list.add(don);
                            }
                        }
                        hoaDonList.clear();
                        hoaDonList.addAll(list);

                        Collections.sort(hoaDonList, new Comparator<HoaDon>() {
                            @Override
                            public int compare(HoaDon o1, HoaDon o2) {
                                // So sánh ngày tháng trực tiếp vì định dạng yyyy-MM-dd có thể so sánh đúng
                                return o2.getNgayDatHang().compareTo(o1.getNgayDatHang());
                            }
                        });

                        callback.onCallback(hoaDonList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to fetch orders", error.toException());
                    }
                });
    }

    // Kiểm tra xem ngày có nằm trong khoảng từ startDate đến endDate hay không
    private boolean isWithinDateRange(String ngayDatHang, String startDate, String endDate, SimpleDateFormat sdf) {
        try {
            long dateInMillis = sdf.parse(ngayDatHang).getTime();
            long startInMillis = sdf.parse(startDate).getTime();
            long endInMillis = sdf.parse(endDate).getTime();

            return dateInMillis >= startInMillis && dateInMillis <= endInMillis;
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<HoaDon> addDateChangeText(ArrayList<HoaDon> dateList) {
        ArrayList<HoaDon> resultList = new ArrayList<>();

        // Biến để theo dõi ngày trước đó
        String previousDate = null;

        Log.e("zzzzzzzzz", "addDateChangeText: Bat Dau");



        for (HoaDon hoaDon : dateList) {
            // Lấy phần ngày của chuỗi (dùng substring để lấy "yyyy-MM-dd")
            String currentDate = hoaDon.getNgayDatHang().substring(0, 10);  // Lấy "yyyy-MM-dd"

            // Kiểm tra xem ngày có thay đổi không
            if (previousDate != null && !currentDate.equals(previousDate)) {
                // Nếu ngày thay đổi, thêm text "Chuyển ngày" vào
                HoaDon thongBaoChuyen = new HoaDon();
                thongBaoChuyen.setChuyenngay(previousDate);
                resultList.add(thongBaoChuyen);
                resultList.add(hoaDon);
            } else {
                resultList.add(hoaDon);
            }

            // Cập nhật ngày trước đó
            previousDate = currentDate;
        }

        HoaDon thongBaoChuyen1 = new HoaDon();
        thongBaoChuyen1.setChuyenngay(previousDate);
        resultList.add(thongBaoChuyen1);



        return resultList;
    }

    public void addDisable(HoaDon hoaDon) {
        String key = hoaDon.getId_hd();
        if (key != null) {
            database.child("HoaDon").child(key).child("disabale").setValue("TRUE")
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm hóa đơn thành công"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Thêm hóa đơn thất bại", e));
        } else {
            Log.e("Firebase", "Không thể tạo key cho hóa đơn mới");
        }
    }


}
