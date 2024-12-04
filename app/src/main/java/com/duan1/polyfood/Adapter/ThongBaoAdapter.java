package com.duan1.polyfood.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duan1.polyfood.ChiTietHoaDonActivity;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {
    private List<ThongBao> thongBaoList;
    private NguoiDungDAO nguoiDungDAO;
    private Context context;
    private ThongBaoDao thongBaoDao;
    private onLoad onLoad;



    public ThongBaoAdapter(List<ThongBao> thongBaoList,Context context,onLoad onLoad) {
        this.thongBaoList = thongBaoList;
        nguoiDungDAO = new NguoiDungDAO();
        this.context = context;
        thongBaoDao = new ThongBaoDao();
        this.onLoad = onLoad;
    }

    public interface onLoad{
        void onLoad(int i);
    }


    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thongbao_item, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        int i = position;
        ThongBao thongBao = thongBaoList.get(position);
        if (thongBao == null) {
            return;
        }


        holder.linearChuyenNgay.setVisibility(View.GONE);
        if (thongBao.getChuyenNgay()==null){

            holder.ivPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v,thongBao,i);
                }
            });


            holder.tvNoiDung.setText(thongBao.getNoidung());
            holder.tvNgayThang.setText(thongBao.getNgayThang());

            if (thongBao.getId_ng()==null){
                return;
            }
            nguoiDungDAO.getAllNguoiDungByID(thongBao.getId_ng(), new NguoiDungDAO.FirebaseCallback() {
                @Override
                public void onCallback(NguoiDung nguoiDung) {
                    if (thongBao.getRole()==null){
                        holder.tvTenNG.setText(nguoiDung.getHoTen());
                        onLoad.onLoad(-1);
                        return;
                    }
                    holder.tvTenNG.setText(nguoiDung.getHoTen()+"("+thongBao.getRole()+")");
                    if (!((Activity) context).isFinishing()) {
                        Glide.with(context)
                                .load(nguoiDung.getimgUrl())
                                .placeholder(R.drawable.load)
                                .error(R.drawable.load)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        onLoad.onLoad(-1);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        onLoad.onLoad(i);
                                        return false;
                                    }
                                })
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
                    thongBao.setRead("READ");
                    notifyItemChanged(i);

                    Intent intent = new Intent(context, ChiTietHoaDonActivity.class);
                    intent.putExtra("id_hd", thongBao.getId_hd());
                    context.startActivity(intent);
                }
            });
        }else {
            holder.linerNoti.setVisibility(View.GONE);
            holder.linearChuyenNgay.setVisibility(View.VISIBLE);
            if (getCurrentDateTime().equalsIgnoreCase(thongBao.getChuyenNgay())){
                holder.tvChuyenNgay.setText("Hôm nay");
                return;
            }
            if (getCurrentDateTimeQua().equalsIgnoreCase(thongBao.getChuyenNgay())){
                holder.tvChuyenNgay.setText("Hôm qua");
                return;
            }
            holder.tvChuyenNgay.setText(thongBao.getChuyenNgay());
        }


    }

    @Override
    public int getItemCount() {
        return thongBaoList != null ? thongBaoList.size() : 0;
    }

    public static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNoiDung, tvNgayThang, tvTenNG,tvChuyenNgay;
        private ImageView ivAvtNG,ivPopupMenu;
        private LinearLayout linerNoti,linearChuyenNgay;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvNgayThang = itemView.findViewById(R.id.tvNgayThang);
            tvTenNG = itemView.findViewById(R.id.tvNameNG);
            ivAvtNG = itemView.findViewById(R.id.ivAvtNG);
            linerNoti = itemView.findViewById(R.id.linerNoti);
            linearChuyenNgay = itemView.findViewById(R.id.linerChuyenNgay);
            tvChuyenNgay = itemView.findViewById(R.id.tvChuyenNgay);
            ivPopupMenu = itemView.findViewById(R.id.ivPopupMenu);
        }
    }

    public static String getCurrentDateTime() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();


        // Định dạng thời gian (ví dụ: dd/MM/yyyy HH:mm:ss)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Trả về chuỗi định dạng ngày giờ
        return dateFormat.format(calendar.getTime());
    }

    public static String getCurrentDateTimeQua() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);

        // Định dạng thời gian (ví dụ: dd/MM/yyyy HH:mm:ss)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Trả về chuỗi định dạng ngày giờ
        return dateFormat.format(calendar.getTime());
    }

    private void showPopup(View view,ThongBao thongBao,int i) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pop_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.btnMask){
                if (thongBaoList.get(i).getRead()==null){
                    thongBaoList.get(i).setRead("read");
                    notifyItemChanged(i);
                }




                thongBaoDao.setReaded(thongBao);
            }
            if (item.getItemId()==R.id.btnXoa){
                thongBaoList.remove(i);
                notifyItemRemoved(i);
                thongBaoDao.setReaded(thongBao);
                thongBaoDao.setGone(thongBao);
            }


            return false;
        });
        popup.show();
    }
}

