package gui;

import config.Config;
import config.Configured;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Configured {

    private static final int MAIN_WIDTH;
    private static final int MAIN_HEIGHT;
    static {
        Config config = Configured.getConfig(GameFrame.class.getName());
        MAIN_WIDTH = config.getInt("width");
        MAIN_HEIGHT = config.getInt("height");
    }

    private JPanel pane;
    private JPanel firstSeat;
    private JPanel secondSeat;
    private JPanel thirdSeat;
    private JPanel forthSeat;
    private JPanel generalActions;
    private JPanel characterActions;
    private JPanel courtPanel;
    private JPanel eventRecorderPanel;
    private JList list1;

    public GameFrame() throws HeadlessException {
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
        setContentPane(pane);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void showActionsLists() {
        generalActions.add(ActionsList.getGeneralInstance());
        characterActions.add(ActionsList.getCharacterInstance());
    }

    public void setHand(JPanel playerHand, int index) {
        switch (index) {
            case 0:
                firstSeat.add(playerHand);
                break;
            case 1:
                secondSeat.add(playerHand);
                break;
            case 2:
                thirdSeat.add(playerHand);
                break;
            default:
                forthSeat.add(playerHand);
        }
    }
    public void update() {
        repaint();
        revalidate();
    }
}
