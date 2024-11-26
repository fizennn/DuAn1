package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.util.List;
import com.bumptech.glide.Glide;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<ThucDon> foodList;
    private StickerDao stickerDao;
    private GradientDrawable drawable = new GradientDrawable();

    public FoodAdapter(Context context, List<ThucDon> foodList) {
        this.context = context;
        this.foodList = foodList;
        stickerDao = new StickerDao();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mon_an_3x6, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        ThucDon food = foodList.get(position);
        holder.tvName.setText(food.getTen());
        holder.tvPrice.setText(food.getDanhGia()+"");
        Glide.with(context)
                .load(food.getHinhAnh()) // Đảm bảo đây là URL hoặc đường dẫn hợp lệ
                .into(holder.imgFood);
//        holder.imgFood.setImageResource();


        holder.tag1.setVisibility(View.INVISIBLE);
        holder.tag2.setVisibility(View.GONE);
        holder.tag3.setVisibility(View.GONE);

        if (food.getSticker1() != null) {
            setSticker(food.getSticker1(), holder.tag1, drawable); // sticker1 với tag1 và drawable chung
        }
        if (food.getSticker2() != null) {
            setSticker(food.getSticker2(), holder.tag2, drawable); // sticker2 với tag2 và drawable riêng
        }
        if (food.getSticker3() != null) {
            setSticker(food.getSticker3(), holder.tag3, drawable); // sticker3 với tag3 và drawable khác
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFood;
        TextView tvName, tvPrice, tvLabel,tag1,tag2,tag3;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
        }
    }

    private void setSticker(String stickerId, TextView tagView, GradientDrawable drawable) {
        stickerDao.getStickerById(stickerId, new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(Sticker sticker) {
                if (sticker != null) {
                    // Hiển thị tag
                    tagView.setVisibility(View.VISIBLE);

                    // Tạo drawable mới để tránh chia sẻ
                    GradientDrawable tagDrawable = new GradientDrawable();
                    tagDrawable.setShape(GradientDrawable.RECTANGLE);
                    tagDrawable.setCornerRadius(5);
                    String stickerColor = sticker.getColor() != null ? sticker.getColor() : "#FFFFFF"; // Màu mặc định
                    tagDrawable.setStroke(2, Color.parseColor(stickerColor)); // Đặt viền sticker

                    // Cập nhật nội dung và màu chữ
                    tagView.setText(" " + sticker.getContent() + " ");
                    tagView.setTextColor(Color.parseColor(stickerColor)); // Đặt màu chữ
                    tagView.setBackground(tagDrawable); // Đặt background drawable cho tag
                }
            }

            @Override
            public void onSuccess(List<Sticker> stickerList) {
                // Nếu bạn cần xử lý danh sách stickers, có thể thêm logic ở đây
            }

            @Override
            public void onFailure(String error) {
                Log.d("StickerError", "onFailure: " + error);
            }
        });
    }
}

