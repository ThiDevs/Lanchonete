package alves.thiago.lanchonete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button botao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botao = (Button) findViewById(R.id.button);

        botao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Intent it = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(it);

            }
        });


    }

}
