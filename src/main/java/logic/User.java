package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;

public class User extends Player {

    public User(String name) {
        super(name);
    }

    @Override
    public void play() {
        Command.getInstance().showActionsLists();
    }

    @Override
    protected void toBeKilled() {
        discard(0);
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
