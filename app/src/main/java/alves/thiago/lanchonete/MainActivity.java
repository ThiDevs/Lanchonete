package alves.thiago.lanchonete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;


public class MainActivity extends AppCompatActivity {
    Button botao;
    String hostIP;
    static String IP;
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




        Thread AchaServer = new Thread(new Runnable() {
            @Override
            public void run() {

                try {



                    String ip = null;
                    try {
                        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                        while (interfaces.hasMoreElements()) {
                            NetworkInterface iface = interfaces.nextElement();
                            // filters out 127.0.0.1 and inactive interfaces
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
        AchaServer.start();



    }

    public void setIP(String ip){
        IP = ip;
    }
    public static String getIP(){
        return IP;
    }

}
