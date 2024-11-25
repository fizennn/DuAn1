package com.duan1.polyfood.Adapter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(Sticker sticker);
        void onDelete(Sticker sticker);

    }

    public StickerAdapter(List<Sticker> stickerList, OnItemClickListener listener) {
        this.stickerList = stickerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Sticker sticker = stickerList.get(position);
        holder.tvContent.setText(sticker.getContent());

        holder.btnEditLiner.setBackgroundColor(Color.parseColor(sticker.getColor())); // Đổi màu nền thành màu cam


        holder.btnEditLiner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                listener.onEdit(sticker);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView btnEdit, btnDelete;
        LinearLayout btnEditLiner;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEditLiner = itemView.findViewById(R.id.Liner);
        }
    }
}
