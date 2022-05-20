package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;

public class CautiousKiller extends Player {
    public CautiousKiller(String name) {
        super(name);
    }

    @Override
    public void play() {

    }

    @Override
    protected void toBeKilled() {

    }

    @Override
    protected boolean decisionToPreventMurder(int playerIndex) {
        return false;
    }

    @Override
    protected boolean decisionToPreventReceiveForeignAid(int playerIndex) {
        return false;
    }

    @Override
    protected boolean decisionToPreventCorruption(int playerIndex) {
        return false;
    }

    @Override
    protected boolean decisionToChallenge() {
        return false;
    }

    @Override
    protected Cart takeOneCartToExchange() {
        return null;
    }

    @Override
    protected ArrayList<Cart> changeCart(ArrayList<Cart> newCarts) {
        return null;
    }
}
