package com.mattiuzzi.fontana.cristhian.email;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etEmail = (EditText) findViewById(R.id.etEmail);
                EditText etAssunto = (EditText) findViewById(R.id.etAssunto);
                EditText etTexto = (EditText) findViewById(R.id.etTexto);

                String email = etEmail.getText().toString();
                String assunto = etAssunto.getText().toString();
                String texto = etTexto.getText().toString();

                // Intent ACTION_SENDTO, procura quais aplicativos podem resolver a ação
                Intent i = new Intent(Intent.ACTION_SENDTO);

                // Com mailto, é especificado somente os aplicativos que podem receber e enviar e-mail
                i.setData(Uri.parse("mailto:"));

                String[] emails = new String[]{email};

                // Preenchimento dos campos extras para enviar para o aplicativo de e-mail compor a mensagem
                i.putExtra(Intent.EXTRA_EMAIL, emails);
                i.putExtra(Intent.EXTRA_SUBJECT, assunto);
                i.putExtra(Intent.EXTRA_TEXT, texto);

                try {
                    // Intent.createChoose mostra menu para o usuário escolher o aplicativo de capazes de resolver a ação
                    startActivity(Intent.createChooser(i, "Escolha o APP"));
                }
                catch (ActivityNotFoundException e) {
                    // Captura e mostra um erro informando que não tem nenhum aplicativo capaz de resolver a ação
                    Toast.makeText(MainActivity.this, "Não há nenhum app que posso realizar essa operação", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}