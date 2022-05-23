package logic;

import models.Cart;
import models.Player;
import models.Result;

import java.util.ArrayList;
import java.util.Collections;

public class CautiousKiller extends Player {
    public CautiousKiller(String name) {
        super(name);
    }

    @Override
    public Result play() {
        if (carts.contains(Cart.ASSASSIN)) {
            if (coins < 3) {
                if (carts.contains(Cart.DUKE)) return taxCollection();
                return earnMoney();
            }
            for (int i = 0; i < 4; i++) if (i != getSeatNumber() && memory.getPlayer(i).isAlive()){
                return murder(i);
            }
        }
        if (coins >= 2) {
            for (Cart cart : carts) {
                if (cart == Cart.AMBASSADOR) {
                    return exchange();
                }
            }
        }
        return (coins > 0) ? exchangeCard() : receiveForeignAid();
    }

    @Override
    protected void toBeKilled() {
        if (carts.size() == 0) return;
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
