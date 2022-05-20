package models;

import config.Config;
import config.Configured;
import gui.ActionsList;
import logic.Command;
import logic.User;

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

    public Player(String name) {
        this.seatNumber = config.getInt(name) - 1; //turn to 0-base
        this.name = name;

        memory.memorizePlayer(this, seatNumber);
    }

    public void tryToPlay() {
        if (!isAlive()) next().tryToPlay(); //continue

        if (memory.numberOfAlivePlayer() == 1) {
            Command.getInstance().showWiner(this);
        } else {
            Command.getInstance().showPlayer(seatNumber);
            play();
            if (! (this instanceof User)) next().tryToPlay(); //wait for command system
        }
    }
    protected boolean isAlive() {
        return !carts.isEmpty();
    }
    public abstract void play();

    public Player next() {
        return memory.getPlayer((seatNumber + 1) % 4);
    }

    public int getSeatNumber() {
        return seatNumber;
    }
    public int getCoins() {
        return coins;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Cart> getCarts() {
        return carts;
    }
    public ArrayList<Cart> getDeadCarts() {
        return deadCarts;
    }

    public void addCart(Cart cart) {
        carts.add(cart);
    }

    public enum Result {
        CAN_NOT,
        UNSUCCESSFUL,
        SUCCESSFUL,
    }
    // general action methods
    // two type of action: () -> boolean , int -> boolean
    public Result earnMoney() {
        memory.memorizeEarnMoney(seatNumber);
        ++coins;
        return Result.SUCCESSFUL;
    }
    public Result receiveForeignAid() {
        memory.memorizeReceiveForeignAid(seatNumber);
        if (askForReactionWithReceiveForeignAid()) {
            coins += 2;
            return Result.SUCCESSFUL;
        }
        return Result.UNSUCCESSFUL;
    }
    public Result exchangeCard(int cartIndex) {
        if (coins < 1) return Result.CAN_NOT;
        memory.memorizeExchangeCard(seatNumber, cartIndex);
        --coins;
        Cart cart = takeOneCartToExchange();
        Court.getInstance().returnCarts(Arrays.asList(cart));
        return Result.SUCCESSFUL;
    }
    public Result coup(int playerIndex) {
        if (coins < 7) return Result.CAN_NOT;
        memory.memorizeCoup(seatNumber, playerIndex);
        coins -= 7;
        memory.getPlayer(playerIndex).toBeKilled();
        return Result.SUCCESSFUL;
    }

    // character action methods
    public Result taxCollection() {
        if (askForChallenge() && askForReactionWithReceiveForeignAid()) {
            memory.memorizeTaxCollection(seatNumber);
            coins += 3;
            return Result.SUCCESSFUL;
        }
        return Result.UNSUCCESSFUL;
    }
    public Result murder(int playerIndex) {
        if (coins < 3) return Result.CAN_NOT;
        coins -= 3;
        memory.memorizeMurder(seatNumber, playerIndex);
        if (askForChallenge() && askForReactionWithMurder()) {
            memory.getPlayer(playerIndex).toBeKilled();
            return Result.SUCCESSFUL;
        }
        return Result.UNSUCCESSFUL; //its lost turn
    }

    public Result corruption(int playerIndex) {
        //ki challenge ki reaction
        if (askForChallenge() && askForReactionWithCorruption()) {
            memory.memorizeCorruption(seatNumber, playerIndex);
            coins += memory.getPlayer(playerIndex).toBeRansom();
            return Result.SUCCESSFUL;
        }
        return Result.UNSUCCESSFUL;
    }

    public Result exchange() {
        if (coins < 2) return Result.CAN_NOT;
        if (askForChallenge()) {
            memory.memorizeExchange(seatNumber);
            coins -= 2;
            ArrayList<Cart> carts = Court.getTwoCart();
            Court.getInstance().returnCarts(changeCart(carts));
            return Result.SUCCESSFUL;
        }
        return Result.UNSUCCESSFUL;
    }

    // reaction methods
    public Result preventMurder(int playerIndex) {
        if (!isAlive()) return Result.CAN_NOT;

        memory.memorizePreventMurder(seatNumber, playerIndex);
        return (askForChallenge() ? Result.SUCCESSFUL : Result.UNSUCCESSFUL);
    }
    public Result preventCorruption(int playerIndex) {
        if (!isAlive()) return Result.CAN_NOT;

        memory.memorizePreventCorruption(seatNumber, playerIndex);
        return (askForChallenge() ? Result.SUCCESSFUL : Result.UNSUCCESSFUL);
    }
    public Result preventReceiveForeignAid(int playerIndex) {
        if (!isAlive()) return Result.CAN_NOT;

        memory.memorizePreventReceiveForeignAid(seatNumber, playerIndex);
        return (askForChallenge() ? Result.SUCCESSFUL : Result.UNSUCCESSFUL);
    }

    // risky method
    public Result challenge(int playerIndex) {
        if (!isAlive()) return Result.CAN_NOT;

        if (prove(memory.getClaimedCard())) {
            toBeKilled();
            return Result.UNSUCCESSFUL;
        } else {
            memory.getPlayer(playerIndex).toBeKilled();
            return Result.SUCCESSFUL;
        }
    }

    // operational methods
    private boolean askForChallenge() {
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.decisionToChallenge()) {
                //it will challenge
                if (player.challenge(seatNumber) == Result.SUCCESSFUL) {
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
                if (player.preventReceiveForeignAid(seatNumber) == Result.SUCCESSFUL) {
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
                if (player.preventMurder(seatNumber) == Result.SUCCESSFUL) {
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
                if (player.preventCorruption(seatNumber) == Result.SUCCESSFUL) {
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
    protected void discard(int index) {
        deadCarts.add(carts.remove(index));
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
