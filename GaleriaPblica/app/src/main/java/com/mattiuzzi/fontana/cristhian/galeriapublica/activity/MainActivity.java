package com.mattiuzzi.fontana.cristhian.galeriapublica.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mattiuzzi.fontana.cristhian.galeriapublica.R;
import com.mattiuzzi.fontana.cristhian.galeriapublica.fragment.GridViewFragment;
import com.mattiuzzi.fontana.cristhian.galeriapublica.fragment.ListViewFragment;
import com.mattiuzzi.fontana.cristhian.galeriapublica.model.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
    static int RESULT_REQUEST_PERMISSION = 2;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

        // O trecho a seguir busca o menu de navegação (grade ou lista) e atribuiu a visualização do recicly view a partir da opção clicada no menu
        bottomNavigationView = findViewById(R.id.btNav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vm.setNavigationOpSelected(item.getItemId());

                if (item.getItemId() == R.id.gridViewOp) {
                    GridViewFragment gridViewFragment = GridViewFragment.newInstance();
                    setFragment(gridViewFragment);
                } else if (item.getItemId() == R.id.listViewOp) {
                    ListViewFragment listViewFragment = ListViewFragment.newInstance();
                    setFragment(listViewFragment);
                }

                return true;
            }
        });
    }

    // Solicita as permissões necessárias para visualizar as imagens na galeria do telefone
    @Override
    protected void onResume() {
        super.onResume();
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
        checkForPermissions(permissions);
    }

    // O método a seguir substitui a visualização da galeria entre grade e lista ou vice-versa.
    void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if(permissionsNotGranted.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !permissionsNotGranted.isEmpty()) {
                // Faz a requisição das permissões não concedidas para o usuário
                requestPermissions(permissionsNotGranted.toArray(new String[0]), RESULT_REQUEST_PERMISSION);
            }
        } else {
            MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

            int navigationOpSelected = vm.getNavigationOpSelected();

            bottomNavigationView.setSelectedItemId(navigationOpSelected);
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

        if(permissionsRejected.size() > 0) {
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
        } else {
            MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

            int navigationOpSelected = vm.getNavigationOpSelected();

            bottomNavigationView.setSelectedItemId(navigationOpSelected);
        }
    }
}