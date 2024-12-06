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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.R;

import java.util.List;

public class StickerTimKiemAdapter extends RecyclerView.Adapter<StickerTimKiemAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;
    private OnItemClickListener listener;
    private Context context;
    private StickerDao stickerDao;

    public interface OnItemClickListener {
        void onEdit(Sticker sticker);
        void onDelete(Sticker sticker);
        void onClick(Sticker  sticker,String check);

    }

    public StickerTimKiemAdapter(List<Sticker> stickerList, Context context, OnItemClickListener listener) {
        this.stickerList = stickerList;
        this.listener = listener;
        this.context=context;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker_timkiem, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        int  i = position;
        Sticker sticker = stickerList.get(position);
        holder.tvContent.setText(" "+sticker.getContent()+" ");



        if (sticker.getSelected()!=null){
            if (sticker.getSelected().equalsIgnoreCase("SELE")){
                holder.img.setVisibility(View.VISIBLE);
                holder.liner.setBackgroundResource(R.drawable.sticker_timkiem1);
                holder.tvContent.setTextColor(Color.parseColor("#FE724C"));
                listener.onClick(sticker,"");
            }
        }



        holder.liner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sticker.getSelected()==null){
                    holder.img.setVisibility(View.VISIBLE);
                    holder.liner.setBackgroundResource(R.drawable.sticker_timkiem1);
                    holder.tvContent.setTextColor(Color.parseColor("#FE724C"));
                    sticker.setSelected("SELE");
                    listener.onClick(sticker,"");
                }else {
                    holder.img.setVisibility(View.GONE);
                    holder.liner.setBackgroundResource(R.drawable.sticker_timkiem);
                    holder.tvContent.setTextColor(Color.parseColor("#A89C9C"));
                    sticker.setSelected(null);
                    listener.onClick(sticker,null);

                }


            }
        });




    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        LinearLayout liner;
        ImageView img;


        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            img = itemView.findViewById(R.id.ivDele);
            liner = itemView.findViewById(R.id.liner);
        }
    }
}
