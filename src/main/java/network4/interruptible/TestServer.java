package network4.interruptible;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer implements Runnable {
    private JTextArea messages;

    @Override
    public void run() {
        try(ServerSocket s = new ServerSocket(8189)){
            while (true){
                Socket incoming = s.accept();
                Runnable r = new TestServerHandler(incoming);
                Thread t = new Thread(r);
                t.start();
            }
        }catch (IOException e){

            messages.append(" \n TestServer.run" +e);
        }
    }
}
