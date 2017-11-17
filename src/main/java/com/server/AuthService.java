package com.server;


public interface AuthService {

    void start();                      //enabling auth service for handling database connections
    String getNickByLoginPass(String login, String pass);
    void stop();

}
