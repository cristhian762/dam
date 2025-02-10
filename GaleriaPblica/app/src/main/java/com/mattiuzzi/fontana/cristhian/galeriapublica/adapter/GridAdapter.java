package com.mattiuzzi.fontana.cristhian.galeriapublica.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.mattiuzzi.fontana.cristhian.galeriapublica.R;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageData;

import java.text.SimpleDateFormat;

public class GridAdapter extends PagingDataAdapter<ImageData, MyViewHolder> {
    public GridAdapter(@NonNull DiffUtil.ItemCallback<ImageData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageData imageData = getItem(position);

        if (imageData == null) {
            return;
        }

        ImageView imageView = holder.itemView.findViewById(R.id.imThumb);
        imageView.setImageBitmap(imageData.getThumb());
    }
}
