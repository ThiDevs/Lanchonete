package alves.thiago.lanchonete;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import java.io.*;
import java.net.Socket;
import java.util.*;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.simplify.ink.InkView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;
import static android.R.layout.simple_list_item_2;
import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;

public class Main2Activity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder = null;

    TextView Quantidade;
    EditText Colaborador;
    EditText Setor;
    TextView Valor;
    Spinner s;
    List<String> item;
    List<String> item2;
    ArrayAdapter<String> adapter;
    ListView listview;
    List<String> value;
    String hostIP;
    ArrayAdapter<String> adapter2;
    Map<String, List<String>> MAP;
    HashMap<String, Integer> items;
    Intent intent;
    InkView ink;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button Finalizar,Limpar_Dados;
        Finalizar = (Button) findViewById(R.id.Finalizar);
        Limpar_Dados = (Button) findViewById(R.id.Limpar_Dados);
        Setor = (EditText) findViewById(R.id.Setor);
        Colaborador = (EditText) findViewById(R.id.Colaborador);
        Quantidade = (TextView) findViewById(R.id.Quantidade);
        Valor = (TextView) findViewById(R.id.Valor);
        ink = (InkView) findViewById(R.id.ink);

        MAP = MainActivity.getMAP();
        hostIP = MainActivity.getIP();
        System.out.println(MAP);

        listview  = (ListView) findViewById(R.id.list);
        value = new ArrayList<String>();
        value.add("");
        items = new HashMap<String, Integer>();
        intent = new Intent(this, MainActivity.class);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, value);
        listview.setAdapter(adapter2);







        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter2.notifyDataSetChanged();
                }
            });
        }catch (Exception e ){
            e.printStackTrace();
        }

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
                        Log.d("MyApp",itemValue);

                        String NomeEscolhido = itemValue.split(" [-] ")[0];
                        System.out.println(NomeEscolhido);
                        Float Valor_Separado = Float.valueOf(itemValue.split(" [-] ")[1]);
                        Log.d("MyApp",String.valueOf(Valor_Separado));

                        try{
                            items.put(NomeEscolhido,items.get(NomeEscolhido)+1);
                        } catch (Exception e){
                            items.put(NomeEscolhido,1);
                            e.printStackTrace();
                        }



                        if (Valor.getText().toString().equals("Valor")){
                            Valor.setText("0");
                        }
                        if (Valor.getText() != null){
                            String ValorSoma = Valor.getText().toString();
                            Log.d("MyApp",ValorSoma);
                            Float ValorSoma2 = Float.valueOf(ValorSoma);
                            Float valortotal =  ValorSoma2 + Valor_Separado;
                            Log.d("MyApp",String.valueOf(valortotal));
                            Valor.setText(String.valueOf(valortotal));

                            Quantidade.setText(items.toString().replaceAll("[{]","").replaceAll("[}]",""));



                        }



                    }
                });


        Finalizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                builder.setMessage("Você confirma que a/o "+ Colaborador.getText() +" do setor " + Setor.getText()
                + " será cadastrado o pedido do valor de R$ "+Valor.getText()+" reais?")
                        .setTitle("Confirmação");

                builder.setPositiveButton("Confirmo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);



                        Thread SendFiscal = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket C2 = new Socket(hostIP,7071);
                                    PrintWriter writer = new PrintWriter(C2.getOutputStream());
                                    String fiscal = Colaborador.getText() +";" + Setor.getText()
                                            + ";"+Quantidade.getText()+";"+Valor.getText();

                                    int color = Color.TRANSPARENT;
                                    Drawable background = ink.getBackground();
                                    if (background instanceof ColorDrawable)
                                        color = ((ColorDrawable) background).getColor();
                                    Bitmap bitmap = ink.getBitmap(color);


                                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                                    OutputStream outStream = null;
                                    File file = new File(extStorageDirectory, "era.png");
                                    String path = file.getPath();
                                    try {
                                        outStream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                        outStream.flush();
                                        outStream.close();
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                    String toSend = fiscal+"SEPARAPAL"+encoded;
                                    Writer out = new BufferedWriter(new OutputStreamWriter(C2.getOutputStream(), "UTF8"));

                                    out.write(toSend);
                                    out.flush();
                                    out.close();




                                    //writer.write(a);
                                    //writer.close();
                                    C2.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        SendFiscal.start();


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

        Limpar_Dados.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Quantidade.setText("");
                Setor.setText("");
                Colaborador.setText("");
                Valor.setText("Valor");
                ink.clear();

            }});

    }

    public void setItem(String Tipo){

        try {
            String[] stockArr2 = new String[MAP.get(Tipo).size()];
            stockArr2 = MAP.get(Tipo).toArray(stockArr2);

            Log.d("MyApp",MAP.toString());

            value.clear();

            value.addAll(Arrays.asList(stockArr2));
        } catch (Exception e ){
            e.printStackTrace();
        }

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter2.notifyDataSetChanged();
                }
            });
        }catch (Exception e ){
            e.printStackTrace();
        }




    }



}
