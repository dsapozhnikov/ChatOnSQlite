package com.client;


import com.server.AuthWindow;
import sun.misc.JavaNetAccess;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ChatWindow extends JFrame {

    private Socket socket;
   private DataInputStream in;
    private DataOutputStream out;
    private JPanel upperPanel;
    private JPanel nickpanel;
    private boolean isAuthorized;
    private  JTextArea inputArea;
    private JScrollPane  scrollInputPane;
    private JButton jb1;
    private TextArea jtaUsers;
    private JScrollPane jtsUsers;
    public static void jOptionPopUp(boolean b) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        if (b)JOptionPane.showMessageDialog(dialog,"Success!","Login successful",JOptionPane.INFORMATION_MESSAGE);
        if (!b)JOptionPane.showMessageDialog(dialog,"Login or passwort is incorrect!","invalid input!",JOptionPane.WARNING_MESSAGE);

    }
    public void setAuthorized(boolean b) {           // setting panel visible/invisible depending on whther user is authorized
        isAuthorized = b;
        upperPanel.setVisible(!isAuthorized);
        inputArea.setVisible(isAuthorized);
        scrollInputPane.setVisible(isAuthorized);
        jb1.setVisible(isAuthorized);
        jtsUsers.setVisible(isAuthorized);

    }
    public ChatWindow() {                       // creating chat window on swing ( creating all the interface elements

        setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700,700);


        setLocationRelativeTo(null);
        setResizable(true);
        jtaUsers = new TextArea();
        jtaUsers.setPreferredSize(new Dimension(175,1));
        jtaUsers.setEditable(false);
        jtsUsers = new JScrollPane(jtaUsers);
        jtaUsers.setBackground(Color.WHITE);
        JLabel jlName = new JLabel();




        JPanel jpanel = new JPanel();                      //enabling scrolling by adding scroll panel
        inputArea = new JTextArea();
        scrollInputPane = new JScrollPane(inputArea);

        upperPanel = new JPanel(new GridLayout(1,1));
        nickpanel = new JPanel();

        JTextField login = new JTextField();                    // adding login panel
        JPasswordField passwordField = new JPasswordField();
        JButton auth = new JButton("Login");

        auth.addActionListener(new ActionListener() {              // assigning action listener to password field
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.writeUTF(login.getText()+" "+passwordField.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        upperPanel.add(login);
        upperPanel.add(passwordField);
        upperPanel.add(auth);
        nickpanel.add(jlName,BorderLayout.CENTER);
//        u.add(jlName);

        add(upperPanel,BorderLayout.NORTH);
        add(nickpanel,BorderLayout.AFTER_LINE_ENDS);

        add(jtsUsers,BorderLayout.EAST);


        Font font = new Font("TimesNewRoman",Font.PLAIN,25);
        Font font1 = new Font("TimesNewRoman",Font.PLAIN,50);

        jtsUsers.setFont(font1);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.getLineWrap();
        inputArea.getLineWrap();
        ImageIcon imageIcon = new ImageIcon("123.png");

        JScrollPane scrollOutputPane= new JScrollPane(textArea);
         jb1 = new JButton();
        jb1.setIcon(imageIcon);
//        jb1.setBackground(Color.WHITE);
  //      jb1.setOpaque(true);

        JMenuBar jMenuBar = new JMenuBar();        // adding menu bar (NOT FUNCTIONAL YET!) p.s will be in the next release :)
        JMenu mSignUp = new JMenu("SignUp");
        JMenu mFile = new JMenu("File");
        JMenu mEdit = new JMenu("Edit");
        JMenuItem fileNew = new JMenuItem("New");

        mFile.setPreferredSize(new Dimension(40,30));
        mEdit.setPreferredSize(new Dimension(40,30));
        fileNew.setPreferredSize(new Dimension(60,20));
        JMenuItem exitItem = new JMenuItem("Exit");
        setJMenuBar(jMenuBar);
        jMenuBar.add(mFile);
        jMenuBar.add(mEdit);
        jMenuBar.add(mSignUp);
        mSignUp.setLocation(0,10);
        mFile.add(fileNew);
        mFile.addSeparator();
        mFile.add(exitItem);
        mSignUp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AuthWindow authWindow =new AuthWindow();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // textArea.append(inputArea.getText()+"\n");
                String msg  = inputArea.getText().trim();
                sendMessage(msg);
                inputArea.grabFocus();
            }
        });

        inputArea.setFont(font);
        textArea.setFont(font);

            try {                                                 // establishing connection with server
                socket=new Socket("localhost",8189);
                in =new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());



        inputArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {                 // enabling key ENTER also functional for sending messages
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                   // textArea.append(inputArea.getText()+"\n");
                    String msg  = inputArea.getText().trim();
                    sendMessage(msg);

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        scrollInputPane.setPreferredSize(new Dimension(500,40));

//        inputArea.setPreferredSize(new Dimension(500,50));

        jb1.setPreferredSize(new Dimension(75,40));
//        jb1.setPreferredSize(new Dimension(100,100));
        jpanel.setLocation(500,500);


        jpanel.add(scrollInputPane, BorderLayout.CENTER);
        jpanel.add(jb1);
        jpanel.setOpaque(false);

        add(scrollOutputPane,BorderLayout.CENTER);

        add(jpanel,BorderLayout.SOUTH);
        Thread t1 =new Thread(() ->{                 // creating a thread for handling timeout option and authentication option (if not auth in 2 mins , timeout happens)

                try {
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("Timeout")) {
                            System.exit(0);
                            break;
                        }
                        if (msg.startsWith("/authok ")) {
                            String nick = msg.split("\\s")[1];
                            jlName.setText(nick);
                            setAuthorized(true);
                            break;}
                        textArea.append(msg+"\n");
                        textArea.setCaretPosition(textArea.getDocument().getLength());

                    }

                    while (true) {
                        String msg  =  in.readUTF();
                        if (msg.startsWith("/")) {                  // separating service messaages by determining them with "/" sign
                            if (msg.startsWith("/usersList")) {
                                String[] users = msg.split("\\s");
                                jtaUsers.setText("");
                                for (int i = 1; i < users.length; i++) {
                                    jtaUsers.append(users[i] + "\n");

                                }

                            }
                        }

                      else textArea.append(msg+"\n");                      // appending a user message to a textarea
                        textArea.setCaretPosition(textArea.getDocument().getLength());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    setAuthorized(false);
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        });
        t1.setDaemon(true);
        t1.start();
            } catch (IOException e) {
                e.printStackTrace();
                setAuthorized(false);
            }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    super.windowClosing(e);
                    out.writeUTF("/end");             //closing connection by sending "end" message and closing socket itself
                    socket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    setAuthorized(false);
                }
            }
        });setAuthorized(false);

        setVisible(true);

    }
    public void sendMessage(String msg) {      //enabling sending messages to server

        try {
            out.writeUTF(msg);

            inputArea.setText("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
