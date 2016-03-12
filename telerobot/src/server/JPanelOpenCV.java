package server;

import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;


public class JPanelOpenCV extends JPanel{
    BufferedImage image;

    public static void main (String args[]) throws InterruptedException{
        OpenCVServer server = new OpenCVServer(4444);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        JPanelOpenCV t1 = new JPanelOpenCV();
        JPanelOpenCV t2 = new JPanelOpenCV();
        VideoCapture camera0 = new VideoCapture(0);
        VideoCapture camera1 = new VideoCapture(1);
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

        Mat mat0 = new Mat();
        Mat mat1 = new Mat();
        camera0.read(mat0);
        camera1.read(mat1);

        if(!camera0.isOpened() || !camera1.isOpened()){
            System.out.println("Error");
        }
        else {
            BufferedImage image1 = null, image2 = null;
            ObjectOutputStream outputToClient = null;
            while(true){
                if (camera0.read(mat0)){
                    image1 = t1.MatToBufferedImage(mat0);
                    t1.image = image1;
                    t1.repaint();
                }
                if (camera1.read(mat1)){
                    image2 = t2.MatToBufferedImage(mat1);
                    t2.image = image2;
                    t2.repaint();
                }
                t1.setPreferredSize(new Dimension(image1.getWidth(), image1.getHeight()));
                t2.setPreferredSize(new Dimension(image2.getWidth(), image2.getHeight()));
                frame.setSize(image1.getWidth() + image2.getWidth() + 30, Math.max(image1.getHeight(), image2.getHeight()) + 30);
                if(server.out != null) {
                    try {

                        if(outputToClient == null) {
                            try {
                                outputToClient = new ObjectOutputStream(server.out);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        List<Byte> list = new ArrayList<>();
                        ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
                        ImageIO.write(image1, "JPG", bScrn);
                        byte imgBytes[] = bScrn.toByteArray();
                        bScrn.close();
                        for(byte bt : imgBytes) {
                            list.add(bt);
                        }
                        outputToClient.writeUnshared(list);
                        outputToClient.flush();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {

                        List<Byte> list = new ArrayList<>();
                        ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
                        ImageIO.write(image2, "JPG", bScrn);
                        byte imgBytes[] = bScrn.toByteArray();
                        bScrn.close();
                        for(byte bt : imgBytes) {
                            list.add(bt);
                        }
                        outputToClient.writeUnshared(list);
                        outputToClient.flush();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        camera0.release();
        camera1.release();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public JPanelOpenCV() {
    }

    public JPanelOpenCV(BufferedImage img) {
        image = img;
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

}
