package com.example.sergi.conexioncamara;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

public class Conectar extends AppCompatActivity {

    private RadioButton rv, ri, rl;
    Context context;
    Boolean isVideoMode = false;
    Boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectar);
        context = getApplicationContext();

        rv = (RadioButton) findViewById(R.id.radButtonV);
        ri = (RadioButton) findViewById(R.id.radButtonIm);
        rl = (RadioButton) findViewById(R.id.radButtonLive);
    }

    public void iniciar(View view) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifi.getWifiState();
       // int conectado = 0;
       // while (conectado == 0) {
            if (state == WIFI_STATE_ENABLED) {
                WifiConfiguration wc = new WifiConfiguration();
                // This is must be quoted according to the documentation
                // http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
                wc.SSID = "\"SJ5000+_370571\"";
                wc.preSharedKey = "\"12345678\"";
                wc.hiddenSSID = true;
                wc.status = WifiConfiguration.Status.ENABLED;
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                int res = wifi.addNetwork(wc);
                Log.d("WifiPreference", "add Network returned " + res);
                boolean b = wifi.enableNetwork(res, true);
                Log.d("WifiPreference", "enableNetwork returned " + b);
                Toast notificacion = Toast.makeText(this, "Conectado a SJ500+_420613", Toast.LENGTH_LONG);
                notificacion.show();
               // conectado = 1;
            } else {
                //Mensaje para conectar el wifi
                Toast notificacion = Toast.makeText(this, "Red wifi desconectada", Toast.LENGTH_LONG);
                notificacion.show();
            }


        //}

        if (rv.isChecked() == true) {
            //Caso de que la opción seleccionada sea un  video
            System.out.println("Video Mode");
            //Cambiamos de actividad
            Intent i = new Intent(this, VideoActivity.class);
            startActivity(i);

        } else if (ri.isChecked() == true) {
            //Caso de que la opción seleccionada sea una imagen
            System.out.println("Photo Mode");
            //Cambiamos de actividad
            Intent i = new Intent(this, FotoActivity.class);
            startActivity(i);
        } else if (rl.isChecked() == true) {
            //Caso de que la opción seleccionada sea video en directo
            System.out.println("Live Mode");
            //Cambiamos de actividad
            Intent i = new Intent(this, LiveActivity.class);
            startActivity(i);
        }

    }

    public void detener(View view) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.disconnect();
    }
}
