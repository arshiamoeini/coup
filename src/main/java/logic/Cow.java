package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;

public class Cow extends Player {
    public Cow(String name) {
        super(name);
    }

    @Override
    public void play() {
        earnMoney();
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
    protected int takeOneCartToExchangeIndex() {
        return 0;
    }

    @Override
    protected ArrayList<Cart> changeCart(ArrayList<Cart> newCarts) {
        return null;
    }
}
