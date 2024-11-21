package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.MonAnActivity;
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavouriteDishAdapter extends RecyclerView.Adapter<FavouriteDishAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private String TAG = "zzzzzzzzzzzzzz";
    private ThucDonDAO thucDonDAO;

    public FavouriteDishAdapter(List<ThucDon> danhSachThucDon, Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
        this.thucDonDAO = new ThucDonDAO();
        vnd = new IntToVND();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_an2x6favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThucDon thucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(thucDon.getTen());
        holder.soSaoTextView.setText(String.valueOf(thucDon.getDanhGia()+""));
        holder.txvGia.setText(vnd.convertToVND(thucDon.getGia()));
        holder.iconDelete.setOnClickListener(view -> {
            deleteFoodItem(position);
        });
        if (context != null) {
            Glide.with(context)
                    .load(thucDon.getHinhAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.imageView);
        }

        // Load hình ảnh món ăn
        Glide.with(context)
                .load(thucDon.getHinhAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                .into(holder.imageView);

        // Thêm sự kiện click vào item để thêm món ăn vào gợi ý
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MonAnActivity.class);
            intent.putExtra("UID", thucDon.getId_td()+"");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }
    public void deleteFoodItem(int position) {
        // Lấy món ăn cần xóa
        ThucDon itemToDelete = danhSachThucDon.get(position);

        // Xóa món ăn khỏi danh sách local (RecyclerView)
        danhSachThucDon.remove(position);

        // Cập nhật RecyclerView
        notifyItemRemoved(position);

        // Nếu bạn đang sử dụng Firebase, xóa món ăn từ Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy ID người dùng hiện tại

        // Đường dẫn đến Firebase nơi món ăn yêu thích được lưu trữ
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NhaHang")
                .child("FavouriteDish")
                .child(userId); // Chuyển đến danh sách món ăn yêu thích của người dùng

        // Xóa món ăn khỏi danh sách yêu thích của người dùng
        databaseReference.child(itemToDelete.getId_td())  // Lấy ID của món ăn cần xóa
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Thông báo xóa thành công từ Firebase
                        Toast.makeText(context, "Món ăn đã được xóa", Toast.LENGTH_SHORT).show();
                    } else {
                        // Thông báo lỗi
                        Toast.makeText(context, "Không thể xóa món ăn", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView,txvGia;
        ImageView imageView;
        LinearLayout layout;
        ImageView iconDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.tvPrice);
            txvGia = itemView.findViewById(R.id.txvgia);
            imageView = itemView.findViewById(R.id.imgFood);
            layout = itemView.findViewById(R.id.linearLayoutChitiet);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }

}
