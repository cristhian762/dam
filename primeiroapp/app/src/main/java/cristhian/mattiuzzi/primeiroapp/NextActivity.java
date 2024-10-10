package cristhian.mattiuzzi.primeiroapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Captura a intenção que carregou essa página
        Intent i = getIntent();

        // Busca dentro do Intent um dado com a chave text
        String text = i.getStringExtra("text");

        // Busca o recurso com id tvText 
        TextView tvText = findViewById(R.id.tvText);

        // Muda o texto presente em tvText para o texto contido na variável text
        tvText.setText(text);
    }
}
