package gui;

import config.Configured;
import logic.eventhandler.SelectingHandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseListener;

public abstract class PlayerHand implements PanelDesigner, Configured {
    private JPanel panel;
    private JLabel playerName;
    private JPanel coins;
    private JPanel secondCart;
    private JPanel firstCart;

    public static int WIDTH = 160;
    public static int HEIGHT = 140;
    public static int CART_WIDTH = 60;
    public static int CART_HEIGHT = 100;
    public PlayerHand(String playerName) {
        this.playerName.setText(playerName);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public ImageIcon getImage(String name) {
        return Configured.getImage(name, CART_WIDTH, CART_HEIGHT);
    }

    public void setCoins(int coins) {
        this.coins.removeAll();
        this.coins.add(new JLabel(String.valueOf(coins)));
    }
    protected void addLabelCart(JLabel label) {
        if (firstCart.getComponents().length == 0) {
            firstCart.add(label);
        } else {
            secondCart.add(label);
        }
    }
    public void addDeadCart(String cartName) {
        Image image = getImage(cartName).getImage();
        JLabel label = new JLabel() {
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, null);
                super.paintComponent(g);
            }
        };
        label.setOpaque(false);
        label.setText("dead");
        label.setBackground(new Color(145, 133, 132, 90));
        addLabelCart(label);
    }
    public abstract void addCart(String cartName);
    //public void update(int coins, )

    @Override
    public JPanel getPanel() {
        return panel;
    }

    public void update() {

    }

    public void removeAll() {
        panel.setBorder(null);
        firstCart.removeAll();
        secondCart.removeAll();
    }
    private void paintBorder(Color color) {
        panel.setBorder(new LineBorder(color, 3));
    }
    public void paintBorderGreen() {
        paintBorder(Color.GREEN);
    }
    public void paintBorderYellow() {
        paintBorder(Color.YELLOW);
    }

    public void addSelectorForCart(int index, SelectingHandler handler) {
        addHandler((index == 0 ? firstCart : secondCart) , handler);
    }
    public void addSelector(SelectingHandler handler) {
        addHandler(panel, handler);
    }
    private void addHandler(JPanel panel, SelectingHandler handler) {
        handler.setSelectable(panel);
        panel.addMouseListener(handler);

    }

    public void removeAllHandler() {
        removeAllMouseListener(panel);
        removeAllMouseListener(firstCart);
        removeAllMouseListener(secondCart);
    }
    public static void removeAllMouseListener(JPanel panel) {
        for (MouseListener listener: panel.getMouseListeners()) {
            panel.removeMouseListener(listener);
        }
    }
}
