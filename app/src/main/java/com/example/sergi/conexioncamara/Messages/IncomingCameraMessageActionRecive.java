package com.example.sergi.conexioncamara.Messages;

/**
 * Created by sergi on 12/03/2018.
 */

public class IncomingCameraMessageActionRecive extends IncomingCameraMessage {

    public IncomingCameraMessageActionRecive(int rval, int msg_id){
        this.rval = rval;
        this.msg_id = msg_id;
    }
}
