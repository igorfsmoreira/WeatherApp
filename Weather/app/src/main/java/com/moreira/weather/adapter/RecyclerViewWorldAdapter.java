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
import java.util.Comparator;

public class RecyclerViewWorldAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewWorldAdapter.ViewHolder> {
    private Context mContext;
    private OnItemInteractionListener mListener;
    private ArrayList<RecyclerViewItem> mItems;

    public RecyclerViewWorldAdapter(Context context, OnItemInteractionListener listener) {
        mContext = context;
        mListener = listener;
    }

    public interface OnItemInteractionListener {
        void onItemClick(RecyclerViewItem item);
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public TextView mTextLocal;
        public TextView mTextStatus;
        public TextView mTextTemperature;
        public ImageView mImageStatus;

        public ViewHolder(View view) {
            super(view);

            mTextLocal = view.findViewById(R.id.rv_world_item_local);
            mTextStatus = view.findViewById(R.id.rv_world_item_description);
            mTextTemperature = view.findViewById(R.id.rv_world_item_temperature);
            mImageStatus = view.findViewById(R.id.rv_world_item_status);
        }
    }

    public static class RecyclerViewItem {
        private final String mLocal;
        private final String mTemperature;
        private final String mStatus;
        private final double mLatitude;
        private final double mLongitude;
        private final String mIconCode;

        public RecyclerViewItem(String local, String temperature, String status, double latitude, double longitude, String iconCode) {
            mLocal = local;
            mTemperature = temperature;
            mStatus = status;
            mLatitude = latitude;
            mLongitude = longitude;
            mIconCode = iconCode;
        }

        public String getLocal() {
            return mLocal;
        }

        public String getTemperature() {
            return mTemperature;
        }

        public String getStatus() {
            return mStatus;
        }

        public double getLatitude() {
            return mLatitude;
        }

        public double getLongitude() {
            return mLongitude;
        }

        public String getIconCode() {
            return mIconCode;
        }
    }

    public void updateItems(ArrayList<RecyclerViewItem> items) {
        // Sort items by local
        items.sort(Comparator.comparing(RecyclerViewWorldAdapter.RecyclerViewItem::getLocal));
        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_world_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = mItems.get(position);
        String iconUrl = "https://openweathermap.org/img/wn/" + item.getIconCode() + "@4x.png";

        holder.mTextLocal.setText(item.getLocal());
        holder.mTextStatus.setText(item.getStatus());
        holder.mTextTemperature.setText(item.getTemperature());
        holder.itemView.setOnClickListener(view -> mListener.onItemClick(item));
        Glide.with(mContext).load(iconUrl).into(holder.mImageStatus);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
