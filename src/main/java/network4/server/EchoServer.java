package network4.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*
 * switch on access telnet in control panel
 * open cmd
 * telnet 127.0.0.1 8189
 * */

public class EchoServer {
    public static void main(String[] args)throws IOException {
        try(ServerSocket server = new ServerSocket(8189)){
            try(Socket incoming = server.accept()){
                InputStream inputStream = incoming.getInputStream();
                OutputStream outputStream = incoming.getOutputStream();

                try(Scanner in = new Scanner(inputStream, "UTF-8")){
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(outputStream,"UTF-8"), true);
                    out.println("Hello! Enter BYE to exit");

                    boolean done = false;
                    while (!done && in.hasNextLine()){
                        String line = in.nextLine();
                        out.println("Echo: " +line);
                        if(line.trim().equals("BYE")) done =true;
                    }
                }
            }
        }
    }
}
