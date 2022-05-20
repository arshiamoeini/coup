package gui;

import config.Configured;
import models.Cart;

import javax.swing.*;

public class UserHand extends PlayerHand {
    public UserHand(String playerName) {
        super(playerName);
    }

    @Override
    public void addCart(String cartName) {
        addLabelCart(new JLabel(getImage(cartName)));
    }
}
