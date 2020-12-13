package network4.threaded;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/*
 * switch on access telnet in control panel
 * open cmd
 * telnet 127.0.0.1 8189
 * */

public class ThreadedEchoServer {
    public static void main(String[] args) {
        try(ServerSocket s = new ServerSocket(8189)){
            int i = 1;
            while(true){
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
class ThreadedEchoHandler implements Runnable{
    private Socket incoming;

    public ThreadedEchoHandler(Socket incoming) {
        this.incoming = incoming;
    }

    @Override
    public void run() {
        try(InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream()){
            Scanner in  = new Scanner(inStream, "UTF-8");
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(outStream, "UTF-8"), true
            );
            out.println("Hello! Enter BYE to exit");

            boolean done = false;
            while(!done && in.hasNextLine()){
                String line = in.nextLine();
                out.println("Echo: " + line);
                if(line.trim().equals("BYE"))
                    done =true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
