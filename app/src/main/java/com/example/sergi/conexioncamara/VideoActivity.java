package com.example.sergi.conexioncamara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import android.os.AsyncTask;

import com.example.sergi.conexioncamara.Messages.IncomingCameraMessage;
import com.example.sergi.conexioncamara.Messages.OutcomingCameraMessage;
import com.example.sergi.conexioncamara.Messages.OutcomingCameraMessageRequest;
import com.example.sergi.conexioncamara.Messages.OutcomingCameraMessageVideo;
import com.example.sergi.conexioncamara.Utils.Constants;
import com.example.sergi.conexioncamara.Utils.Parser;

public class VideoActivity extends AppCompatActivity {

    TextView textStatus;
    NetworkTask networktask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        textStatus = (TextView)findViewById(R.id.textStatus);
    }

    public void iniciarVideo(View view) throws IOException {
        OutcomingCameraMessageVideo msg = new OutcomingCameraMessageVideo(Constants.MSG_ID_START_VIDEO); //Constructor del mensaje
        networktask = new NetworkTask();
        networktask.execute(msg);

    }



    public void detenerVideo(View view) throws IOException {
        OutcomingCameraMessageVideo msg = new OutcomingCameraMessageVideo(Constants.MSG_ID_STOP_VIDEO); //Constructor del mensaje
        networktask = new NetworkTask();
        networktask.execute(msg);
    }

    public class NetworkTask extends AsyncTask<OutcomingCameraMessageVideo, byte[], Boolean> {

        Socket nsocket; //Network Socket
        InputStream nis; //Network Input Stream
        OutputStream nos; //Network Output Stream

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "onPreExecute");
        }

        @Override
        protected Boolean doInBackground(OutcomingCameraMessageVideo... params) { //This runs on a different thread
            boolean result = false;
            String ruta;
            Parser p = new Parser();

            try {

                OutcomingCameraMessageVideo msgVideo = params[0];
                OutcomingCameraMessageRequest msg = new OutcomingCameraMessageRequest(Constants.MSG_ID_REQUEST);

                //TODO INICIAR LA CONEXIÓN
                Log.i("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress(Constants.IP_CAMARA, Constants.PUERTO_CAMARA);
                nsocket = new Socket();
                nsocket.connect(sockaddr, Constants.TIMEOUT_SOCKET); //10 segundos de timeout
                if (nsocket.isConnected()) {
                    nis = nsocket.getInputStream();  //Creamos los streams
                    nos = nsocket.getOutputStream();
                    Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                    Log.i("AsyncTask", "doInBackground: Waiting for inital data...");
                    //TODO OBTENER EL TOKEN
                    //Enviamos el mensaje inicial para obtener el token
                    String cmd = msg.componerMensajeRequest();  //Creación de la cadena inicial
                    nos.write(cmd.getBytes());
                    Log.i("AsyncTask", "doInBackground: Waiting for reply message..");
                    byte[] buffer = new byte[Constants.TAMANO_BUFFER];
                    int read = nis.read(buffer, 0, Constants.TAMANO_BUFFER); //This is blocking
                    byte[] tempdata = new byte[read];
                    System.arraycopy(buffer, 0, tempdata, 0, read);
                    //IncomingCameraMessage inMsgReply = new IncomingCameraMessage(tempdata);
                    publishProgress(tempdata);
                    //Parsear la cadena recibida
                    IncomingCameraMessage inMsgReply = p.parsearMensaje(tempdata);
                    if(inMsgReply.rval < 0) {
                        //La acción de tomar foto ha fallado
                        return result;
                    }

                    //TODO SETEAR EL TOKEN EN EL MSG
                    //OutcomingCameraMessage msgPhoto = new OutcomingCameraMessage(769, inMsgReply.paramToken);
                    msgVideo.setToken(inMsgReply.paramToken); //Modificamos el token del mensaje
                    String cmdVideo = msgVideo.componerMensajeVideo();

                    //TODO ENVIAR MSG
                    nos.write(cmdVideo.getBytes());

                    //TODO ESPERAR RESPUESTA --> CON TIMEOUT
                    byte[] inputData = new byte[1024];
                    int readCount = readInputStreamWithTimeout(nis, inputData, Constants.TIMEOUT_MENSAJE);  //TimeOut de 6 segundos
                    byte[] tempdataPhoto = new byte[readCount];
                    System.arraycopy(inputData, 0, tempdataPhoto, 0, readCount);
                    publishProgress(tempdataPhoto);

                    //Comprobar los mensajes recibidos y determinar si ha habido una respuesta correcta
                    //Primeramente parseamos las cadenas
                    int i;
                    String cadena = new String(tempdataPhoto);
                    String delimiter = "[{]";
                    String[] parts = cadena.split(delimiter);
                    for (i = 0; i < parts.length; i++){  //Recorremos el array de partes
                        //Añadimos el caracter que anteriormente hemos eliminado
                        parts[i] = "{" + parts[i];
                    }

                    IncomingCameraMessage[] msgsRespond = new IncomingCameraMessage[i];
                    for (i = 0; i < parts.length; i++){  //Recorremos el array de partes y cremos los distintos mensajes
                        msgsRespond[i] = p.parsearMensaje(parts[i].getBytes());
                        if(msgsRespond[i].type != null) {
                            if (msgsRespond[i].type.contains("video_record_complete")) {
                                result = true;
                                if (msgsRespond[i].param != null){
                                    ruta = msgsRespond[i].param;
                                    publishProgress(ruta.getBytes());
                                }
                            }

                            if (msgsRespond[i].type.contains("start_video_record")) {
                                result = true;
                            }
                        }
                    }


                    //TODO CERRAR SOCKETS Y STREAMS
                    nis.close();
                    nos.close();
                    nsocket.close();

                }

                //TODO RETURN OK | ERROR

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        public int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
                throws IOException  {
            int bufferOffset = 0;
            long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
            while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
                int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
                // can alternatively use bufferedReader, guarded by isReady():
                int readResult = is.read(b, bufferOffset, readLength);
                if (readResult == -1) break;
                bufferOffset += readResult;
            }
            return bufferOffset;
        }


        @Override
        protected void onProgressUpdate(byte[]... values) {
            String str = null;
            if (values.length > 0) {
                Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                str = new String(values[0]);
                textStatus.setText(str);

            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            System.out.println("RESULTADO: " +result);
            if (result == true) {
                Log.i("AsyncTask", "onPostExecute: Completed.");
                //Hacer aquí un Toast de que algo ha fallado
                textStatus.setText("El video se ha iniciado/parado");
            } else {
                Log.i("AsyncTask", "onPostExecute: Something ocurred.");
                textStatus.setText("No se ha podido realizar el video");
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        networktask.cancel(true); //In case the task is currently running
    }

}
