package com.duan1.polyfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.util.List;
import com.bumptech.glide.Glide;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<ThucDon> foodList;

    public FoodAdapter(Context context, List<ThucDon> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mon_an_3x6, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        ThucDon food = foodList.get(position);
        holder.tvName.setText(food.getTen());
        holder.tvPrice.setText(food.getDanhGia()+"");
        Glide.with(context)
                .load(food.getHinhAnh()) // Đảm bảo đây là URL hoặc đường dẫn hợp lệ
                .into(holder.imgFood);
//        holder.imgFood.setImageResource();
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFood;
        TextView tvName, tvPrice, tvLabel;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLabel = itemView.findViewById(R.id.tvLabel);
        }
    }
}

