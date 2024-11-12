package com.duan1.polyfood.Database;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duan1.polyfood.Fragment.DishSuggestFragment;
import com.duan1.polyfood.Models.NhaHang;
import com.duan1.polyfood.Models.ThucDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThucDonDAO {
    private DatabaseReference database;
    private StorageReference storageReference;


    public ThucDonDAO() {
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("ThucDonImages");
    }

    public interface FirebaseCallback {
        void onCallback(ArrayList<ThucDon> thucDonList);
        void onCallback(ThucDon thucDon);
    }

    public void getAllThucDon(FirebaseCallback callback) {
        database.child("NhaHang").child("ThucDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ThucDon> thucDonList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        ThucDon thucDon = data.getValue(ThucDon.class);
                        thucDonList.add(thucDon);
                    } catch (DatabaseException e) {
                        Log.e("Firebase", "Failed to convert data: " + e.getMessage());
                    }
                }
                if (thucDonList.isEmpty()) {
                    Log.d("Firebase", "Data snapshot is empty");
                } else {
                    Log.d("Firebase", "Retrieved data: " + thucDonList.toString());
                }
                callback.onCallback(thucDonList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }


    public void addThucDon(ThucDon thucDon, Uri imageUri) {

        if (thucDon.getDanhGia() == null || thucDon.getDanhGia().isEmpty()) {
            thucDon.setDanhGia("0"); // Giá trị mặc định cho đánh giá
        }
        if (thucDon.getPhanHoi() == null || thucDon.getPhanHoi().isEmpty()) {
            thucDon.setPhanHoi("0"); // Giá trị mặc định cho phản hồi
        }

        String key = database.child("NhaHang").child("ThucDon").push().getKey();
        if (key != null) {
            thucDon.setId_td(key);
            StorageReference imgRef = storageReference.child(thucDon.getTen() + ".jpg");

            imgRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                thucDon.setHinhAnh(uri.toString());
                                database.child("NhaHang").child("ThucDon").child(key).setValue(thucDon);
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to get download URL: " + e.getMessage())))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to upload image: " + e.getMessage()));
        }
    }


    public void updateThucDon(ThucDon thucDon) {
        database.child("NhaHang").child("ThucDon").child(thucDon.getId_td()).setValue(thucDon);
    }

    public void deleteThucDon(String id) {
        database.child("NhaHang").child("ThucDon").child(id).removeValue();
    }


    public void getAThucDon(FirebaseCallback callback,String UID) {
        database.child("NhaHang").child("ThucDon").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ThucDon thucDon = snapshot.getValue(ThucDon.class);
                callback.onCallback(thucDon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addSuggestedDishToFirebase(ThucDon thucDon, Context context) {
        // Tham chiếu đến nút "SuggestedDishes" trên Firebase
        DatabaseReference suggestedRef = FirebaseDatabase.getInstance().getReference("NhaHang").child("ThucDonSuggestions");

        // Kiểm tra xem món ăn đã tồn tại chưa
        suggestedRef.orderByChild("ten").equalTo(thucDon.getTen()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu món ăn đã tồn tại
                    Toast.makeText(context, "Món ăn này đã có trong gợi ý", Toast.LENGTH_SHORT).show();  // Sử dụng requireContext() thay vì getContext()
                } else {
                    // Nếu món ăn chưa tồn tại, tạo key mới và thêm món ăn vào Firebase
                    String id = suggestedRef.push().getKey();  // Tạo một key duy nhất cho món ăn

                    if (id != null) {
                        // Tạo đối tượng chỉ chứa các trường cần thiết
                        ThucDon dishToUpload = new ThucDon(thucDon.getTen(), thucDon.getDanhGia(), thucDon.getHinhAnh(), thucDon.getGia());
                        // Lưu vào Firebase
                        Log.d("AddSuggestedDish", "Giá món ăn: " + thucDon.getGia());

                        suggestedRef.child(id).setValue(dishToUpload)
                                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Món ăn đã được thêm vào gợi ý"))
                                .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi thêm món ăn vào gợi ý", e));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Lỗi khi kiểm tra món ăn", databaseError.toException());
            }
        });
    }




    public void getSuggestedDishes(FirebaseCallback callback) {
        FirebaseDatabase.getInstance().getReference("NhaHang").child("ThucDonSuggestions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<ThucDon> suggestedDishes = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            try {
                                // Firebase tự động ánh xạ các trường trong dữ liệu vào đối tượng ThucDon
                                ThucDon thucDon = data.getValue(ThucDon.class);
                                suggestedDishes.add(thucDon);
                            } catch (DatabaseException e) {
                                Log.e("Firebase", "Failed to convert data: " + e.getMessage());
                            }
                        }
                        callback.onCallback(suggestedDishes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Database error: " + error.getMessage());
                    }
                });
    }




    public void searchThucDonByName(String name, FirebaseCallback callback) {
        database.child("NhaHang").child("ThucDon")
                .orderByChild("ten")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<ThucDon> searchResults = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            ThucDon thucDon = data.getValue(ThucDon.class);
                            if (thucDon != null && thucDon.getTen() != null) {
                                // Kiểm tra nếu tên món ăn chứa từ khóa
                                if (thucDon.getTen().toLowerCase().contains(name.toLowerCase())) {
                                    searchResults.add(thucDon);
                                }
                            }
                        }
                        callback.onCallback(searchResults);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Database error: " + error.getMessage());
                    }
                });
    }

}
