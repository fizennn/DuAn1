package com.duan1.polyfood.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Models.DonHang;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.NhaHang;
import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    private DatabaseReference database;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    // ======= CRUD cho NhaHang =======

    public void getAllNhaHang(ArrayList<NhaHang> nhaHangList, Runnable onComplete) {
        database.child("NhaHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                nhaHangList.add(nhaHang);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                for (int i = 0; i < nhaHangList.size(); i++) {
                    if (nhaHangList.get(i).getId_nh().equals(nhaHang.getId_nh())) {
                        nhaHangList.set(i, nhaHang);  // Cập nhật nhà hàng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NhaHang nhaHang = snapshot.getValue(NhaHang.class);
                nhaHangList.removeIf(nh -> nh.getId_nh().equals(nhaHang.getId_nh()));  // Xóa nhà hàng khỏi danh sách
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

    public void addNhaHang(NhaHang nhaHang) {
        String key = database.child("NhaHang").push().getKey();
        nhaHang.setId_nh(key);
        database.child("NhaHang").child(key).setValue(nhaHang);
    }

    public void updateNhaHang(NhaHang nhaHang) {
        database.child("NhaHang").child(nhaHang.getId_nh()).setValue(nhaHang);
    }

    public void deleteNhaHang(String id) {
        database.child("NhaHang").child(id).removeValue();
    }




    // ======= CRUD cho NguoiDung =======

    public void getAllNguoiDung(ArrayList<NguoiDung> nguoiDungList, Runnable onComplete) {
        database.child("NguoiDung").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                nguoiDungList.add(nguoiDung);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                for (int i = 0; i < nguoiDungList.size(); i++) {
                    if (nguoiDungList.get(i).getId_nd().equals(nguoiDung.getId_nd())) {
                        nguoiDungList.set(i, nguoiDung);  // Cập nhật người dùng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                nguoiDungList.removeIf(nd -> nd.getId_nd().equals(nguoiDung.getId_nd()));  // Xóa người dùng khỏi danh sách
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

    public void addNguoiDung(NguoiDung nguoiDung) {
        String key = database.child("NguoiDung").push().getKey();
        nguoiDung.setId_nd(key);
        database.child("NguoiDung").child(key).setValue(nguoiDung);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        database.child("NguoiDung").child(nguoiDung.getId_nd()).setValue(nguoiDung);
    }

    public void deleteNguoiDung(String id) {
        database.child("NguoiDung").child(id).removeValue();
    }




    // ======= CRUD cho ThucDon =======

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
        database.child("ThucDon").child(key).setValue(thucDon);
    }

    public void updateThucDon(ThucDon thucDon) {
        database.child("ThucDon").child(thucDon.getId_td()).setValue(thucDon);
    }

    public void deleteThucDon(String id) {
        database.child("ThucDon").child(id).removeValue();
    }




    // ======= CRUD cho DonHang =======

    public void getAllDonHang(ArrayList<DonHang> donHangList, Runnable onComplete) {
        database.child("DonHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                donHangList.add(donHang);
                onComplete.run();  // Cập nhật giao diện khi có dữ liệu mới
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                for (int i = 0; i < donHangList.size(); i++) {
                    if (donHangList.get(i).getId_dh().equals(donHang.getId_dh())) {
                        donHangList.set(i, donHang);  // Cập nhật đơn hàng trong danh sách
                        break;
                    }
                }
                onComplete.run();  // Cập nhật giao diện khi dữ liệu được sửa
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                donHangList.removeIf(d -> d.getId_dh().equals(donHang.getId_dh()));  // Xóa đơn hàng khỏi danh sách
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

    public void addDonHang(DonHang donHang) {
//        tao tu dong id khi them
//        String key = database.child("DonHang").push().getKey();
//        donHang.setId_dh(key);
//        database.child("DonHang").child(key).setValue(donHang);

        String idDh = donHang.getId_dh(); // Lấy id_dh từ đối tượng
        database.child("DonHang").child(idDh).setValue(donHang); // Sử dụng id_dh từ donHang
    }

    public void updateDonHang(DonHang donHang) {
        database.child("DonHang").child(donHang.getId_dh()).setValue(donHang);
    }

    public void deleteDonHang(String id) {
        database.child("DonHang").child(id).removeValue();
    }




    // ======= CRUD cho HoaDon =======

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
        database.child("HoaDon").child(key).setValue(hoaDon);
    }

    public void updateHoaDon(HoaDon hoaDon) {
        database.child("HoaDon").child(hoaDon.getId_tt()).setValue(hoaDon);
    }

    public void deleteHoaDon(String id) {
        database.child("HoaDon").child(id).removeValue();
    }
}
