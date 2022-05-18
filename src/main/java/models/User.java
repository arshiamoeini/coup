package models;

import java.util.ArrayList;

public class User extends Player {

    public User(String name, Cart firstCart, Cart secondCart) {
        super(name, firstCart, secondCart);
    }

    @Override
    public void play() {
        this.earnMoney();
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
