package com.server;


import com.client.ChatWindow;
import com.sun.deploy.util.ArrayUtil;
import sun.security.krb5.internal.KDCOptions;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.Timer;

public class ClientHandler {            //represents a Model in MVC
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private String name;
    private Timer t;
    private String nick;

        public ClientHandler(Socket socket,Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.name="";
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            t = new Timer();
            Thread t1 = new Thread(()-> {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (name.equals("")) {
                    sendServerMessage("Timeout");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.start();

            new Thread(()-> {       //authentication
            try {
            while (true) {

                String s = in.readUTF();
                String[] elements = s.split("\\s");
                   if (SQL.checkIfUserExistsInDb(elements[0],elements[1])) {
                       ChatWindow.jOptionPopUp(true);
                       nick=elements[0];
                       sendServerMessage("/authok "+nick);      // confirmation of successful auth.
                       name=nick;
                       server.broadCast(name+" entered the chat");
                       server.subsribeMe(this);
                       break;

                    }else {
                       ChatWindow.jOptionPopUp(false);
                   }

                // nick = server.getAuthService().getNickByLoginPass(elements[1],elements[2]);

//                    if (nick!=null) {
//                        if (!server.isNicjBusy(nick)) {
//                            sendServerMessage("/authok "+nick);
//                            name=nick;
//
//                        }
//                }

            }   while (true) {      // private messages

                    String s= in.readUTF();
                    if (s.equalsIgnoreCase("/end"))break;
                     if (s.startsWith("/w")) {
                        String nick = s.split("\\s")[1];
                        String message = s.substring(3+nick.length());
                        ClientHandler c = server.match(nick);

                        c.sendServerMessage("from "+this.getName()+" " +message);
                        this.sendServerMessage(message);

                    }
                    else if(!s.startsWith("/w"))server.broadCast(s);
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                        server.unsubscribeMe(this);
                server.broadCast(   name!=""?name+"left chat":"");
                try {
                    socket.close();
                    } catch (IOException e) {
                                e.printStackTrace();
                    }
                }

        }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendServerMessage(String msg){
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
