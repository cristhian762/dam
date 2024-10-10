package cristhian.mattiuzzi.primeiroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// classe AppCompatActivity para retrocompatibilidade
// MainActivity classe principal da aplicação
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // setContentView responsável por atribuir a visualização que será carregada
        // R.layout.activity_main, R é o recurso presente na pasta res, layout é o diretório e
        // o activity_main é o xml contendo a visualização 
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // findViewById busca no contexto da MainActivity o recurso que tem determinado identificador
        // R.id.btnSend é o recurso presente na pasta res cujo identificador é btnSend
        // A variavel do tipo Button btnSend armazena o recurso do botão de enviar que tem o id btnSend
        Button btnSend = findViewById(R.id.btnSend);

        // No contexto do botão de enviar, sobrescreve a ação de click no botão 
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Busca o recurso que representa o campo de entrada de texto
                EditText etText = findViewById(R.id.etText);

                // No contexto do campo de entrada pega o elemento de texto com getText e converte para string 
                // com o toString e salva em uma variável text do tipo Sring
                String text = etText.getText().toString();

                // Cria uma instância de Intent que armazena a intenção de sair da classe atual MainActivity 
                // e "ir para" NextActivity. No caso MainActivity sai do fluxo principal e é destruída enquanto
                // uma nova instanciada da classe NextActivity é criada 
                Intent i = new Intent(MainActivity.this, NextActivity.class);

                // Adiciona um dado adicional na instância de Intent chamado text que contem o texto do campo de
                // entrada de texto
                i.putExtra("text", text);

                // Sinaliza para o sistema operacional que há uma intenção pronta e aguardando execução
                startActivity(i);
            }
        });
    }
}
