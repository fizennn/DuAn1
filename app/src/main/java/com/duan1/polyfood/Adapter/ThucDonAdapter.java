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
        View view = LayoutInflater.from(context).inflate(R.layout.item_thuc_don, parent, false);
        return new ThucDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThucDonViewHolder holder, int position) {
        ThucDon thucDon = thucDonList.get(position);
        holder.tvTen.setText(thucDon.getTen());
        holder.tvGia.setText(String.valueOf(thucDon.getGia()));
        holder.tvMoTa.setText(thucDon.getMoTa());
        Glide.with(context).load(thucDon.getHinhAnh()).into(holder.imgThucDon);
    }

    @Override
    public int getItemCount() {
        return thucDonList.size();
    }

    public static class ThucDonViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGia, tvMoTa;
        ImageView imgThucDon;

        public ThucDonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            imgThucDon = itemView.findViewById(R.id.ivHinhAnh);
        }
    }
}
