package RealTimeChatApplication;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author Atul
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server extends JFrame{
    ServerSocket server;  // come from net package
    Socket socket;
    BufferedReader br; //come from io package
    PrintWriter out;
    private JLabel heading =new JLabel("Server Area");
    private JTextArea  messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);

    //Constructor
    public Server()
    {
        try {
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
//            startWriting();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10){
//                    System.out.println("you have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+ contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI(){
        // gui code...

        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);  // window in centre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("https://pngtree.com/so/message-icon"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame ka layout set karenge
        this.setLayout(new BorderLayout());
        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollpane=new JScrollPane(messageArea);
        this.add(jScrollpane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);



        this.setVisible(true);
    }


    public void startReading()
    {
       //thread - read karke deta rahega
        Runnable r1 = () ->{
            System.out.println("reader startd...");
           while(true){           // bar bar read karta rahega
                try {
                    String msg = br.readLine();  // massage read kane ke liye
                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    messageArea.append("Client : " + msg+"\n");
                } catch(Exception e){
//                    e.printStackTrace();   // console pe jo exception aati hai usse print karne ke liye
                    System.out.println("Connection Closed");
                    break;
                }
            }
        };
        new Thread(r1).start();  // pehle Thread class ka object banaoge useme runnable ka refrence r1 pass kerenge then start
    }
    public void startWriting()
    {
        //thread - data user lega and then send karega client tak
        Runnable r2 = () -> {
            System.out.println("writer started..");
            while(!socket.isClosed()){ // BAr bar write karne ke liye
            try{
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));      // Console se data lene ke liye matlab user se
                String content = br1.readLine(); // massage bhej ne liye

                out.println(content);
                out.flush();  // kabhi data nahi jata to flush kardiya
                if(content.equals("exit")){
                    socket.close();
                    break;
                }
               }   catch (Exception e){
                e.printStackTrace();
            }
           }


        };
        new Thread(r2).start(); // pehle Thread class ka object banaoge useme runnable ka refrence r2 pass kerenge then start
    }
    public static void main(String[] arg) {
        System.out.println("This is Server Going to Start.....");
        Server server = new Server();
    }
}
