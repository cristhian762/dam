package com.example.produtos.adapter;

import androidx.annotation.NonNull;
import androidx.paging.DifferCallback;
import androidx.recyclerview.widget.DiffUtil;

import com.example.produtos.model.Product;

public class ProductComparator extends DiffUtil.ItemCallback<Product> {
    @Override
    public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
        return oldItem.id.equals(newItem.id);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
        return oldItem.id.equals(newItem.id) &&
                oldItem.name.equals(newItem.name) &&
                oldItem.price.equals(newItem.price);
    }
}
