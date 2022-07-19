package com.moreira.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.moreira.weather.R;

import java.util.ArrayList;

public class RecyclerViewHourlyAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewHourlyAdapter.ViewHolder> {

    private ArrayList<RecyclerViewItem> mItems;
    private Context mContext;

    public RecyclerViewHourlyAdapter(Context context) {
        mContext = context;
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public TextView mTextTime;
        public ImageView mImageStatus;
        public TextView mTextTemperature;

        public ViewHolder(View view) {
            super(view);

            mTextTime = view.findViewById(R.id.rv_hourly_item_time);
            mImageStatus = view.findViewById(R.id.rv_hourly_item_status);
            mTextTemperature = view.findViewById(R.id.rv_hourly_item_temperature);
        }
    }

    public static class RecyclerViewItem {
        private final String mTime;
        private final String mTemperature;
        private final String mIconCode;

        public RecyclerViewItem(String time, String temperature, String iconCode) {
            mTime = time;
            mTemperature = temperature;
            this.mIconCode = iconCode;
        }

        public String getTime() {
            return mTime;
        }

        public String getTemperature() {
            return mTemperature;
        }

        public String getIconCode() {
            return mIconCode;
        }
    }

    public void updateItems(ArrayList<RecyclerViewItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hourly_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = mItems.get(position);
        String iconUrl = "https://openweathermap.org/img/wn/" + item.getIconCode() + "@2x.png";

        holder.mTextTime.setText(item.getTime());
        holder.mTextTemperature.setText(item.getTemperature());
        Glide.with(mContext).load(iconUrl).into(holder.mImageStatus);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
