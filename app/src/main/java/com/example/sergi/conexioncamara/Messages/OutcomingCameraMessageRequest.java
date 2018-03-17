package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageRequest extends OutcomingCameraMessage {

    public OutcomingCameraMessageRequest(int msg_id) {
        this.msg_id = msg_id;
        this.token = 0;
    }

    public String componerMensajeRequest() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + "}";
        return cadena;
    }
}
