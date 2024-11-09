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
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.R;

import java.util.List;

public class ThucDonNgangAdapter extends RecyclerView.Adapter<ThucDonNgangAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private String TAG = "zzzzzzzzzzzzzz";

    public ThucDonNgangAdapter(List<ThucDon> danhSachThucDon,Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
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
        ThucDon ThucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(ThucDon.getTen());
        holder.soSaoTextView.setText(String.valueOf(ThucDon.getDanhGia()+""));
        holder.txvGia.setText(vnd.convertToVND(ThucDon.getGia()));
        Log.d(TAG, "onBindViewHolder: URL Image Load "+ThucDon.getHinhAnh());
        if (context != null) {
            Glide.with(context)
                    .load(ThucDon.getHinhAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView,txvGia;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.tvPrice);
            txvGia = itemView.findViewById(R.id.txvgia);
            imageView = itemView.findViewById(R.id.imgFood);
        }
    }


}

