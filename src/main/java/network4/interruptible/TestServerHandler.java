package network4.interruptible;

import javax.swing.*;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TestServerHandler implements Runnable {
    private Socket incoming;
    private int counter;
    private JTextArea messages;

    public TestServerHandler(Socket incoming) {
        this.incoming = incoming;
    }

    @Override
    public void run() {
        try{
            try{
                OutputStream outStream = incoming.getOutputStream();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(outStream, "UTF-8"),
                        true);
                while (counter <100){
                    counter++;
                    if(counter <=10) out.println(counter);
                    Thread.sleep(100);
                }
            }finally {
                incoming.close();

                messages.append("Closing server \n");
            }
        }catch (Exception e){
            messages.append("\n TestServerHandler.run: " +e);
        }
    }
}
