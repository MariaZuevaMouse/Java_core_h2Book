package network4.interruptible;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class InterruptibleSocketFrame extends JFrame {
    private Scanner in;
    private JButton interruptibleButton;
    private JButton blockingButton;
    private JButton cancelButton;
    private JTextArea messages;
    private TestServer server;
    private  Thread connectThread;

    public InterruptibleSocketFrame() throws HeadlessException {
        JPanel northPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);

        final int TEXT_ROWS = 20;
        final int TEXT_COLUMNS = 60;
        messages = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        add(new JScrollPane(messages));

        interruptibleButton = new JButton(" Interruptible");
        blockingButton = new JButton("Blocking");

        northPanel.add(interruptibleButton);
        northPanel.add(blockingButton);

        interruptibleButton.addActionListener(event->{
            interruptibleButton.setEnabled(false);
            blockingButton.setEnabled(false);
            cancelButton.setEnabled(true);
            connectThread = new Thread(()->{
                try{
                    connectInterruptibly();
                }catch (IOException e){
                    messages.append("\n InterruptibleSocketTest" +
                            ".connectInterrupible: " +e);
                }
            });
            connectThread.start();
        });
        blockingButton.addActionListener(event->{
            interruptibleButton.setEnabled(false);
            blockingButton.setEnabled(false);
            cancelButton.setEnabled(true);
            connectThread = new Thread(()->{
                try{
                    connectBlocking();
                }catch (IOException e){
                    messages.append("\n InterruptibleSocketTest" +
                            ".connectBlocking: " +e);
                }
            });
            connectThread.start();
        });
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        northPanel.add(cancelButton);
        cancelButton.addActionListener(event ->{
            connectThread.interrupt();
            cancelButton.setEnabled(false);
        });
        server = new TestServer();
        new Thread(server).start();
        pack();
    }

    private void connectBlocking() throws IOException{
        messages.append("Blocking: \n");
        try(Socket socket = new Socket("localhost", 8189)){
            in = new Scanner(socket.getInputStream(), "UTF-8");
            while(!Thread.currentThread().isInterrupted()){
                messages.append("Reading ");
                if(in.hasNextLine()){
                    String line = in.nextLine();
                    messages.append(line);
                    messages.append("\n");
                }
            }
        }finally {
            EventQueue.invokeLater(()->{
                messages.append("Socket closed\n");
                interruptibleButton.setEnabled(true);
                blockingButton.setEnabled(true);
            });
        }
    }

    private void connectInterruptibly() throws IOException {
        messages.append("Interruptible: \n");
        try(SocketChannel channel = SocketChannel.open(
                new InetSocketAddress("localhost", 8189))){
            in = new Scanner(channel, "UTF-8");
            while(!Thread.currentThread().isInterrupted()){
                messages.append("Reading ");
                if(in.hasNextLine()){
                    String line = in.nextLine();
                    messages.append(line);
                    messages.append("\n");
                }
            }
        }finally {
            EventQueue.invokeLater(()->{
                messages.append("Channel closed\n");
                interruptibleButton.setEnabled(true);
                blockingButton.setEnabled(true);
            });
        }
    }
}
