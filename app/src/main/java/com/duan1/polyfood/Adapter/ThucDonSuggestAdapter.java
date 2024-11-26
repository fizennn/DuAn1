package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.MonAnActivity;
import com.duan1.polyfood.Other.IntToVND;
import com.duan1.polyfood.R;

import java.util.List;

public class ThucDonSuggestAdapter extends RecyclerView.Adapter<ThucDonSuggestAdapter.ViewHolder> {

    private List<ThucDon> danhSachThucDon;
    private IntToVND vnd;
    private Context context;
    private ThucDonDAO thucDonDAO;

    public ThucDonSuggestAdapter(List<ThucDon> danhSachThucDon,Context context) {
        this.danhSachThucDon = danhSachThucDon;
        this.context = context;
        vnd = new IntToVND();
        thucDonDAO = new ThucDonDAO();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_an_2x6sug, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThucDon ThucDon = danhSachThucDon.get(position);
        holder.tenTextView.setText(ThucDon.getTen());
        holder.txvGia.setText(vnd.convertToVND(ThucDon.getGia()));


        if (ThucDon.getGoiY()!=null){
            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    thucDonDAO.updateGoiY(ThucDon.getId_td(),"1");
                }else{
                    thucDonDAO.updateGoiY(ThucDon.getId_td(),null);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return danhSachThucDon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenTextView,txvGia;
        CheckBox checkBox;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            txvGia = itemView.findViewById(R.id.txvgia);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }


}

