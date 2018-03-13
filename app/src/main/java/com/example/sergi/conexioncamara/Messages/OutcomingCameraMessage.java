package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 28/02/2018.
 */

public class OutcomingCameraMessage extends CameraMessage {


    public OutcomingCameraMessage(int msg_id, int token) {
        this.msg_id = msg_id;
        this.token = token;
    }

    public OutcomingCameraMessage(int msg_id) {
        this.msg_id = msg_id;
    }

    public String componerMensajeRequest() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + "}";
        return cadena;
    }
}
