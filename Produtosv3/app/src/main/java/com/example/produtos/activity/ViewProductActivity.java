package com.example.produtos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.produtos.R;
import com.example.produtos.util.ImageCache;
import com.example.produtos.model.Product;
import com.example.produtos.model.ViewProductViewModel;

/**
 * Esta tela mostra os detalhes de um produto em específico. Um produto possui uma foto, um nome,
 * preço, descrição, nome do usuário que o cadastrou e a data em que foi cadastrado.
 *
 * Os detalhes do produto são obtidos junto ao servidor web.
 */
public class ViewProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        // Para obter os detalhes do produto, a app envia o id do produto ao servidor web. Este
        // último responde com os detalhes do produto referente ao pid.

        // O pid do produto é enviado para esta tela quando o produto é clicado na tela de Home.
        // Aqui nós obtemos o pid.
        Intent i = getIntent();
        String pid = i.getStringExtra("pid");

        // obtemos o ViewModel pois é nele que está o método que se conecta ao servior web.
        ViewProductViewModel viewProductViewModel = new ViewModelProvider(this).get(ViewProductViewModel.class);

        // O ViewModel possui o método getProductDetailsLD, que obtém os detalhes de um produto em
        // específico do servidor web.
        //
        // O método getProductDetailsLD retorna um LiveData, que na prática é um container que avisa
        // quando o resultado do servidor chegou. Ele guarda os detalhes de um produto que o servidor
        // entregou para a app.
        LiveData<Product> product = viewProductViewModel.getProductDetailsLD(pid);

        // Aqui nós observamos o LiveData. Quando o servidor responder, o resultado contendo uma produto
        // será guardado dentro do LiveData. Neste momento o
        // LiveData avisa que o produto chegou chamando o método onChanged abaixo.
        product.observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {

                // product contém os detalhes do produto que foram entregues pelo servidor web
                if(product != null) {

                    // aqui nós obtemos a imagem do produto. A imagem não vem logo de cara. Primeiro
                    // obtemos os detalhes do produto. Uma vez recebidos os campos de id, nome, preço,
                    // descrição, criado por, usamos o id para obter a imagem do produto em separado.
                    // A classe ImageCache obtém a imagem de um produto específico, guarda em um cache
                    // o seta em um ImageView fornecido.
                    ImageView imvProductPhoto = findViewById(R.id.imvProductPhoto);
                    int imgHeight = (int) ViewProductActivity.this.getResources().getDimension(R.dimen.img_height);
                    ImageCache.loadImageUrlToImageView(ViewProductActivity.this, product.img, imvProductPhoto, -1, imgHeight);

                    // Abaixo nós obtemos os dados do produto e setamos na interfa de usuário
                    TextView tvName = findViewById(R.id.tvName);
                    tvName.setText(product.name);

                    TextView tvPrice = findViewById(R.id.tvPrice);
                    tvPrice.setText(product.price);

                    TextView tvDescription = findViewById(R.id.tvDescription);
                    tvDescription.setText(product.description);

                    TextView tvCreatedBy = findViewById(R.id.tvCreatedBy);
                    tvCreatedBy.setText("Criado por " + product.createdBy + " em " + product.createdAt);
                }
                else {
                    Toast.makeText(ViewProductActivity.this, "Não foi possível obter os detalhes do produto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}