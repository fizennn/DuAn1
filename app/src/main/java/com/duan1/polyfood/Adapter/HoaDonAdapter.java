package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder>{
    private Context context;
    private ArrayList<HoaDon> hoaDonList;

    public HoaDonAdapter(Context context, ArrayList<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);
        holder.txtTenMonAn.setText(hoaDon.getTenMonAn());
        holder.txtGia.setText("Giá: " +formatToVND(hoaDon.getGia()));
        holder.txtSoLuong.setText("Số lương: x" + hoaDon.getSoLuong());
        holder.txtTongTien.setText("Tổng tiền: " + formatToVND(hoaDon.getTongTien()));
        holder.txtPhuongThucThanhToan.setText("Phương thức thanh toán: " + hoaDon.getPhuongThucThanhToan());

        Glide.with(context)
                .load(hoaDon.getHinhAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                .into(holder.imgMonAn);
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan;
        ImageView imgMonAn;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenMonAn = itemView.findViewById(R.id.txtTenMonAn);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            txtPhuongThucThanhToan = itemView.findViewById(R.id.txtPhuongThucThanhToan);
        }
    }
}
