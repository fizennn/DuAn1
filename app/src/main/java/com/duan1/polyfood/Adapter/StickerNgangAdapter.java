package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.R;

import java.util.List;

public class StickerNgangAdapter extends RecyclerView.Adapter<StickerNgangAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onEdit(Sticker sticker);
        void onDelete(Sticker sticker);

    }

    public StickerNgangAdapter(List<Sticker> stickerList, Context context, OnItemClickListener listener) {
        this.stickerList = stickerList;
        this.listener = listener;
        this.context=context;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker_ngang, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Sticker sticker = stickerList.get(position);
        holder.tvContent.setText(sticker.getContent());

        holder.tag.setColorFilter(Color.parseColor(sticker.getColor()), PorterDuff.Mode.SRC_IN);

        holder.btnEditLiner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                listener.onEdit(sticker);
                return true;
            }
        });


        if (context!=null && sticker.getImageUri()!=null) {
            Glide.with(context)
                    .load(sticker.getImageUri())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(holder.sticker);
        }

    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView btnEdit, btnDelete;
        LinearLayout btnEditLiner;
        ImageView tag,sticker;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEditLiner = itemView.findViewById(R.id.Liner);
            tag = itemView.findViewById(R.id.tag);
            sticker = itemView.findViewById(R.id.imgSticker);
        }
    }
}
