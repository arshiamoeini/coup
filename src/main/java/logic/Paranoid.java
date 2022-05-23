package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;

public class Paranoid extends Player {
    public Paranoid(String name) {
        super(name);
    }
    private boolean takeChallenge = true;

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
        takeChallenge = !takeChallenge;
        return takeChallenge;
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
