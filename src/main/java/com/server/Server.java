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


public class Server {
    private final int PORT = 8189;
    private Vector<ClientHandler>clients;
    private Vector<ArrayList<ClientHandler>> privates;
    private AuthService authService;
    private Timer t;
    public AuthService getAuthService() {
        return authService;
    }
//    private Vector<ClientsHandler> clients;

    public Server() {
//        Method[] meth = BaseAuthService.class.getDeclaredMethods();
//        for (Method o: meth) {
//            if (o.getName().equals("start")) {
//                try {
//                    o.invoke(getAuthService());
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();
        authService = new BaseAuthService();
        privates = new Vector<>();
        t = new Timer();

    authService.start();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server is up...");

            while (true) {
                socket = server.accept(); //режим ожидания, возвращает объект типа сокет, блокирует выполнение кода

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
                server.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void getPriivateConversation(ClientHandler c0,  String nick, String msg) {
//int i = 0;
//       for (ClientHandler c: clients) {
//           if (c.getName().equals(nick)) {
//               c.
//           c0.sendServerMessage(msg);
//           }
//       }
//    }
//    public void privateMulticast(String msg) {
//        for (int i = 0; i < privates.size(); i++) {
//            for (int j = 0; j < privates.get(i).size(); j++) {
//                privates.get(i).get(j).sendServerMessage(msg);
//
//            }
//        }
//    }
//    public void timerSchedule(ClientHandler c) {
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//
//
//
//        },120000);
//
//
//    }
public  synchronized void broadCast(String msg) {
        for (ClientHandler c:clients) {
            c.sendServerMessage(msg);
        }

}
public void broadCastUserList() {
        StringBuffer stg = new StringBuffer("/usersList");

        for (ClientHandler c:clients) {

            stg.append(" ").append(c.getName());

        }
        for (ClientHandler c:clients){
            c.sendServerMessage(stg.toString());
        }


}
public  ClientHandler match(String nick) {
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

//    public void broadcast(String msg){
//        for(ClientHandler c: clients){
//            c.sendMessage(msg);
//        }
//    }
//    public void subscribeMe(ClientsHandler c){
//        clients.add(c);
//    }
//    public void unsubscribeMe(ClientHandler c){
//        clients.remove(c);
//    }
//}





