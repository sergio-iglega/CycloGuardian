package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 12/03/2018.
 */

public class IncomingCameraMessageReply extends IncomingCameraMessage {

    public  IncomingCameraMessageReply(int rval, int msg_id, int param) {
        this.paramToken = param;
        this.rval = rval;
        this.msg_id = msg_id;
    }

}
