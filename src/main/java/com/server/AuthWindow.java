package com.server;
/*
 Creating auth window for user sign  up
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthWindow extends JFrame {
    JPanel contentPanel;
    JTextField login;
    JPasswordField password ;
    JButton signUp;

    public AuthWindow() throws HeadlessException {

        setTitle("Sign Up");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(400,400);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(50,100,0,100));
        setContentPane(contentPanel);
        setLocationRelativeTo(null);

        login = new JTextField();
        login.setBounds(100,50,120,30);

        contentPanel.add(login);
        login.setColumns(10);

        JLabel lbluserName = new JLabel("User Name/Login");
            lbluserName.setBounds(50,50,120,30);

            contentPanel.add(lbluserName);

        password = new JPasswordField();
        password.setBounds(74,100,100,20);
        contentPanel.add(password);
        password.setColumns(10);

        JLabel lblPassword  = new JLabel("Password");
        lblPassword.setBounds(75,205,80,15);
        contentPanel.add(lblPassword);

        signUp = new JButton("SignUp");
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String usr = login.getText().trim();
               String pass = password.getText();
                System.out.println(usr);
                System.out.println(pass);
               if (usr.equals("")||pass.equals("")) {
                   JOptionPane.showMessageDialog(null,"Name or passwort is wrong!!!","Error",JOptionPane.ERROR_MESSAGE);
                   login.setText("");
                   password.setText("");

               }
                if (!SQL.checkIfUserExistsInDb(usr)){
                   SQL.insertUserDataIntoTheTable(usr,pass);
                   JOptionPane.showMessageDialog(null,"Registration completed.","Success!",JOptionPane.INFORMATION_MESSAGE);
                    login.setText("");
                    password.setText("");

               }else if (SQL.checkIfUserExistsInDb(usr)) {
                    JOptionPane.showMessageDialog(null,"This username already exists!","Warning!",JOptionPane.WARNING_MESSAGE);
                    login.setText("");
                    password.setText("");

                }

            }
        });

        signUp.setBounds(130,160,90,20);
        signUp.setLocation(50,10);
        contentPanel.add(signUp);
        signUp.setLocation(25,25);


        setVisible(true);



    }
}
