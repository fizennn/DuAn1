package com.duan1.polyfood.Adapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Fragment.ChoGiaoFragment;
import com.duan1.polyfood.Fragment.ChoXuLyFragment;
import com.duan1.polyfood.Fragment.DangGiaoFragment;
import com.duan1.polyfood.Fragment.HoanThanhFragment;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HoaDon> hoaDonList;
    private HoaDonDAO hoaDonDAO;

    public HoaDonAdapter(Context context, ArrayList<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.hoaDonDAO = new HoaDonDAO();
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
        holder.txtTrangThai.setText("Trạng thái: " + hoaDon.getTrangThai());

        Glide.with(context)
                .load(hoaDon.getHinhAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                .into(holder.imgMonAn);

        // Thiết lập hiển thị theo trạng thái
        if ("Đang giao".equals(hoaDon.getTrangThai())) {
            holder.btnDaNhanHang.setVisibility(View.VISIBLE); // Hiện nút "Đã nhận được hàng"
            holder.btnDaNhanHang.setOnClickListener(v -> {
                // Cập nhật trạng thái đơn hàng thành "Hoàn thành"
                hoaDon.setTrangThai("Hoàn thành");
                hoaDonDAO.updateHoaDon(hoaDon);

                // Cập nhật danh sách hiển thị và giao diện
                hoaDonList.remove(position);
                notifyItemRemoved(position);

                notifyDataSetChanged();
            });
        } else {
            holder.btnDaNhanHang.setVisibility(View.GONE); // Ẩn nút nếu không ở trạng thái "Đang giao"
        }

    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan, txtTrangThai;
        ImageView imgMonAn;
        Button btnDaNhanHang, btnXacNhanXuLy;

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
        }
    }

}