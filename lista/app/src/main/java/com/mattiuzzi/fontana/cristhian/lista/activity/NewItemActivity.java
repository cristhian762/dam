package com.mattiuzzi.fontana.cristhian.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mattiuzzi.fontana.cristhian.lista.R;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST = 1;
    Uri photoSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Restaurar o estado
        if (savedInstanceState != null) {
            String imageUriString = savedInstanceState.getString("imageUri");

            if (imageUriString != null) {
                photoSelected = Uri.parse(imageUriString);
                ImageView imvfotoPreview = findViewById(R.id.imvPhotoPreview);

                // Modifica o elemento de imagem
                imvfotoPreview.setImageURI(photoSelected);
            }
        }

        ImageButton imgCI = findViewById(R.id.imbCl);

        imgCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declara o intent para abrir um arquivo
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Define o tipo de arquivo que está buscando, no caso imagens
                photoPickerIntent.setType("image/*");

                // Delega ao sistema a abertura da janela do sistema para seleção da imagem
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validação da imagem
                if (photoSelected == null) {
                    Toast.makeText(NewItemActivity.this, "É necessário selecionar uma imagem!", Toast.LENGTH_LONG).show();

                    return;
                }

                EditText etTitle = findViewById(R.id.etTitle);
                String title = etTitle.getText().toString();

                // Validação do titulo
                if (title.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir um título", Toast.LENGTH_LONG).show();

                    return;
                }

                EditText etDesc = findViewById(R.id.etDesc);
                String desc = etDesc.getText().toString();

                // Validação da descrição
                if (desc.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir uma descrição", Toast.LENGTH_LONG).show();

                    return;
                }

                Intent i = new Intent();

                i.setData(photoSelected);
                i.putExtra("title", title);
                i.putExtra("description", desc);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        NewItemActivity.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                // Cria uma Uri a partir do arquivo
                photoSelected = data.getData();

                ImageView imvfotoPreview = findViewById(R.id.imvPhotoPreview);

                // Modifica o elemento de imagem
                imvfotoPreview.setImageURI(photoSelected);
            }
        }
    }
}