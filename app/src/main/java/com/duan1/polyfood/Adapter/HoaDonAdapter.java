package com.duan1.polyfood.Adapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.ChiTietHoaDonActivity;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HoaDon> hoaDonList;
    private HoaDonDAO hoaDonDAO;
    private ThongBaoDao thongBaoDao;

    public HoaDonAdapter(Context context, ArrayList<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.hoaDonDAO = new HoaDonDAO();
        thongBaoDao = new ThongBaoDao();
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
        holder.txtTrangThai1.setText("Trạng thái: " + hoaDon.getTrangThai());
        holder.txtTenMonAn1.setText(hoaDon.getTenMonAn());
        holder.txtTongTien1.setText("Tổng tiền: " + formatToVND(hoaDon.getTongTien()));


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

                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                ThongBao thongBao = new ThongBao();

                thongBao.setId_hd(hoaDon.getId_hd());
                thongBao.setId_nn(hoaDon.getId_nd());
                thongBao.setNoidung("Đơn hàng "+hoaDon.getTenMonAn()+" (sl:"+hoaDon.getSoLuong()+") của bạn đã hoàn thành ! !");
                thongBao.setRole("Tài Xế");
                thongBao.setTrangThai(hoaDon.getTrangThai());

                thongBaoDao.guiThongBao(thongBao,context);

                notifyDataSetChanged();

            });
        } else {
            holder.btnDaNhanHang.setVisibility(View.GONE); // Ẩn nút nếu không ở trạng thái "Đang giao"
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietHoaDonActivity.class);
                intent.putExtra("id_hd", hoaDon.getId_hd());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn,txtTenMonAn1, txtGia, txtSoLuong, txtTongTien,txtTongTien1, txtPhuongThucThanhToan, txtTrangThai,txtTrangThai1;
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
            btnDaNhanHang = itemView.findViewById(R.id.btnDaNhanDcHang);
            btnXacNhanXuLy = itemView.findViewById(R.id.btnXacNhanXuLy);

            txtTenMonAn1 = itemView.findViewById(R.id.txtTenMonAn1);
            txtTongTien1 = itemView.findViewById(R.id.txtTongTien1);
            txtTrangThai1 = itemView.findViewById(R.id.txtTrangThai1);

        }
    }

}
