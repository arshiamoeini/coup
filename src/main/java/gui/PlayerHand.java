package gui;

import config.Configured;
import logic.eventhandler.SelectingHandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseListener;

public abstract class PlayerHand implements PanelDesigner, Configured {
    protected JPanel panel;
    private JLabel playerName;
    private JPanel coins;
    protected JPanel firstCart1;
    protected JPanel secondCart1;
    protected JPanel firstCart;
    protected JPanel secondCart;


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
        label.setForeground(Color.RED);
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
        panel.repaint();
        panel.revalidate();
    }

    public void removeAll() {
        firstCart.removeAll();
        secondCart.removeAll();
    }
    public abstract void removeAllHandler();
    public static void removeAllMouseListener(JPanel panel) {
        for (MouseListener listener: panel.getMouseListeners()) {
            panel.removeMouseListener(listener);
        }
    }
    //BOARD PAINT METHODS
    private void paintBorder(Color color) {
        panel.setBorder(new LineBorder(color, 3));
       // panel.setBorder();
       // Border border = new Li
        //TODO
    }
    public void paintBorderGreen() {
        paintBorder(Color.GREEN);
    }
    public void paintBorderYellow() {
        paintBorder(Color.YELLOW);
    }
    public void paintBorderRed() {
        paintBorder(Color.RED);
    }
    public void paintBorderBlue() {
        paintBorder(Color.BLUE);
    }
    public void paintBorderOrange() {
        paintBorder(Color.ORANGE);
    }


    public void unColorBoard() {
        panel.setBorder(null);
    }

    //HANDLER ADDER METHODS
    public static void addHandler(JPanel panel, SelectingHandler handler) {
        handler.setSelectable(panel);
        panel.addMouseListener(handler);
    }
}
