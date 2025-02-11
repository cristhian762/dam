package com.mattiuzzi.fontana.cristhian.socialnetworkv2.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.model.NewPostViewModel;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NewPostActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 2;

    NewPostViewModel newPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        newPostViewModel = new NewPostViewModel(getApplication());

        Button btnNewPostImage = findViewById(R.id.btnNewPostImage);

        btnNewPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_TAKE_PICTURE);
            }
        });

        Button btnNewPostBack = findViewById(R.id.btnNewPostBack);
        btnNewPostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewPostActivity.this, FeedActivity.class);

                startActivity(i);
            }
        });

        Button btnNewPostAdd = findViewById(R.id.btnNewPostAdd);
        btnNewPostAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPhotoPath = newPostViewModel.getSelectPhotoLocation();
                if(currentPhotoPath.isEmpty()) {
                    Toast.makeText(NewPostActivity.this, "O campo Foto não foi preenchido", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                try {
                    int h = (int) getResources().getDimension(R.dimen.img_height);
                    Util.scaleImage(currentPhotoPath, -1, 2*h);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                EditText editNewPostText =  findViewById(R.id.editNewPostText);
                final String text = editNewPostText.getText().toString();
                if(text.isEmpty()) {
                    Toast.makeText(NewPostActivity.this, "Campo Texto não foi preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                LiveData<JSONObject> resultLD = newPostViewModel.setPost(currentPhotoPath, text);

                resultLD.observe(NewPostActivity.this, new Observer<JSONObject>() {
                    @Override
                    public void onChanged(JSONObject object) {
                        if(object != null) {
                            try {
                                int success = object.getInt("success");

                                if (success == 1) {
                                    Toast.makeText(NewPostActivity.this, "Post Criado com sucesso", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(NewPostActivity.this, FeedActivity.class);

                                    startActivity(i);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            Toast.makeText(NewPostActivity.this, "Erro ao criar a postagem. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_TAKE_PICTURE) {
            if(resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    try {
                        String filePath = saveImageToInternalStorage(selectedImageUri);
                        newPostViewModel.setSelectPhotoLocation(filePath);

                        ImageView ivNewPost = findViewById(R.id.ivNewPost);
                        Bitmap bitmap = Util.getBitmap(this, selectedImageUri);
                        ivNewPost.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            throw new IOException("Falha ao abrir a imagem");
        }

        String fileName = "image_" + System.currentTimeMillis() + ".jpg";

        File directory = getFilesDir();
        File file = new File(directory, fileName);

        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file.getAbsolutePath();
    }
}