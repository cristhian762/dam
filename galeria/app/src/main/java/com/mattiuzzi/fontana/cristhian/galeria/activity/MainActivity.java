package com.mattiuzzi.fontana.cristhian.galeria.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mattiuzzi.fontana.cristhian.galeria.R;
import com.mattiuzzi.fontana.cristhian.galeria.adapter.MainAdapter;
import com.mattiuzzi.fontana.cristhian.galeria.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
    static int RESULT_REQUEST_PERMISSION = 2;
    String currentPhotoPath;

    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Atribui uma ActionBar para a tela inicial
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Faz a leitura da lista de fotos já salvas
        File[] files = dir.listFiles();

        for(int i = 0; i < files.length; i++) {
            // Adiciona o caminho de cada imagem no array de fotos
            photos.add(files[i].getAbsolutePath());
        }

        mainAdapter = new MainAdapter(MainActivity.this, photos);
        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setAdapter(mainAdapter);

        float w = getResources().getDimension(R.dimen.itemWidth);
        // Calcula o número de colunas
        int numberOfColumns = Utils.calculateNoOfColumns(MainActivity.this, w);

        // Ajusta a exibição para um GRID
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        rvGallery.setLayoutManager(gridLayoutManager);

        // Cria list vazia de permissões
        List<String> permissions = new ArrayList<>();

        // Adiciona permissão de acesso a câmera
        permissions.add(Manifest.permission.CAMERA);

        // Verifica concessão das permissões
        checkForPermissions(permissions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Verifica se o item da toolbar clicado foi o de “tirar foto”
        if (item.getItemId() == R.id.opCamera) {
            dispatchTakePictureIntent();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPhotoActivity(String photoPath) {
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
    }

    private void dispatchTakePictureIntent() {
        File f = null;

        try {
            f = createImageFile();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();

            return;
        }

        currentPhotoPath = f.getAbsolutePath();

        if(f != null) {
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "mattiuzzi.fontana.cristhian.galeria.fileprovider", f);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);

        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_TAKE_PICTURE) {
            if(resultCode == Activity.RESULT_OK) {
                photos.add(currentPhotoPath);
                mainAdapter.notifyItemInserted(photos.size()-1);
            } else {
                File f = new File(currentPhotoPath);
                f.delete();
            }
        }
    }

    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !permissionsNotGranted.isEmpty()) {
            // Faz a requisição das permissões não concedidas para o usuário
            requestPermissions(permissionsNotGranted.toArray(new String[0]), RESULT_REQUEST_PERMISSION);
        }
    }

    private boolean hasPermission(String permission) {
        // Verifica se a permissão já foi concedida
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }

    // Método chamado após aceitar ou não a permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> permissionsRejected = new ArrayList<>();

        if (requestCode == RESULT_REQUEST_PERMISSION) {
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if (!permissionsRejected.isEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                // Abre o modal solicitando permissões ao usuário
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Para usar essa app é preciso conceder essas permissões")
                        .setPositiveButton("OK", (dialog, which) ->
                                requestPermissions(permissionsRejected.toArray(new String[0]), RESULT_REQUEST_PERMISSION)
                        )
                        .create()
                        .show();
            }
        }
    }
}