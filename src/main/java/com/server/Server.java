package com.server;




import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

// represents a Controller role in MVC pattern , creates ClientHAndler objects, manipulates its methods
// (MVC is not complete becouse Server still doesn`t controls (updates) view.

public class Server {
    private final int PORT = 8189;
    private Vector<ClientHandler>clients;
    private Vector<ArrayList<ClientHandler>> privates;
    private AuthService authService;
    private Timer t;
    public AuthService getAuthService() {
        return authService;
    }


    public Server() {


        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();
        authService = new BaseAuthService();
        privates = new Vector<>();
        t = new Timer();

    authService.start();

        try {
            server = new ServerSocket(PORT);      //creating server socket listening for the clients and connecting to them
            System.out.println("Server is up...");

            while (true) {
                socket = server.accept();         // stand by mode, waiting for client`s action

                System.out.println("Client has connected successfully...");
                new ClientHandler(socket,this);

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server failed to start!!!");
        } finally {
            try {
                authService.stop();
                socket.close();
                server.close();             //closing server connection(all the clients will be dropped as well)

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public  synchronized void broadCast(String msg) { // broadcast message too all clients
        for (ClientHandler c:clients) {
            c.sendServerMessage(msg);
        }

}
public void broadCastUserList() {            //updating userlist panel at the right side of the window, broadcast message
        StringBuffer stg = new StringBuffer("/usersList");

        for (ClientHandler c:clients) {

            stg.append(" ").append(c.getName());

        }
        for (ClientHandler c:clients){
            c.sendServerMessage(stg.toString());
        }


}
public  ClientHandler match(String nick) {         //checking by nickname for sending private messages
    for (ClientHandler o : clients) {
        if (o.getName().equals(nick)) {
            return o;

        }
    }return null;
}
public synchronized boolean isNicjBusy(String nick) {
        for (ClientHandler o:clients) {
            if (o.getName().equals(nick)) {
                return true;

            }
        }return false;
}
public synchronized void subsribeMe(ClientHandler c) {
clients.add(c);
broadCastUserList();
}
public synchronized void unsubscribeMe(ClientHandler c) {
        clients.remove(c);
        broadCastUserList();
}


}







