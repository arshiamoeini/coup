package logic;

import models.Cart;
import models.Memory;
import models.Player;
import models.Result;

import java.util.ArrayList;
import java.util.Arrays;

public class Cow extends Player {
    public Cow(String name) {
        super(name);
    }

    @Override
    public Result play() {
        if (carts.contains(Cart.CAPTAIN)) {
            int killer = memory.foundInstanceOf(CautiousKiller.class);
            if (killer != -1) {
                return corruption(killer);
            }
        }
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
