package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.PayActivity;
import com.duan1.polyfood.R;

import java.util.List;

public class CartIteamAdapter extends RecyclerView.Adapter<CartIteamAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private String TAG = "zzzzzzzzzzzzzz";

    public CartIteamAdapter(List<ThucDon> danhSachThucDon, Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
        vnd = new IntToVND();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_an_2x6_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThucDon ThucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(ThucDon.getTen());
        holder.soSaoTextView.setText(String.valueOf(ThucDon.getSoLuong() + " Suất"));
        holder.txvGia.setText(vnd.convertToVND(ThucDon.getGia() * ThucDon.getSoLuong()));

        if (context != null) {
            Glide.with(context)
                    .load(ThucDon.getHinhAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.imageView);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("UID", ThucDon.getId_td());
                intent.putExtra("SO_LUONG", ThucDon.getSoLuong());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView, soSaoTextView, txvGia;
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            soSaoTextView = itemView.findViewById(R.id.txvSoluong);
            txvGia = itemView.findViewById(R.id.txvgia);
            imageView = itemView.findViewById(R.id.imgFood);
            layout = itemView.findViewById(R.id.linearLayoutChitiet);
        }
    }
}