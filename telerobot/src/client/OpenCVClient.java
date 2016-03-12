package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Ivan on 27.02.2016.
 */
public class OpenCVClient {
    Socket socket = null;
    InputStream in = null;
    OutputStream out = null;


    public OpenCVClient(String address, int port) {
        try {
            System.out.println("Connecting to... " + address + ":" + port);
            socket = new Socket(address, port);
            System.out.println("Connected!");
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
