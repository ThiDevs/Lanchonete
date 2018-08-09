package alves.thiago.lanchonete;

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;



public class Main3Activity extends AppCompatActivity {
    String IP;
    ConstraintLayout Layout;
    ListView listview;
    List<CardView> value;
    ArrayAdapter<CardView> adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Layout = findViewById(R.id.layout);

        IP = MainActivity.getIP();
        listview  =  findViewById(R.id.list);
        value = new ArrayList<>();

        value.add(new CardView(this));
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, value);
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





        Thread teste = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    String hostIP = IP;
                    socket.connect(new InetSocketAddress(hostIP, 7072), 10000);

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    String message = br.readLine();
                    Type mapType = new TypeToken<List<String>>(){}.getType();
                    Gson gson = new Gson();
                    List<String> list = gson.fromJson(message,mapType);


                    Log.d("MyApp", list.get(0));
                    CreateCard(list.get(0));



                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        teste.start();




    }


    public void CreateCard(String name){
        CardView card = new CardView(this);

        // Set the CardView layoutParams
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        card.setLayoutParams(params);
        card.setRadius(9);
        card.setContentPadding(100, 100, 500, 100);

        int color = Color.TRANSPARENT;
        Drawable background = Layout.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        card.setCardBackgroundColor(color);
        card.setMaxCardElevation(15);
        card.setCardElevation(9);
        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(name);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        tv.setTextColor(Color.BLACK);

        // Put the TextView in CardView
        card.addView(tv);
        value.add(card);
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
        // Finally, add the CardView in root layout
        //Layout.addView(card);

    }
}
