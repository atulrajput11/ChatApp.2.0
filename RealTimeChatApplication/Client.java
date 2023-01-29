package RealTimeChatApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Client()
    {
        try{
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        } catch (Exception e ){

        }
    }
    public void startReading()
    {
        //thead - read karke deta rahega
        Runnable r1 = () ->{
            System.out.println("reader startd...");
            while(true){
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat");
                        break;
                    }
                    System.out.println("Server : " + msg);
                } catch(Exception e){
                    e.printStackTrace();
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
            while(true){
                try{
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();


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
