package com.example.produtos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.produtos.util.Config;

/**
 * A classe MainActivity é a primeira a ser chamada. Entretanto, aqui essa Activity não possui
 * interface de usuário associada (arquivo layout xml). No arquivo AndroidManifest.xml nós
 * declaramos que o tema de MainActivity é NoDisplay, o que significa que essa tela não vai mostra
 * UI.
 *
 * O objetivo de MainActivity é somente verificar se o usuário já está logado ou não. Se o usuário
 * não estiver logado, a app redireciona para a tela de login (LoginActiviy). Caso o usuário já
 * esteja logado, então a app redireciona para a tela principal da app (HomeActivity).
 *
 * A classe Config possui funções que guardam as informações de login e senha em um espaço privado e
 * seguro da app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se o usuário ainda não logou, então não existe informação de login guardada na app.
        // Então a app é redirecionada para a tela de login.
        if(Config.getLogin(MainActivity.this).isEmpty()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        // Se o usuário já logou, então a informação de login está guardada na app. Então
        // a app é redirecionada para a tela principal da app (HomeActivity)
        else {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

    }
}