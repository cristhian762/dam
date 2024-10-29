package com.mattiuzzi.fontana.cristhian.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mattiuzzi.fontana.cristhian.lista.R;
import com.mattiuzzi.fontana.cristhian.lista.adapter.MyAdapter;
import com.mattiuzzi.fontana.cristhian.lista.model.MyItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Identificador da tela de retorno
    static int NEW_ITEM_REQUEST = 1;
    List<MyItem> itens = null;
    MyAdapter myAdapter;

    // onSaveInstanceState metodo chamado toda vez que o sistema precisa salvar o estado
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("item_list", new ArrayList<>(itens));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Restaurar o estado
        if (savedInstanceState != null) {
            // Recupera a lista de itens do estado salvo
            itens = savedInstanceState.getParcelableArrayList("item_list");
        } else {
            itens = new ArrayList<>();
        }

        FloatingActionButton fabAddItem = findViewById(R.id.fabAddNewItem);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewItemActivity.class);
                // startActivityForResult assume que terá um retorno de dados da activity chamada
                startActivityForResult(i, NEW_ITEM_REQUEST);
            }
        });

        RecyclerView rvItens = findViewById(R.id.rvItens);

        myAdapter = new MyAdapter(this,itens);
        rvItens.setAdapter(myAdapter);

        rvItens.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItens.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(), DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar se é o retorno esperado
        if(requestCode == NEW_ITEM_REQUEST) {

            // Verifica o status do resultado
            if(resultCode == Activity.RESULT_OK) {
                MyItem myItem = new MyItem(
                        data.getData(),
                        data.getStringExtra("title"),
                        data.getStringExtra("description")
                );

                itens.add(myItem);

                myAdapter.notifyItemInserted(itens.size() - 1);
            }
        }
    }
}