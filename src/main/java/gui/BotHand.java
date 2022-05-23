package gui;

import config.Configured;
import logic.eventhandler.SelectingHandler;
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

    public void addSelector(SelectingHandler handler) {
        addHandler(panel, handler);
    }

    @Override
    public void removeAllHandler() {
        removeAllMouseListener(panel);
    }
}
