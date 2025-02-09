package com.example.produtos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.produtos.activity.HomeActivity;
import com.example.produtos.model.Product;
import com.example.produtos.R;
import com.example.produtos.util.Config;
import com.example.produtos.util.ImageCache;

import java.net.URL;

public class MyAdapter extends PagingDataAdapter<Product, MyViewHolder> {

    HomeActivity homeActivity;

    public MyAdapter(HomeActivity homeActivity, @NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
        this.homeActivity = homeActivity;
    }

    /**
     * Cria os elementos de UI referente a um item da lista
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    /**
     * Preenche um item da lista
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = this.getItem(position);

        // preenche o campo de nome
        TextView tvNameList = holder.itemView.findViewById(R.id.tvNameList);
        tvNameList.setText(product.name);

        // preenche o campo de preço
        TextView tvPiceList = holder.itemView.findViewById(R.id.tvPriceList);
        tvPiceList.setText(product.price);

        // preenche o campo de foto
        int w = (int) homeActivity.getResources().getDimension(R.dimen.thumb_width);
        int h = (int) homeActivity.getResources().getDimension(R.dimen.thumb_height);
        ImageView imvProductThumb = holder.itemView.findViewById(R.id.imvProductThumb);
        // somente agora o a imagem é obtida do servidor. Caso a imagem já esteja salva no cache da app,
        // não baixamos ela de novo
        ImageCache.loadImageUrlToImageView(homeActivity, product.img, imvProductThumb, w, h);

        // ao clicar em um item da lista, navegamos para a tela que mostra os detalhes do produto
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.startViewProductAcitivity(product.id);
            }
        });
    }
}
