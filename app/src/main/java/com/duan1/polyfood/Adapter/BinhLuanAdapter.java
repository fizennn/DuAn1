package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.BinhLuan;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.R;

import org.w3c.dom.Comment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.BinhLuanViewHolder> {
    private Context context;
    private List<BinhLuan> binhLuanList;
    private NguoiDungDAO nguoiDungDAO;

    public BinhLuanAdapter(Context context, List<BinhLuan> binhLuanList) {
        this.context = context;
        this.binhLuanList = binhLuanList;
        nguoiDungDAO = new NguoiDungDAO();



        Collections.sort(binhLuanList, new Comparator<BinhLuan>() {
            @Override
            public int compare(BinhLuan c1, BinhLuan c2) {
                // Sắp xếp theo ngày bình luận (mới nhất trước)
                return c2.getNgay().compareTo(c1.getNgay());
            }
        });
    }

    @NonNull
    @Override
    public BinhLuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.binhluan, parent, false);
        return new BinhLuanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuanViewHolder holder, int position) {
        BinhLuan binhLuan = binhLuanList.get(position);
        holder.txvNameComment.setText(binhLuan.getTen());
        holder.txvDateComment.setText((binhLuan.getNgay()).split(" ")[0]);
        holder.txvComment.setText(binhLuan.getBl());

        // Hiển thị hình ảnh đại diện và hình ảnh bình luận nếu có
        // Glide hoặc Picasso có thể được sử dụng để tải ảnh từ URL


        // Hiển thị sao (rating) dựa trên giá trị sao
        int sao = binhLuan.getSao();
        for (int i = 0; i < 5; i++) {
            holder.stars[i].setImageResource(i < sao ? R.drawable.star50 : R.drawable.star_empty);
        }





        holder.txvNameComment.setText(binhLuan.getTen());

        nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {
                Glide.with(context)
                        .load(nguoiDung.getimgUrl())
                        .placeholder(R.drawable.load)
                        .error(R.drawable.load)
                        .into(holder.imgProfileComment);
            }
        });




        if (binhLuan.getAnh()==null){
            holder.linearLayout.setVisibility(View.GONE);
            return;
        }

        if (context != null) {
            Glide.with(context)
                    .load(binhLuan.getAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.imgAnhComment);
        }


    }

    @Override
    public int getItemCount() {
        return binhLuanList.size();
    }

    public static class BinhLuanViewHolder extends RecyclerView.ViewHolder {
        TextView txvNameComment, txvDateComment, txvComment;
        ImageView imgProfileComment, imgAnhComment;
        ImageView[] stars = new ImageView[5];
        LinearLayout linearLayout;

        public BinhLuanViewHolder(@NonNull View itemView) {
            super(itemView);
            txvNameComment = itemView.findViewById(R.id.txvNameComment);
            txvDateComment = itemView.findViewById(R.id.txvDateComment);
            txvComment = itemView.findViewById(R.id.txvComment);
            imgProfileComment = itemView.findViewById(R.id.imgProfileComment);
            imgAnhComment = itemView.findViewById(R.id.imgAnhComment);
            stars[0] = itemView.findViewById(R.id.saorate1);
            stars[1] = itemView.findViewById(R.id.saorate2);
            stars[2] = itemView.findViewById(R.id.saorate3);
            stars[3] = itemView.findViewById(R.id.saorate4);
            stars[4] = itemView.findViewById(R.id.saorate5);
            linearLayout = itemView.findViewById(R.id.linerBinhLuan);
        }
    }
}
