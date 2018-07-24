package alves.thiago.lanchonete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder = null;

    EditText Quantidade;
    EditText Colaborador;
    EditText Setor;
    Spinner s;
    List<String> item;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button Finalizar;
        Finalizar = (Button) findViewById(R.id.Finalizar);
        Setor = (EditText) findViewById(R.id.Setor);
        Colaborador = (EditText) findViewById(R.id.Colaborador);
        Quantidade = (EditText) findViewById(R.id.Quantidade);



       s = (Spinner) findViewById(R.id.spinner);



        Thread Get_Clientes = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String SERVER_IP = "172.16.4.64";//editText.getText().toString();
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                    Socket socket = new Socket(serverAddr, 7070);

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    String message = br.readLine();
                    System.out.println("Message received from the server : " + message);

                    item = new ArrayList<String>();
                    item.add(message);

                    while (message != null){
                        message = br.readLine();
                        if (message != null) {
                            System.out.println("Message received from the server : " + message);
                            item.add(message);
                        }

                    }



                   // socket.getOutputStream().write(itemValue.getBytes());
                    //socket.getOutputStream().flush();
                    //socket.close();

                }catch (Exception e ){
                    e.printStackTrace();

                }

            }
        });

        Get_Clientes.start();
        try {
            Get_Clientes.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] stockArr = new String[item.size()];
        stockArr = item.toArray(stockArr);



        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, stockArr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s.setAdapter(adapter);




        Finalizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                builder.setMessage("Você tem confirma que a/o "+ Colaborador.getText() +" do setor " + Setor.getText()
                + " será cadastrado para o fiado do valor de R$ 5 reais?")
                        .setTitle("Confirmação");

                builder.setPositiveButton("Confirmo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });



                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });

    }

}
