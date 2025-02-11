package com.mattiuzzi.fontana.cristhian.socialnetworkv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.adapter.FeedAdapter;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.model.FeedViewModel;

public class FeedActivity extends AppCompatActivity {

    FeedViewModel feedViewModel;
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.rvFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedAdapter = new FeedAdapter();
        recyclerView.setAdapter(feedAdapter);

        feedViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(FeedViewModel.class);

        feedViewModel.pageLv.observe(this, pagingData -> {
            feedAdapter.submitData(getLifecycle(), pagingData);

            // Verifica se o RecyclerView tem itens
            feedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (itemCount > 0) {
                        TextView tvInfo = findViewById(R.id.tvTextFeedInfo);
                        tvInfo.setText("");
                    }
                }
            });
        });

        Button btnRegister = findViewById(R.id.btnUserUpdate);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, UpdateRegisterActivity.class);

                startActivity(i);
            }
        });

        Button btnNewPost = findViewById(R.id.btnNewPost);
        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, NewPostActivity.class);

                startActivity(i);
            }
        });

        TextView tvInfo = findViewById(R.id.tvTextFeedInfo);
        tvInfo.setText("Buscando Feed ...");
    }
}