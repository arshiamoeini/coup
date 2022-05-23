package logic;

import models.Cart;
import models.Player;
import models.Result;

import java.util.ArrayList;

public class Paranoid extends Player {
    public Paranoid(String name) {
        super(name);
    }
    private boolean takeChallenge = true;

    @Override
    public Result play() {
        return earnMoney();
    }

    @Override
    protected void toBeKilled() {
        if (carts.size() == 0) return;
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
