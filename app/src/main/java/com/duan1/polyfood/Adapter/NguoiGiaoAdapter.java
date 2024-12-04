package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.ChiTietHoaDonActivity;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.DeliveryActivity;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class NguoiGiaoAdapter extends RecyclerView.Adapter<NguoiGiaoAdapter.ViewHolder>{

    private Context context;
    private ArrayList<HoaDon> hoaDonList;
    private HoaDonDAO hoaDonDAO;
    private ThongBaoDao thongBaoDao;

    public NguoiGiaoAdapter(Context context, ArrayList<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.hoaDonDAO = new HoaDonDAO();
        thongBaoDao = new ThongBaoDao();
    }

    @NonNull
    @Override
    public NguoiGiaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new NguoiGiaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        holder.txtGia.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn
        holder.txtSoLuong.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn
        holder.txtPhuongThucThanhToan.setVisibility(View.VISIBLE);  // Đảm bảo không bị ẩn

        holder.cv1.setVisibility(View.GONE);
        holder.cv2.setVisibility(View.VISIBLE);


        if ("Chờ giao".equals(hoaDon.getTrangThai())) {
            holder.btnXacNhanGiaoHang.setVisibility(View.VISIBLE);
            holder.btnXacNhanGiaoHang.setOnClickListener(v -> {
                hoaDonDAO.getHoaDonByStatus("Đang giao", new HoaDonDAO.FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<HoaDon> hoaDonList1) {
                        try {
                            if (hoaDonList1.size()!=0){
                                Toast.makeText(context, "Hãy hoàn thành đơn hàng bạn đã nhận trước!", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                if (!hoaDonList.isEmpty()){
                                    hoaDonList.remove(position);
                                }


                                hoaDon.setTrangThai("Đang giao");
                                hoaDonDAO.updateHoaDon(hoaDon);

                                // Thông báo cho fragment khác
                                if (context instanceof DeliveryActivity) {
                                    ((DeliveryActivity) context).updateDonHangDangGiao(hoaDon);
                                }


                                notifyItemRemoved(position);

                                ThongBao thongBao = new ThongBao();

                                thongBao.setId_hd(hoaDon.getId_hd());
                                thongBao.setId_nn(hoaDon.getId_nd());
                                thongBao.setNoidung("Đơn hàng "+hoaDon.getTenMonAn()+" (sl:"+hoaDon.getSoLuong()+") của bạn đã được xác nhận giao hàng !");
                                thongBao.setRole("Tài Xế");
                                thongBao.setTrangThai(hoaDon.getTrangThai());

                                thongBaoDao.guiThongBao(thongBao,context);

                                notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            Toast.makeText(context, "Hãy hoàn thành đơn hàng bạn đã nhận trước!", Toast.LENGTH_SHORT).show();
                        }





                    }

                    @Override
                    public void onCallback(HoaDon hoaDon) {

                    }
                });

                
            });
        } else {
            holder.btnXacNhanGiaoHang.setVisibility(View.GONE);
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

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan, txtTrangThai,txtTenMonAn1,txtTongTien1,txtTrangThai1;
        ImageView imgMonAn;
        Button btnXacNhanGiaoHang;
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
            btnXacNhanGiaoHang = itemView.findViewById(R.id.btnXacNhanGiaoHang);
            cv1 = itemView.findViewById(R.id.cv1);
            cv2 = itemView.findViewById(R.id.cv2);

            txtTenMonAn1 = itemView.findViewById(R.id.txtTenMonAn1);
            txtTongTien1 = itemView.findViewById(R.id.txtTongTien1);
            txtTrangThai1 = itemView.findViewById(R.id.txtTrangThai1);

        }
    }
}
