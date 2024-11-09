package com.duan1.polyfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.util.List;

public class ThucDonNgangAdapter extends RecyclerView.Adapter<ThucDonNgangAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;

    public ThucDonNgangAdapter(List<ThucDon> danhSachThucDon) {
        this.danhSachThucDon = danhSachThucDon;
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
    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.tvPrice);
        }
    }
}

