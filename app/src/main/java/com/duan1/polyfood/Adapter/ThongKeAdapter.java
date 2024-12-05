package com.duan1.polyfood.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.List;

public class ThongKeAdapter extends RecyclerView.Adapter<ThongKeAdapter.ThongKeViewHolder> {
    private List<HoaDon> hoaDonList;

    public ThongKeAdapter(List<HoaDon> hoaDonList) {
        this.hoaDonList = hoaDonList;
    }

    @NonNull
    @Override
    public ThongKeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thongke_item, parent, false);
        return new ThongKeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKeViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);
        holder.tvNgayHoaDon.setText("" + hoaDon.getNgayDatHang().toString().substring(0,10));
        holder.tvTongTien.setText("" + hoaDon.getTongTien());

        if (position % 2 == 0){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        try {
            return hoaDonList.size();
        }catch (Exception e){

        }
        return 0;
    }

    public static class ThongKeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgayHoaDon, tvTongTien;
        LinearLayout linearLayout;

        public ThongKeViewHolder(View itemView) {
            super(itemView);
            tvNgayHoaDon = itemView.findViewById(R.id.tvNgayHoaDon);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            linearLayout = itemView.findViewById(R.id.liner);
        }
    }

    public void updateData(List<HoaDon> hoaDonList) {
        this.hoaDonList = hoaDonList;
        notifyDataSetChanged();
    }
}

