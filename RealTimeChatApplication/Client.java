package RealTimeChatApplication;
/**
*
*  @author Atul
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Declare components
    private JLabel heading =new JLabel("Client Area");
    private JTextArea  messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);

    public Client()
    {
        try{
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
              createGUI();
              handleEvents();
            startReading();
            //startWriting();
        } catch (Exception e ){

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
        this.setTitle("Client Messenger[END]");
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
            while(true){
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                        //System.out.println("Server : " + msg);
                        messageArea.append("Server : " + msg+"\n");
                } catch(Exception e){
                    //  e.printStackTrace();
                        System.out.println("Connection Closed");
                        break;
                }
            }
        };
        new Thread(r1).start();
    }
    public void startWriting()
    {
        //thread - data user lega and the send karega client tak
        Runnable r2 = () -> {
            System.out.println("writer started..");
            while(!socket.isClosed()){
                try{
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }   catch (Exception e){
                    e.printStackTrace();
                }
            }


        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Client....");
        new Client();
    }
}
