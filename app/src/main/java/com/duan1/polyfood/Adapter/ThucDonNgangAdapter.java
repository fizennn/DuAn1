package com.duan1.polyfood.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.MonAnActivity;
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.R;

import java.util.List;

public class ThucDonNgangAdapter extends RecyclerView.Adapter<ThucDonNgangAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private Sticker sticker;
    private StickerDao stickerDao;
    private GradientDrawable drawable = new GradientDrawable();
    private GradientDrawable drawable1 = new GradientDrawable();
    private GradientDrawable drawable2 = new GradientDrawable();



    public ThucDonNgangAdapter(List<ThucDon> danhSachThucDon,Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
        vnd = new IntToVND();
        stickerDao = new StickerDao();
        drawable.setShape(GradientDrawable.RECTANGLE); // Hình chữ nhật
        drawable.setCornerRadius(5);
        drawable.setColor(context.getResources().getColor(android.R.color.white)); // Màu nền (tuỳ chỉnh)

        drawable1 = drawable;
        drawable2 = drawable;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_an_2x6, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThucDon ThucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(ThucDon.getTen());
        holder.soSaoTextView.setText(String.valueOf(ThucDon.getDanhGia()+""));
        holder.txvGia.setText(vnd.convertToVND(ThucDon.getGia()));

        if (context != null) {
            Glide.with(context)
                    .load(ThucDon.getHinhAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.imageView);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MonAnActivity.class);
                intent.putExtra("UID", ThucDon.getId_td()+"");
                context.startActivity(intent);

            }
        });





        holder.tag1.setVisibility(View.INVISIBLE);
        holder.tag2.setVisibility(View.GONE);
        holder.tag3.setVisibility(View.GONE);

        if (ThucDon.getSticker1() != null) {
            setSticker(ThucDon.getSticker1(), holder.tag1, drawable); // sticker1 với tag1 và drawable chung
        }
        if (ThucDon.getSticker2() != null) {
            setSticker(ThucDon.getSticker2(), holder.tag2, drawable); // sticker2 với tag2 và drawable riêng
        }
        if (ThucDon.getSticker3() != null) {
            setSticker(ThucDon.getSticker3(), holder.tag3, drawable); // sticker3 với tag3 và drawable khác
        }


        // Áp dụng drawable làm background







    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView,txvGia,tag1,tag2,tag3;
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.tvPrice);
            txvGia = itemView.findViewById(R.id.txvgia);
            imageView = itemView.findViewById(R.id.imgFood);
            layout = itemView.findViewById(R.id.linearLayoutChitiet);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
        }
    }
    public void updateList(List<ThucDon> newThucDonList) {
        this.danhSachThucDon.clear();
        this.danhSachThucDon.addAll(newThucDonList);
        notifyDataSetChanged();
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

