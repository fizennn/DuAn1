package com.duan1.polyfood.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.ChiTietHoaDonActivity;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.R;

import java.util.List;




public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {
    private List<ThongBao> thongBaoList;
    private NguoiDungDAO nguoiDungDAO;
    private Context context;
    private ThongBaoDao thongBaoDao;
    public ThongBaoAdapter(List<ThongBao> thongBaoList,Context context) {
        this.thongBaoList = thongBaoList;
        nguoiDungDAO = new NguoiDungDAO();
        this.context = context;
        thongBaoDao = new ThongBaoDao();
    }

    public interface onClickBill{
        void onClickBill(String id);
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thongbao_item, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);
        if (thongBao == null) {
            return;
        }

        holder.tvNoiDung.setText(thongBao.getNoidung());
        holder.tvNgayThang.setText(thongBao.getNgayThang());
        nguoiDungDAO.getAllNguoiDungByID(thongBao.getId_ng(), new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {
                if (thongBao.getRole()==null){
                    holder.tvTenNG.setText(nguoiDung.getHoTen());
                    return;
                }
                holder.tvTenNG.setText(nguoiDung.getHoTen()+"("+thongBao.getRole()+")");
                if (!((Activity) context).isFinishing()) {
                    Glide.with(context)
                            .load(nguoiDung.getimgUrl())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .into(holder.ivAvtNG);
                }
            }
        });

        if (thongBao.getRead()==null){
            holder.linerNoti.setBackgroundColor(Color.parseColor("#9FFFEFC1"));
        }

        holder.linerNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thongBaoDao.setReaded(thongBao);

                Intent intent = new Intent(context, ChiTietHoaDonActivity.class);
                intent.putExtra("id_hd", thongBao.getId_hd());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return thongBaoList != null ? thongBaoList.size() : 0;
    }

    public static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNoiDung, tvNgayThang, tvTenNG;
        private ImageView ivAvtNG;
        private LinearLayout linerNoti;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvNgayThang = itemView.findViewById(R.id.tvNgayThang);
            tvTenNG = itemView.findViewById(R.id.tvNameNG);
            ivAvtNG = itemView.findViewById(R.id.ivAvtNG);
            linerNoti = itemView.findViewById(R.id.linerNoti);
        }
    }
}

