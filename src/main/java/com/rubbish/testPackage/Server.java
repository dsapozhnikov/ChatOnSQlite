package com.rubbish.testPackage;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private final int PORT = 8189;
    private Vector<ClientHandler> clients;
    public Server(){
        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();
        try{
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен, ждем клиентов");
            while(true){
                socket = server.accept(); //режим ожидания, возвращает объект типа сокет, блокирует выполнение кода
//                clients.add(new ClientHandler(socket, this));
                subscribeMe(new ClientHandler(socket, this));
                System.out.println("Клиент подключился");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Не удалось запустить сервер");
        }finally{
            try{
                socket.close();
                server.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void broadcast(String msg){
        for(ClientHandler c: clients){
            c.sendMessage(msg);
        }
    }
    public void subscribeMe(ClientHandler c){
        clients.add(c);
    }
    public void unsubscribeMe(ClientHandler c){
        clients.remove(c);
    }
}
