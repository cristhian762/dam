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

public class ListAdapter extends PagingDataAdapter<ImageData, MyViewHolder> {
    public ListAdapter(@NonNull DiffUtil.ItemCallback<ImageData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageData imageData = getItem(position);
        if (imageData == null) {
            return;
        }

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.getFileName());

        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(imageData.getDate()));

        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + imageData.getSize());

        ImageView imageView = holder.itemView.findViewById(R.id.imThumb);
        imageView.setImageBitmap(imageData.getThumb());
    }
}
