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

public class RecyclerViewDailyAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewDailyAdapter.ViewHolder> {

    private ArrayList<RecyclerViewItem> mItems;
    private Context mContext;

    public RecyclerViewDailyAdapter(Context context) {
        mContext = context;
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public TextView mTextTime;
        public ImageView mImageStatus;
        public TextView mTextTemMax;
        public TextView mTextTempMin;

        public ViewHolder(View view) {
            super(view);

            mTextTime = view.findViewById(R.id.rv_daily_item_time);
            mImageStatus = view.findViewById(R.id.rv_daily_item_status);
            mTextTemMax = view.findViewById(R.id.rv_daily_item_temperature_max);
            mTextTempMin = view.findViewById(R.id.rv_daily_item_temperature_min);
        }
    }

    public static class RecyclerViewItem {
        private final String mTime;
        private final String mTemperatureMax;
        private final String mTemperatureMin;
        private final String mIconCode;

        public RecyclerViewItem(String time, String tempMax, String tempMin, String iconCode) {
            mTime = time;
            mTemperatureMax = tempMax;
            mTemperatureMin = tempMin;
            mIconCode = iconCode;
        }

        public String getTime() {
            return mTime;
        }

        public String getTemperatureMax() {
            return mTemperatureMax;
        }

        public String getTemperatureMin() {
            return mTemperatureMin;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_daily_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = mItems.get(position);
        String iconUrl = "https://openweathermap.org/img/wn/" + item.getIconCode() + "@2x.png";

        holder.mTextTime.setText(item.getTime());
        holder.mTextTemMax.setText(item.getTemperatureMax());
        holder.mTextTempMin.setText(item.getTemperatureMin());
        Glide.with(mContext).load(iconUrl).into(holder.mImageStatus);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
