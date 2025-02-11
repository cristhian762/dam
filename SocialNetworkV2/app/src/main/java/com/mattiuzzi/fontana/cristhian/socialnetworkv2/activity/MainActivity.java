package com.mattiuzzi.fontana.cristhian.socialnetworkv2.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.model.LoginViewModel;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;

    static int RESULT_REQUEST_PERMISSION = 2;
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

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        checkForPermissions(permissions);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(i);
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editLogin =  findViewById(R.id.editLogin);
                final String login = editLogin.getText().toString();
                if(login.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Campo Login não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText editPassword =  findViewById(R.id.editPassword);
                final String password = editPassword.getText().toString();
                if(password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Campo de senha não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                v.setEnabled(false);

                LiveData<Boolean> resultLD = loginViewModel.login(login, password);

                resultLD.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean) {
                            Toast.makeText(MainActivity.this, "Usuário Logado com sucesso", Toast.LENGTH_LONG).show();

                            Config.setLogin(MainActivity.this, login);
                            Config.setPassword(MainActivity.this, password);

                            Intent i = new Intent(MainActivity.this, FeedActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Servidor indisponível", Toast.LENGTH_LONG).show();
                        }

                        v.setEnabled(true);
                    }
                });
            }
        });
    }

    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        for(String permission : permissions) {
            if( !hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
    }

    private boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if(requestCode == RESULT_REQUEST_PERMISSION) {

            for(String permission : permissions) {
                if(!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if(permissionsRejected.size() > 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this).
                            setMessage("Para usar essa app é preciso conceder essas permissões").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                                }
                            }).create().show();
                }
            }
        }
    }
}
