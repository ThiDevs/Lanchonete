package alves.thiago.lanchonete;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;
import static android.R.layout.simple_list_item_2;
import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;

public class Main2Activity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder = null;

    EditText Quantidade;
    EditText Colaborador;
    EditText Setor;
    Spinner s;
    Spinner s2;
    List<String> item;
    List<String> item2;
    ArrayAdapter<String> adapter;
    ListView listview;
    //ArrayAdapter<String> adapter2;
    List<String> value = new ArrayList<String>();
    String hostIP;
    ArrayAdapter<String> adapter2;
    Map<String, List<String>> MAP;
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

        MAP = MainActivity.getMAP();
        hostIP = MainActivity.getIP();
        System.out.println(MAP);

        listview  = (ListView) findViewById(R.id.list);


        adapter2 = new ArrayAdapter<String>(this,simple_list_item_1, android.R.id.text1, value);
        listview.setAdapter(adapter2);


       s = (Spinner) findViewById(R.id.spinner);




            Thread Get_Clientes = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String SERVER_IP = hostIP;//editText.getText().toString();
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



                }catch (Exception e ){
                    e.printStackTrace();

                }

            }
        });

       // Get_Clientes.start();
        try {
            Get_Clientes.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] stockArr = new String[MAP.keySet().size()];
        stockArr = MAP.keySet().toArray(stockArr);

        adapter = new ArrayAdapter<String>(this, simple_spinner_item, stockArr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parentView, View selectedItemView, final int position, long id) {

                String Tipo = parentView.getItemAtPosition(position).toString();
                setItem(Tipo);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                        String itemValue = (String) listview.getItemAtPosition(position);
                        Thread EnviarIndexVideo = new Thread(new Runnable() {
                            public void run() {


                            }

                        });
                    }
                });



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

    public void setItem(String Tipo){


       // String[] stockArr2 = new String[MAP.get(Tipo).size()];
       // stockArr2 = MAP.keySet().toArray(stockArr2);

      //  value.addAll(Arrays.asList(stockArr2));




       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }



}
