package com.server;


import java.sql.*;

public class MainTet {
    static Connection connection;
    static Statement statement;
    static PreparedStatement prfstmt;

    public static void main(String[] args) {
        connect();
        String l = "andrey12";
        String p = "pass12";
        System.out.println(checkIfUserExistsInDb(l, p));

        insertUserDataIntoTheTable(l, p);
    }

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            try {
                connection = DriverManager.getConnection("JDBC:sqlite:database3.db");
                statement = connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE  " +
                    "IF NOT EXISTS users (\n " +
                    "id integer primary key autoincrement, \n" +
                    "login STRING unique, \n " +
                    "password STRING  unique);");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void insertUserDataIntoTheTable(String login, String password) {
        String query = "INSERT INTO users(login, password) VALUES(?,?)";
        try {
            PreparedStatement prstmt = connection.prepareStatement("INSERT INTO users(login, password) VALUES(?,?)");
            prstmt.setString(1, login);
            prstmt.setString(2, password);
            prstmt.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
        }
    }

    public static boolean checkIfUserExistsInDb(String login, String password) {
        try {
//            try {
//                Class.forName("org.sqlite.JDBC");
//                connection = DriverManager.getConnection("JDBC:sqlite:database3.db");
//            } catch (ClassNotFoundException | SQLException e) {
//                e.printStackTrace();
//            }

            prfstmt = connection.prepareStatement("SELECT * from users WHERE login = ?");
            prfstmt.setString(1, login);

            ResultSet rs = prfstmt.executeQuery();
            while (rs.next()) {
                if (login.equals(rs.getString("login"))) {
                    return true;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;

    }
}

