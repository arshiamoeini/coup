package gui;

import config.Configured;
import logic.eventhandler.SelectingHandler;
import models.Cart;
import models.Player;

import javax.swing.*;

public class CourtPanel implements PanelDesigner, Configured, ColoringCarts {
    private static CourtPanel instance;
    static {
        instance = new CourtPanel();
    }

    private JPanel columnOfCarts;
    private JPanel showingCart;
    private JPanel panel;

    public CourtPanel() {
        columnOfCarts.add(new JLabel(getImage("backward")));
    }

    public static CourtPanel getInstance() {
        return instance;
    }

    @Override
    public ImageIcon getImage(String name) {
        return Configured.getImage(name, PlayerHand.CART_WIDTH, PlayerHand.CART_HEIGHT);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public JPanel getFirstCart() {
        return columnOfCarts;
    }

    @Override
    public JPanel getSecondCart() {
        return showingCart;
    }

    public void addCartToShow(int index, String cartName) {
        (index == 0 ? columnOfCarts : showingCart).removeAll();
        (index == 0 ? columnOfCarts : showingCart).add(new JLabel(getImage(cartName)));
    }

    public void addMouseListener(int index, SelectingHandler selectingHandler) {
         PlayerHand.addHandler(index == 0 ? columnOfCarts : showingCart, selectingHandler);
    }

    public void unShowCarts() {
        columnOfCarts.removeAll();
        showingCart.removeAll();
        columnOfCarts.add(new JLabel(getImage("backward")));
    }

    public void removeAllHandler() {
        PlayerHand.removeAllMouseListener(columnOfCarts);
        PlayerHand.removeAllMouseListener(showingCart);
    }
}
