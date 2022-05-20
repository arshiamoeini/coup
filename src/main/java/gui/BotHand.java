package gui;

import config.Configured;
import models.Cart;

import javax.swing.*;

public class BotHand extends PlayerHand {
    public BotHand(String playerName) {
        super(playerName);
    }

    @Override
    public void addCart(String cartName) {
        addLabelCart(new JLabel(getImage("backward")));
    }
}
