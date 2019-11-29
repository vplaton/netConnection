package pro.vplaton;


import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.*;

public class Main {

    public static final String APPLICATION_NAME = "Connection Internet";
    private static Integer cnt = 0, val = 0;

    public static void main(String[] args) {
       /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
       });*/

        createGUI();
    }

    private static void createGUI() {
        /*JFrame frame = new JFrame(APPLICATION_NAME);
        frame.setMinimumSize(new Dimension(300, 200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);*/

        try {
            setTrayIcon();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void setTrayIcon() {
        URL imageURL;
        TrayIcon trayIcon = null;

        if(! SystemTray.isSupported() ) {
            return;
        }

        System.out.println("Вошли");

        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayMenu.add(item);

        //URL imageURL = Main.class.getResource("/images/icon32x32.png");
        imageURL = Main.class.getResource("icon32x32.png");

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        // Image icon = new ImageIcon(imageURL, "Tray icon").getImage();
        trayIcon = new TrayIcon(icon, APPLICATION_NAME, trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        trayIcon.displayMessage(APPLICATION_NAME, "Application started!", TrayIcon.MessageType.INFO);

        TrayIcon finalTrayIcon = trayIcon;
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        if(netIsAvailable()){
                            if(cnt != 1) {
                                URL newImageURL = Main.class.getResource("succes-icon32x32.png");
                                finalTrayIcon.setImage(Toolkit.getDefaultToolkit().getImage(newImageURL));
                                cnt = 1;
                            }
                        }else{
                            if(cnt != 0) {
                                URL newImageURL = Main.class.getResource("error-icon32x32.png");
                                finalTrayIcon.setImage(Toolkit.getDefaultToolkit().getImage(newImageURL));
                                cnt = 0;
                                //System.out.println("no 1");
                            }

                            val++;
                            if(val.equals(1)){
                                URL newImageURL = Main.class.getResource("error-icon32x32.png");
                                finalTrayIcon.setImage(Toolkit.getDefaultToolkit().getImage(newImageURL));
                                //System.out.println("no 2");
                            }

                            AudioListener audioListener = new AudioListener();
                            try {
                                audioListener.soundPlay();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                        Thread.sleep(2500); //1000 - 1 сек
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        run.start(); // заводим
    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

}
