package gui;

import config.Config;
import config.Configured;

import javax.swing.*;
import java.awt.*;

public class Manual extends JFrame implements Configured {
    private static Manual instance;
    private static final int MAIN_WIDTH;
    private static final int MAIN_HEIGHT;
    static {
        Config config = Configured.getConfig(Manual.class.getName());
        MAIN_WIDTH = config.getInt("width");
        MAIN_HEIGHT = config.getInt("height");
        instance = new Manual();
    }
    private JPanel panel;
    private JLabel manual1;
    private JLabel manual2;

    public Manual() throws HeadlessException {
        //setting
        setSize(MAIN_WIDTH, MAIN_HEIGHT);
        setResizable(false);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        Rectangle mainScreen = gd[0].getDefaultConfiguration().getBounds();
        int upLeftCornerX = mainScreen.x + mainScreen.width / 2 - MAIN_WIDTH / 2;
        int upLeftCornerY = mainScreen.y + mainScreen.height / 2 - MAIN_HEIGHT / 2;
        setLocation(upLeftCornerX, upLeftCornerY);


        setLayout(null);
        setContentPane(panel);
        manual1.setIcon(getImage("manual1"));
        manual2.setIcon(getImage("manual2"));

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    public static Manual getInstance() {
        return instance;
    }

    @Override
    public ImageIcon getImage(String name) {
        return Configured.getImage(name, MAIN_WIDTH, MAIN_HEIGHT);
    }
}
