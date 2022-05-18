package models;

import config.Config;
import config.Configured;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Player implements Configured {
    private static Config config = Configured.getConfig(Player.class.getName());
    private static Memory memory = new Memory();
    int seatNumber; //from 0 to 3

    String name;
    private ArrayList<Cart> carts = new ArrayList<>();
    private ArrayList<Cart> deadCarts = new ArrayList<>();

    private int coins = 2;

    public Player(String name, Cart firstCart, Cart secondCart) {
        this.seatNumber = config.getInt(name) - 1; //turn to 0-base
        this.name = name;
        carts.add(firstCart);
        carts.add(secondCart);

        memory.memorizePlayer(this, seatNumber);
    }

    public void tryToPlay() {
        //TODO
        play();
        next().tryToPlay();
    }
    private boolean isAlive() {
        return !carts.isEmpty();
    }
    public abstract void play();

    public Player next() {
        return memory.getPlayer((seatNumber + 1) % 4);
    }

    // general action methods
    // two type of action: () -> boolean , int -> boolean
    public boolean earnMoney() {
        memory.memorizeEarnMoney(seatNumber);
        ++coins;
        return true;
    }
    public boolean receiveForeignAid() {
        memory.memorizeReceiveForeignAid(seatNumber);
        if (askForReactionWithReceiveForeignAid()) {
            coins += 2;
            return true;
        }
        return false;
    }
    public boolean exchangeCard(int cardIndex) {
        if (coins < 1) return false;
        memory.memorizeExchangeCard(seatNumber);
        --coins;
        Cart cart = takeOneCartToExchange();
        Court.getInstance().returnCarts(Arrays.asList(cart));
        return true;
    }
    public boolean coup(int playerIndex) {
        if (coins < 7) return false;
        memory.memorizeCoup(seatNumber, playerIndex);
        coins -= 7;
        memory.getPlayer(playerIndex).toBeKilled();
        return true;
    }

    // character action methods
    public boolean taxCollection() {
        if (askForChallenge() && askForReactionWithReceiveForeignAid()) {
            memory.memorizeTaxCollection(seatNumber);
            coins += 3;
            return true;
        }
        return false;
    }
    public boolean murder(int playerIndex) {
        if (coins < 3) return  false;
        coins -= 3;
        memory.memorizeMurder(seatNumber, playerIndex);
        if (askForChallenge() && askForReactionWithMurder()) {
            memory.getPlayer(playerIndex).toBeKilled();
            return true;
        }
        return false;
    }

    public boolean corruption(int playerIndex) {
        //ki challenge ki reaction
        if (askForChallenge() && askForReactionWithCorruption()) {
            memory.memorizeCorruption(seatNumber, playerIndex);
            coins += memory.getPlayer(playerIndex).toBeRansom();
            return true;
        }
        return false;
    }

    public boolean exchange() {
        if (coins < 2) return false;
        if (askForChallenge()) {
            memory.memorizeExchange(seatNumber);
            coins -= 2;
            ArrayList<Cart> carts = Court.getTwoCart();
            Court.getInstance().returnCarts(changeCart(carts));
            return true;
        }
        return false;
    }

    // reaction methods
    public boolean preventMurder(int playerIndex) {
        memory.memorizePreventMurder(seatNumber, playerIndex);
        return askForChallenge();
    }
    public boolean preventCorruption(int playerIndex) {
        memory.memorizePreventCorruption(seatNumber, playerIndex);
        return askForChallenge();
    }
    public boolean preventReceiveForeignAid(int playerIndex) {
        memory.memorizePreventReceiveForeignAid(seatNumber, playerIndex);
        return askForChallenge();
    }

    // risky method
    public boolean challenge(int playerIndex) {
        if (prove(memory.getClaimedCard())) {
            toBeKilled();
            return false;
        } else {
            memory.getPlayer(playerIndex).toBeKilled();
            return true;
        }
    }

    // operational methods
    private boolean askForChallenge() {
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.decisionToChallenge()) {
                //it will challenge
                if (player.challenge(seatNumber)) {
                    return false;
                } else {
                    return true;
                }
            }
            player = player.next();
        }
        return true;
    }
    private boolean askForReactionWithMurder() {
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.decisionToPreventMurder(seatNumber)) {
                //it will challenge
                if (player.preventReceiveForeignAid(seatNumber)) {
                    return false;
                } else {
                    return true;
                }
            }
            player = player.next();
        }
        return true;
    }
    private boolean askForReactionWithReceiveForeignAid() {
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.decisionToPreventReceiveForeignAid(seatNumber)) {
                //it will challenge
                if (player.preventMurder(seatNumber)) {
                    return false;
                } else {
                    return true;
                }
            }
            player = player.next();
        }
        return true;
    }
    private boolean askForReactionWithCorruption() {
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.decisionToPreventCorruption(seatNumber)) {
                //it will challenge
                if (player.preventCorruption(seatNumber)) {
                    return false;
                } else {
                    return true;
                }
            }
            player = player.next();
        }
        return true;
    }

    private boolean prove(Cart claimedCard) {
        return false;
    }
    private int toBeRansom() {
        return 0;
    }
    protected abstract void toBeKilled();

    protected abstract boolean decisionToPreventMurder(int playerIndex);
    protected abstract boolean decisionToPreventReceiveForeignAid(int playerIndex);
    protected abstract boolean decisionToPreventCorruption(int playerIndex);

    protected abstract boolean decisionToChallenge();
    protected abstract Cart takeOneCartToExchange();
    protected abstract ArrayList<Cart> changeCart(ArrayList<Cart> newCarts);

}
