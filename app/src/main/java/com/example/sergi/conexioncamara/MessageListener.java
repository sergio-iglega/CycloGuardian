package com.example.sergi.conexioncamara;

import com.example.sergi.conexioncamara.Messages.CameraMessage;

/**
 * Created by sergi on 27/02/2018.
 */

public interface MessageListener {

    void onNewMessageReceived(CameraMessage message);
}
