package com.drone.thesis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drone.thesis.R;
import com.drone.thesis.models.Drones;
import com.github.MakMoinee.library.interfaces.DefaultEventListener;

import java.util.List;

public class DroneAdapter extends RecyclerView.Adapter<DroneAdapter.ViewHolder> {

    Context mContext;
    List<Drones> dronesList;

    DefaultEventListener listener;

    public DroneAdapter(Context mContext, List<Drones> dronesList, DefaultEventListener listener) {
        this.mContext = mContext;
        this.dronesList = dronesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DroneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_drone, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DroneAdapter.ViewHolder holder, int position) {
        Drones drones = dronesList.get(position);
        if (drones != null) {
            holder.txtDroneName.setText(drones.getDroneName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dronesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDroneName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDroneName = itemView.findViewById(R.id.txtDroneName);
        }
    }
}
