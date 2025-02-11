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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.model.UpdateRegisterViewModel;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Config;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class UpdateRegisterActivity extends AppCompatActivity {

    UpdateRegisterViewModel updateRegisterViewModel;
    static int RESULT_TAKE_PICTURE = 1;
    String photoSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateRegisterViewModel = new ViewModelProvider(this).get(UpdateRegisterViewModel.class);

        String selectPhotoLocation = updateRegisterViewModel.getSelectPhotoLocation();

        if (!selectPhotoLocation.isEmpty()) {
            photoSelected = selectPhotoLocation;
            ImageView imvfotoPreview = findViewById(R.id.imgUser);
            Bitmap bitmap = Util.getBitmap(photoSelected, imvfotoPreview.getWidth(), imvfotoPreview.getHeight());
            imvfotoPreview.setImageBitmap(bitmap);
        }

        Button btnSetImage = findViewById(R.id.btnSetImage);

        btnSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_TAKE_PICTURE);
            }
        });

        Button btnNewRegister = findViewById(R.id.btnUpRegister);
        btnNewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPhotoPath = updateRegisterViewModel.getSelectPhotoLocation();
                if(currentPhotoPath.isEmpty()) {
                    Toast.makeText(UpdateRegisterActivity.this, "O campo Foto não foi preenchido", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText editEmail =  findViewById(R.id.editEmail);
                final String newLogin = editEmail.getText().toString();
                if(newLogin.isEmpty()) {
                    Toast.makeText(UpdateRegisterActivity.this, "Campo Email não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etNewPassword =  findViewById(R.id.editPasswordRegister);
                final String newPassword = etNewPassword.getText().toString();

                EditText editName =  findViewById(R.id.editName);
                final String name = editName.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(UpdateRegisterActivity.this, "Campo Nome Completo não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText editDateOfBirth =  findViewById(R.id.editDateOfBirth);
                final String dateOfBirth = editDateOfBirth.getText().toString();
                if(dateOfBirth.isEmpty()) {
                    Toast.makeText(UpdateRegisterActivity.this, "Campo Data de Nascimento não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                Pattern DATE_PATTERN = Pattern.compile(
                        "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"
                );

                if(!DATE_PATTERN.matcher(dateOfBirth).matches()) {
                    Toast.makeText(UpdateRegisterActivity.this, "Formato inválido para Data de Nascimento", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText editCity =  findViewById(R.id.editCity);
                final String city = editCity.getText().toString();
                if(city.isEmpty()) {
                    Toast.makeText(UpdateRegisterActivity.this, "Campo Cidade não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    int h = (int) getResources().getDimension(R.dimen.img_height);
                    Util.scaleImage(currentPhotoPath, -1, 2*h);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                v.setEnabled(false);

                LiveData<Boolean> resultLD = updateRegisterViewModel.updateRegister(newLogin, newPassword, name, dateOfBirth, city, currentPhotoPath);

                resultLD.observe(UpdateRegisterActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean) {
                            Toast.makeText(UpdateRegisterActivity.this, "Registro atualizado com sucesso", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UpdateRegisterActivity.this, FeedActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(UpdateRegisterActivity.this, "Erro ao registrar novo usuário", Toast.LENGTH_LONG).show();
                        }

                        v.setEnabled(true);
                    }
                });
            }
        });

        Button btnBackLogin = findViewById(R.id.btnUpBackLogin);
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateRegisterActivity.this, FeedActivity.class);

                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LiveData<JSONObject> resultLD = updateRegisterViewModel.findUserByLogin(Config.getLogin(getApplicationContext()));

        resultLD.observe(UpdateRegisterActivity.this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject object) {
                if(object != null) {
                    try {
                        String login = object.getString("login");
                        String name = object.getString("nome");
                        String dateOfBirth = object.getString("data_nascimento");
                        String url = object.getString("foto");
                        String city = object.getString("cidade");

                        TextView title = findViewById(R.id.textTitle);
                        EditText editEmail =  findViewById(R.id.editEmail);
                        EditText editName =  findViewById(R.id.editName);
                        EditText editDateOfBirth =  findViewById(R.id.editDateOfBirth);
                        EditText editCity =  findViewById(R.id.editCity);
                        Button btnUpRegister = findViewById(R.id.btnUpRegister);

                        title.setText("Atualizar Dados");
                        editEmail.setText(login);
                        editName.setText(name);
                        editDateOfBirth.setText(dateOfBirth);
                        editCity.setText(city);
                        btnUpRegister.setEnabled(true);

                        downloadImageAsync(url);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(UpdateRegisterActivity.this, "Dados encontrados", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(UpdateRegisterActivity.this, "Não foi possível buscar os dados", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(UpdateRegisterActivity.this, FeedActivity.class);

                    startActivity(i);
                }
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
                        updateRegisterViewModel.setSelectPhotoLocation(filePath);

                        ImageView imvPhoto = findViewById(R.id.imgUser);
                        Bitmap bitmap = Util.getBitmap(this, selectedImageUri);
                        imvPhoto.setImageBitmap(bitmap);
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

    private String downloadImageToInternalStorage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Falha ao baixar a imagem, código de resposta: " + connection.getResponseCode());
        }

        InputStream inputStream = connection.getInputStream();

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

    private void downloadImageAsync(String imageUrl) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String filePath = downloadImageToInternalStorage(imageUrl);
                runOnUiThread(() -> {
                    updateRegisterViewModel.setSelectPhotoLocation(filePath);
                        ImageView imvPhoto = findViewById(R.id.imgUser);
                        File file = new File(filePath);
                        Uri uri = Uri.fromFile(file);
                        Bitmap bitmap = null;
                        try {
                            bitmap = Util.getBitmap(getApplicationContext(), uri);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        imvPhoto.setImageBitmap(bitmap);
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(UpdateRegisterActivity.this, "Erro ao baixar a imagem", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}