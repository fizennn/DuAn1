package com.duan1.polyfood.Adapter;

import android.content.Context;
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
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.R;

import java.util.List;

public class DishSuggestAdapter extends RecyclerView.Adapter<DishSuggestAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private String TAG = "zzzzzzzzzzzzzz";
    private ThucDonDAO thucDonDAO;

    public DishSuggestAdapter(List<ThucDon> danhSachThucDon, Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
        this.thucDonDAO = new ThucDonDAO();
        vnd = new IntToVND();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_an_2x6, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThucDon thucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(thucDon.getTen());
        holder.soSaoTextView.setText(String.valueOf(thucDon.getDanhGia()+""));
        holder.txvGia.setText(vnd.convertToVND(thucDon.getGia()));

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
            thucDonDAO.addSuggestedDishToFirebase(thucDon, context);  // Gọi hàm thêm món ăn vào gợi ý
            Toast.makeText(context, "Món ăn đã được thêm vào gợi ý!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView,txvGia;
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.tvPrice);
            txvGia = itemView.findViewById(R.id.txvgia);
            imageView = itemView.findViewById(R.id.imgFood);
            layout = itemView.findViewById(R.id.linearLayoutChitiet);
        }
    }
    public void updateDishImage(String dishId, String newImageUrl) {
        for (int i = 0; i < danhSachThucDon.size(); i++) {
            ThucDon dish = danhSachThucDon.get(i);
            if (dish.getId_td().equals(dishId)) {
                dish.setHinhAnh(newImageUrl);
                notifyItemChanged(i);
                break;
            }
        }
    }
}
