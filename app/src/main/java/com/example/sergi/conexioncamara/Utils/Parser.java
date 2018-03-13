package com.example.sergi.conexioncamara.Utils;

import android.util.Log;

import com.example.sergi.conexioncamara.Messages.IncomingCameraMessage;
import com.example.sergi.conexioncamara.Messages.IncomingCameraMessageActionRecive;
import com.example.sergi.conexioncamara.Messages.IncomingCameraMessageActionState;
import com.example.sergi.conexioncamara.Messages.IncomingCameraMessageReply;
import com.example.sergi.conexioncamara.Messages.IncomingCameraMessageVideoReply;
import com.example.sergi.conexioncamara.Messages.IncomingCameraMessageVideoStart;

/**
 * Created by sergi on 12/03/2018.
 */

public class Parser {

    public IncomingCameraMessage parsearMensaje(byte[] cadena) {
        String cad = new String(cadena);
        String cadToken = cad.replace(" ", "");  //Quitamos los espacios existentes en la cadena
            //TODO mensaje REPLY
            if ((cadToken.contains("rval")) && (cadToken.contains("msg_id")) && (cadToken.contains("param")) &&
                    !(cadToken.contains("tmp"))) {
                String delims = "[{,}]";
                String[] tokens = cadToken.split(delims);
                int rval = 0, param = 0, msg_id = 0;
                for (int i = 0; i < tokens.length; i++) {  //Parseamos las cadenas primero por comas
                    String delims2 = "[:]";
                    String[] tokens2 = tokens[i].split(delims2);
                    for (int j = 0; j < tokens2.length; j++) {   //Parseamos para obtener las parejas atributo valor
                        if (tokens2[j].contains("rval")) {
                            rval = Integer.parseInt(tokens2[j + 1]);
                        }

                        if (tokens2[j].contains("msg_id")) {
                            msg_id = Integer.parseInt(tokens2[j + 1]);
                        }

                        if (tokens2[j].contains("param")) {
                            param = Integer.parseInt(tokens2[j + 1]);
                        }
                    }
                }

                    //Creamos el nuevo mensaje, llamando a su constructor
                    IncomingCameraMessageReply reply = new IncomingCameraMessageReply(rval, msg_id, param);
                    return reply;
            }

        //TODO mensaje Petición Recibido
        if ((cadToken.contains("rval")) && (cadToken.contains("msg_id"))) {
            String delims = "[{,}]";
            String[] tokens = cadToken.split(delims);
            int rval = 0,  msg_id = 0;
            for (int i = 0; i < tokens.length; i++) {  //Parseamos las cadenas primero por comas
                String delims2 = "[:]";
                String[] tokens2 = tokens[i].split(delims2);
                for (int j = 0; j < tokens2.length; j++) {   //Parseamos para obtener las parejas atributo valor
                    if (tokens2[j].contains("rval")) {
                        rval = Integer.parseInt(tokens2[j + 1]);
                    }

                    if (tokens2[j].contains("msg_id")) {
                        msg_id = Integer.parseInt(tokens2[j + 1]);
                    }
                }
            }

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageActionRecive actRecive = new IncomingCameraMessageActionRecive(rval,msg_id);
            return actRecive;
        }

        //TODO mensaje acción realizada
        if ((cadToken.contains("type")) && (cadToken.contains("msg_id")) && (cadToken.contains("param"))) {
            String delims = "[{,}]";
            String[] tokens = cadToken.split(delims);
            String param = null, type = null;
            int msg_id = 0;
            for (int i = 0; i < tokens.length; i++) {  //Parseamos las cadenas primero por comas
                String delims2 = "[:]";
                String[] tokens2 = tokens[i].split(delims2);
                for (int j = 0; j < tokens2.length; j++) {   //Parseamos para obtener las parejas atributo valor
                    if (tokens2[j].contains("type")) {
                        type = tokens2[j + 1];
                    }

                    if (tokens2[j].contains("msg_id")) {
                        msg_id = Integer.parseInt(tokens2[j + 1]);
                    }

                    if (tokens2[j].contains("param")) {
                        param = tokens2[j+1];
                    }
                }
            }

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageActionState actionState = new IncomingCameraMessageActionState(type,param,msg_id);
            return actionState;
        }

        //TODO mensaje video iniciado
        if ((cadToken.contains("type")) && (cadToken.contains("msg_id"))) {
            String delims = "[{,}]";
            String[] tokens = cadToken.split(delims);
            String type = null;
            int msg_id = 0;
            for (int i = 0; i < tokens.length; i++) {  //Parseamos las cadenas primero por comas
                String delims2 = "[:]";
                String[] tokens2 = tokens[i].split(delims2);
                for (int j = 0; j < tokens2.length; j++) {   //Parseamos para obtener las parejas atributo valor
                    if (tokens2[j].contains("type")) {
                        type = tokens2[j + 1];
                    }

                    if (tokens2[j].contains("msg_id")) {
                        msg_id = Integer.parseInt(tokens2[j + 1]);
                    }

                }
            }

            //Creamos el nuevo mensaje, llamando a su constructor
            IncomingCameraMessageVideoStart videoStart = new IncomingCameraMessageVideoStart(type, msg_id);
            return videoStart;
        }

        //TODO mensaje video reply
        if ((cadToken.contains("rval")) && (cadToken.contains("msg_id")) && (cadToken.contains("param")) &&
                (cadToken.contains("tmp"))) {
            String delims = "[{,}]";
            String[] tokens = cadToken.split(delims);
            int rval = 0, msg_id = 0;
            String param = null;
            for (int i = 0; i < tokens.length; i++) {  //Parseamos las cadenas primero por comas
                String delims2 = "[:]";
                String[] tokens2 = tokens[i].split(delims2);
                for (int j = 0; j < tokens2.length; j++) {   //Parseamos para obtener las parejas atributo valor
                    if (tokens2[j].contains("rval")) {
                        rval = Integer.parseInt(tokens2[j + 1]);
                    }

                    if (tokens2[j].contains("msg_id")) {
                        msg_id = Integer.parseInt(tokens2[j + 1]);
                    }

                    if (tokens2[j].contains("param")) {
                        param = tokens2[j + 1];
                    }
                }
            }

            //Creamos el nuevo mensaje, llamando a su constructor;
            IncomingCameraMessageVideoReply videoReply = new IncomingCameraMessageVideoReply(rval,msg_id,param);
            return videoReply;
        }

        IncomingCameraMessage msgFallo = new IncomingCameraMessage();
        return msgFallo;

    }
}
