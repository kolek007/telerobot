package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class JPanelOpenCV extends JPanel {
    BufferedImage image;

    public static void main(String args[]) throws InterruptedException {

        OpenCVClient client = new OpenCVClient("192.168.1.103", 4444);


//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        JPanelOpenCV t1 = new JPanelOpenCV();

        JPanelOpenCV t2 = new JPanelOpenCV();

        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        frame.getContentPane().add(mainPanel);
        mainPanel.add(t1);
        mainPanel.add(t2);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setTitle("Cams");
        frame.setLocation(0, 0);
        frame.setVisible(true);

        BufferedImage image1 = null, image2 = null;
        ObjectInputStream inputFromServer = null;
        try {
            inputFromServer = new ObjectInputStream(client.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {

            try {

                List<Byte> list = new ArrayList<>();
                list = (List<Byte>)inputFromServer.readUnshared();
                byte[] bytes = new byte[list.size()];
                int i = 0;
                for(Byte bt : list) {
                    bytes[i++] = bt;
                }
                image1 = ImageIO.read(new ByteArrayInputStream(bytes));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {

                List<Byte> list = new ArrayList<>();
                list = (List<Byte>)inputFromServer.readUnshared();
                byte[] bytes = new byte[list.size()];
                int i = 0;
                for(Byte bt : list) {
                    bytes[i++] = bt;
                }
                image2 = ImageIO.read(new ByteArrayInputStream(bytes));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



            t1.image = image1;
            t1.repaint();
            t2.image = image2;
            t2.repaint();
            t1.setPreferredSize(new Dimension(image1.getWidth(), image1.getHeight()));
            t2.setPreferredSize(new Dimension(image2.getWidth(), image2.getHeight()));
            frame.setSize(image1.getWidth() + image2.getWidth() + 30, Math.max(image1.getHeight(), image2.getHeight()) + 30);
        }

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public JPanelOpenCV() {
    }


}
