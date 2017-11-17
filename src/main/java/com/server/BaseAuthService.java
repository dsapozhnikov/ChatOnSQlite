package com.server;



import java.util.ArrayList;

public class BaseAuthService implements AuthService { // creating service for authentication

    private class Entry {
        private String login;
        private String pass;
        private String nick;


        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }
    private ArrayList<Entry>entries;

    @Override
    public void start() {
        SQL.connect();



    }


    @Override
    public String getNickByLoginPass(String login, String pass) {       // simply making login==nickname :)
        for (Entry e:entries) {
            if (e.login.equals(login) && e.pass.equals(pass)) {
                return e.nick;
            }

        }return null;
    }

    @Override
    public void stop() {
        SQL.disconnect();

    }


}
