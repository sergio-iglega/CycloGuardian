package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 13/03/2018.
 */

public class IncomingCameraMessageVideoReply extends IncomingCameraMessage {

    public  IncomingCameraMessageVideoReply(int rval, int msg_id, String param) {
        this.rval = rval;
        this.msg_id = msg_id;
        this.param = param;
    }
}

