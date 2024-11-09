package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.util.Log;
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
import java.util.List;

public class ThucDonAdapter extends RecyclerView.Adapter<ThucDonAdapter.ThucDonViewHolder>{
    private List<ThucDon> thucDonList;
    private Context context;

    public ThucDonAdapter(Context context, List<ThucDon> thucDonList) {
        this.context = context;
        this.thucDonList = thucDonList;
    }

    @NonNull
    @Override
    public ThucDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mon_an_2x6, parent, false);
        return new ThucDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThucDonViewHolder holder, int position) {
        ThucDon thucDon = thucDonList.get(position);
        holder.tvTen.setText(thucDon.getTen());
        holder.tvGia.setText(String.valueOf(thucDon.getGia()));
        holder.tvDanhGia.setText(thucDon.getMoTa());
        // Check if the image URI is null

        Glide.with(context)
                .load(thucDon.getHinhAnh())
                .placeholder(R.drawable.hide_icon) // This image appears if hinhAnh is null
                .into(holder.imgThucDon);

    }

    @Override
    public int getItemCount() {
        return thucDonList.size();
    }

    public static class ThucDonViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGia, tvDanhGia;
        ImageView imgThucDon;

        public ThucDonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvName);
            tvGia = itemView.findViewById(R.id.tvPrice);
            tvDanhGia = itemView.findViewById(R.id.tvFeedback);
            imgThucDon = itemView.findViewById(R.id.ivHinhAnh);
        }
    }
}
