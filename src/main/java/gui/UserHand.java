package gui;

import logic.eventhandler.SelectingHandler;

import javax.swing.*;

public class UserHand extends PlayerHand implements ColoringCarts {
    public UserHand(String playerName) {
        super(playerName);
    }

    @Override
    public void addCart(String cartName) {
        addLabelCart(new JLabel(getImage(cartName)));
    }


    public void addSelectorForCart(int index, SelectingHandler handler) {
        addHandler((index == 0 ? firstCart : secondCart) , handler);
    }

    public void removeAllHandler() {
        removeAllMouseListener(firstCart);
        removeAllMouseListener(secondCart);
    }

    @Override
    public JPanel getFirstCart() {
        return firstCart;
    }

    @Override
    public JPanel getSecondCart() {
        return secondCart;
    }
}
