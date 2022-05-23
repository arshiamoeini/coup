package logic;

import models.Cart;
import models.Player;

import java.util.ArrayList;

public class Revolutionary  extends Player {

    public Revolutionary(String name) {
        super(name);
    }
    @Override
    public void play() {
        if (coins < 7)  taxCollection();
        else {
            for (int i = 0; i < 4; i++)  if (i != getSeatNumber()){
                if (memory.getPlayer(i).isAlive()) {
                    coup(i);
                }
            }
        }
    }

    @Override
    protected void toBeKilled() {
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i) != Cart.DUKE) {
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
        return null;
    }
}
