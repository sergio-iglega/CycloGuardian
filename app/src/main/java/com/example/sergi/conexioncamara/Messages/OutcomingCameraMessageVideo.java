package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageVideo extends OutcomingCameraMessage {

    public OutcomingCameraMessageVideo(int msg_id) {
        this.msg_id = msg_id;
    }

    public String componerMensajeVideo() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + "}";
        return cadena;
    }
}
