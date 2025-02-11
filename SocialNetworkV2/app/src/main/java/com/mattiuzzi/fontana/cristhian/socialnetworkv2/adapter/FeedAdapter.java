package com.mattiuzzi.fontana.cristhian.socialnetworkv2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.data.PostData;

public class FeedAdapter extends PagingDataAdapter<PostData, MyViewHolder> {
    public FeedAdapter() {
        super(DIFF_CALLBACK);
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
        PostData postData = getItem(position);

        if (postData == null) {
            return;
        }

        TextView tvName = holder.itemView.findViewById(R.id.tvPostUserAuthorName);
        tvName.setText(postData.getAuthorName());

        TextView tvLogin = holder.itemView.findViewById(R.id.tvPostUserAuthorLogin);
        tvLogin.setText(postData.getAuthorLogin());

        TextView tvDate = holder.itemView.findViewById(R.id.tvPostDate);
        tvDate.setText(postData.getDate());

        TextView tvText = holder.itemView.findViewById(R.id.tvPostDescription);
        tvText.setText(postData.getText());

        ImageView ivPostItem = holder.itemView.findViewById(R.id.ivPostItem);
        Glide.with(holder.itemView.getContext())
                .load(postData.getImage())
                .into(ivPostItem);

        ImageView ivPostUserAuthor = holder.itemView.findViewById(R.id.ivPostUserAuthor);
        Glide.with(holder.itemView.getContext())
                .load(postData.getAuthorImage())
                .into(ivPostUserAuthor);

    }

    private static final DiffUtil.ItemCallback<PostData> DIFF_CALLBACK = new DiffUtil.ItemCallback<PostData>() {
        @Override
        public boolean areItemsTheSame(@NonNull PostData oldItem, @NonNull PostData newItem) {
            return oldItem.getText().equals(newItem.getText());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PostData oldItem, @NonNull PostData newItem) {
            return oldItem.getText().equals(newItem.getText());
        }
    };

}
