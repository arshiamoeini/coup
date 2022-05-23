package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;
import java.util.Collections;

public class CautiousKiller extends Player {
    public CautiousKiller(String name) {
        super(name);
    }

    @Override
    public void play() {
        for (Cart cart: carts) {
            if (cart == Cart.AMBASSADOR) {
                exchange();
            }
        }
        if (coins > 0) exchangeCard();
        else receiveForeignAid();
    }

    @Override
    protected void toBeKilled() {
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i) != Cart.AMBASSADOR && carts.get(i) != Cart.ASSASSIN) {
                discard(i);
                return;
            }
        }
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i) != Cart.ASSASSIN) {
                discard(i);
                return;
            }
        }
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
        int killer = -1;
        for (int i = 0; i < newCarts.size(); i++) {
            if (newCarts.get(i) == Cart.ASSASSIN) {
                killer = i;
            }
        }
        if (killer == -1) return newCarts;
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i) == Cart.AMBASSADOR) {
                swap(newCarts, killer, i);
                break;
            }
        }
        return newCarts;
    }
}
