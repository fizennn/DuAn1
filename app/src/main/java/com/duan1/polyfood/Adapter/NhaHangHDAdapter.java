package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class NhaHangHDAdapter extends RecyclerView.Adapter<NhaHangHDAdapter.ViewHolder>{

    private Context context;
    private ArrayList<HoaDon> hoaDonList;
    private HoaDonDAO hoaDonDAO;
    private ThongBaoDao thongBaoDao;

    public NhaHangHDAdapter(Context context, ArrayList<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.hoaDonDAO = new HoaDonDAO();
        thongBaoDao = new ThongBaoDao();
    }
    @NonNull
    @Override
    public NhaHangHDAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new NhaHangHDAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhaHangHDAdapter.ViewHolder holder, int position) {

        holder.cv1.setVisibility(View.GONE);
        holder.cv2.setVisibility(View.VISIBLE);

        HoaDon hoaDon = hoaDonList.get(position);
        holder.txtTenMonAn.setText(hoaDon.getTenMonAn());
        holder.txtGia.setText("Giá: " +formatToVND(hoaDon.getGia()));
        holder.txtSoLuong.setText("Số lương: x" + hoaDon.getSoLuong());
        holder.txtTongTien.setText("Tổng tiền: " + formatToVND(hoaDon.getTongTien()));
        holder.txtPhuongThucThanhToan.setText("Phương thức thanh toán: " + hoaDon.getPhuongThucThanhToan());
        holder.txtTrangThai.setText("Trạng thái: " + hoaDon.getTrangThai());

        Glide.with(context)
                .load(hoaDon.getHinhAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                .into(holder.imgMonAn);

        holder.txtGia.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn
        holder.txtSoLuong.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn
        holder.txtPhuongThucThanhToan.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn
        holder.imgMonAn.setVisibility(View.GONE);



        if ("Chờ xử lý".equals(hoaDon.getTrangThai())) {
            holder.btnXacNhanXuLy.setVisibility(View.VISIBLE);
            holder.btnXacNhanXuLy.setOnClickListener(v -> {

                hoaDon.setTrangThai("Chờ giao");
                hoaDonDAO.updateHoaDon(hoaDon);

                hoaDonList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

                ThongBao thongBao = new ThongBao();

                thongBao.setId_hd(hoaDon.getId_hd());
                thongBao.setId_nn(hoaDon.getId_nd());
                thongBao.setNoidung("Đơn hàng "+hoaDon.getTenMonAn()+" (sl:"+hoaDon.getSoLuong()+") của bạn đã được xác nhận xử lý !");
                thongBao.setRole("Nhà Hàng");
                thongBao.setTrangThai(hoaDon.getTrangThai());

                thongBaoDao.guiThongBao(thongBao,context);
            });
        } else {
            holder.btnXacNhanXuLy.setVisibility(View.GONE);
        }
    }


    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan, txtTrangThai;
        ImageView imgMonAn;
        Button btnDaNhanHang, btnXacNhanXuLy;
        CardView cv1,cv2;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenMonAn = itemView.findViewById(R.id.txtTenMonAn);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            txtPhuongThucThanhToan = itemView.findViewById(R.id.txtPhuongThucThanhToan);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            btnDaNhanHang = itemView.findViewById(R.id.btnDaNhanHang);
            btnXacNhanXuLy = itemView.findViewById(R.id.btnXacNhanXuLy);
            cv2 = itemView.findViewById(R.id.cv2);
            cv1 = itemView.findViewById(R.id.cv1);
        }
    }
}
