package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OpenCVServer extends Thread{
    InputStream in = null;
    OutputStream out = null;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    boolean run = true;

    public OpenCVServer(int portNum) {
        super();
        System.out.println("Welcome to Server side");
        // create server socket
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port " + portNum);
            System.exit(-1);
        }
        setDaemon(true);
        start();


    }

    @Override
    public void run() {
        while (run) {
            if(clientSocket == null) {
                try {
                    System.out.print("Waiting for a client...");
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected");
                    in = clientSocket.getInputStream();
                    out = clientSocket.getOutputStream();
                } catch (IOException e) {
                    System.out.println("Can't accept");
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                run = false;
                break;
            }
        }
    }

    public void disconnect() {
        try {
            run = false;
            interrupt();
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }



}