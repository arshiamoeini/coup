package gui;

import javax.swing.*;

public class PlayerHand implements PanelDesigner {
    private JPanel panel;
    private JLabel PlayerName;
    private JPanel coins;
    private JPanel secondCart;
    private JPanel firstCart;

    public PlayerHand() {

    }
    //public void update(int coins, )

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
