package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class Top3Adapter extends RecyclerView.Adapter<Top3Adapter.Top3ViewHolder>{
    private Context context;
    private ArrayList<ThucDon> thucDonList;

    // Constructor
    public Top3Adapter(Context context, ArrayList<ThucDon> thucDonList) {
        this.context = context;
        this.thucDonList = thucDonList;
    }

    @NonNull
    @Override
    public Top3ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_top3, parent, false);
        return new Top3ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Top3ViewHolder holder, int position) {
        // Get item data
        ThucDon thucDon = thucDonList.get(position);
        holder.tenTextView.setText("Tên món ăn: " + thucDon.getTen());
        holder.danhGiaTextView.setText("Đánh giá: " + thucDon.getDanhGia());

        Glide.with(context)
                .load(thucDon.getHinhAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return thucDonList.size();
    }

    // ViewHolder class to hold the views
    public static class Top3ViewHolder extends RecyclerView.ViewHolder {

        TextView tenTextView, danhGiaTextView;
        ImageView imageView;

        public Top3ViewHolder(View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tenTextView);
            danhGiaTextView = itemView.findViewById(R.id.danhGiaTextView);
            imageView = itemView.findViewById(R.id.hinhAnhImageView);
        }
    }
}
