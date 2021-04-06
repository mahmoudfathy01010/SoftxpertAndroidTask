package com.elbaitdesign.softxpertandroidtask.ui;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elbaitdesign.softxpertandroidtask.R;
import com.elbaitdesign.softxpertandroidtask.model.Car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    Context context;
    ArrayList<Car> data;
    public CarAdapter(Context context, ArrayList<Car> data) {
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarAdapter.ViewHolder viewHolder = new CarAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_car, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = data.get(position);

        if(car.getImageUrl()==null){
            Picasso.get()
                    .load(R.drawable.image_error)
                    .fit().centerCrop()
                    .into(holder.image);
        }
        else {
            Picasso.get()
                    .load(car.getImageUrl())
                    .fit().centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .error(R.drawable.image_error)
                    .into(holder.image);
        }


        holder.brand.setText(car.getBrand());
        holder.constructionYear.setText(String.format(context.getString(R.string.construction_year),car.getBrand()));
        if(car.getUsed()){
            holder.status.setText(context.getString(R.string.is_used));
            holder.status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else {
            holder.status.setText(context.getString(R.string.is_new));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView brand;
        TextView constructionYear;
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.car_image);
            brand = itemView.findViewById(R.id.brand);
            constructionYear = itemView.findViewById(R.id.construction_year);
            status = itemView.findViewById(R.id.status);

        }
    }
}
