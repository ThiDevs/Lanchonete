package alves.thiago.lanchonete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String hostIP;
    static String IP;
    static Map<String, List<String>> MAP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botao,botao2;
        botao = findViewById(R.id.button);

        botao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Intent it = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(it);

            }
        });

        botao2 = findViewById(R.id.button2);

        botao2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Intent it = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(it);

            }
        });



        Thread AchaServer = new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    String ip = null;
                    try {
                        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                        while (interfaces.hasMoreElements()) {
                            NetworkInterface iface = interfaces.nextElement();
                            if (iface.isLoopback() || !iface.isUp())
                                continue;

                            Enumeration<InetAddress> addresses = iface.getInetAddresses();
                            while(addresses.hasMoreElements()) {
                                InetAddress addr = addresses.nextElement();
                                ip = addr.getCanonicalHostName();
                                System.out.println(ip);

                            }
                        }
                    } catch (SocketException e) {
                        throw new RuntimeException(e);

                    }

                    assert ip != null;
                    String[] a = ip.split("[.]");


                    String ipSub = a[0] + "." + a[1] + "." + a[2];
                    System.out.println(ipSub);

                    ArrayList<String> item = new ArrayList<String>();

                    for (int i = 1; i < 255; i++) {
                        hostIP = ipSub + "." + i;
                        System.out.println(hostIP);

                        try {
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(hostIP, 7070), 100);

                            InputStream is = socket.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            String message = br.readLine();
                            System.out.println("Message received from the server : " + message);


                            socket.close();
                            setIP(hostIP);

                            break;
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        //AchaServer.start();
        setIP("172.16.3.112");
        Thread teste = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    hostIP = "172.16.3.112";
                    socket.connect(new InetSocketAddress(hostIP, 7070), 10000);

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    String message = br.readLine();
                    System.out.println("Message received from the server : " + message);

                    Type mapType = new TypeToken<Map<String, List<String>>>(){}.getType();
                    Map<String, List<String>> son = new Gson().fromJson(message, mapType);
                    setMAP(son);
                    socket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        teste.start();



    }

    public void setIP(String ip){
        IP = ip;
    }
    public static String getIP(){
        return IP;
    }
    public void setMAP( Map<String, List<String>> map){
        MAP = map;
    }
    public static Map<String, List<String>> getMAP(){
        return MAP;
    }

}
