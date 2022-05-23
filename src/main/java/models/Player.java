package models;

import config.Config;
import config.Configured;
import logic.Command;
import logic.User;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public abstract class Player implements Configured {
    private static Config config = Configured.getConfig(Player.class.getName());
    protected static Memory memory = new Memory();
    int seatNumber; //from 0 to 3

    String name;
    protected ArrayList<Cart> carts = new ArrayList<>();
    private ArrayList<Cart> deadCarts = new ArrayList<>();

    protected int coins = 2;

    public Player(String name) {
        this.seatNumber = config.getInt(name) - 1; //turn to 0-base
        this.name = name;

        memory.memorizePlayer(this, seatNumber);
    }

    public void tryToPlay() {
        if (!isAlive()) {
            next().tryToPlay();
            return;
        }

        if (memory.numberOfAlivePlayer() == 1) {
            Command.getInstance().update();
            Command.getInstance().showWiner(this);
            return; //Done calling
        }

        Command.getInstance().unColor();
        Command.getInstance().showPlayer(seatNumber);
        Command.getInstance().update();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        play();

        if (! (this instanceof User)) {
            next().tryToPlay(); //wait for command system
            return;
        }
    }
    public boolean isAlive() {
        return !carts.isEmpty();
    }
    public abstract void play();

    public Player next() {
        return memory.getPlayer((seatNumber + 1) % 4);
    }

    public int getSeatNumber() {
        return seatNumber;
    }
    public int getCoins() { return coins; }
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
        return Result.PREVENTED;
    }
    public Result exchangeCard() {
        if (coins < 1) return Result.CAN_NOT(1);
        memory.memorizeExchangeCard(seatNumber);
        --coins;
        Cart cart = carts.remove(takeOneCartToExchangeIndex());
        Court.getInstance().returnCarts(Arrays.asList(cart));
        carts.add(Court.getInstance().getACart());
        return Result.SUCCESSFUL;
    }
    public Result coup(int playerIndex) {
        if (coins < 7) return Result.CAN_NOT(7 - coins);

        Command.getInstance().showTarget(playerIndex);
        memory.memorizeCoup(seatNumber, playerIndex);

        coins -= 7;
        memory.getPlayer(playerIndex).toBeKilled();
        return Result.SUCCESSFUL;
    }

    // character action methods
    public Result taxCollection() {
        memory.memorizeTaxCollection(seatNumber);
        if (askForChallenge()) {
            coins += 3;
            return Result.SUCCESSFUL;
        }
        return Result.DEFEAT_CHALLENGE;
    }
    public Result murder(int playerIndex) {
        if (coins < 3) return Result.CAN_NOT(3 - coins);

        Command.getInstance().showTarget(playerIndex);
        memory.memorizeMurder(seatNumber, playerIndex);

        if (askForChallenge()) {
            coins -= 3;
            if (askForReactionWithMurder()) {
                memory.getPlayer(playerIndex).toBeKilled();
                return Result.SUCCESSFUL;
            }
            return Result.PREVENTED;
        }
        return Result.DEFEAT_CHALLENGE; //its lost turn
    }

    public Result corruption(int playerIndex) {
        Command.getInstance().showTarget(playerIndex);
        memory.memorizeCorruption(seatNumber, playerIndex);

        if (askForChallenge()) {
            if (askForReactionWithCorruption()) {
                coins += memory.getPlayer(playerIndex).toBeRansom();
                return Result.SUCCESSFUL;
            }
            return Result.PREVENTED;
        }
        return Result.DEFEAT_CHALLENGE;
    }

    public Result exchange() {
        if (coins < 2) return Result.CAN_NOT(2 - coins);

        memory.memorizeExchange(seatNumber);

        if (askForChallenge()) {
            coins -= 2;
            ArrayList<Cart> carts = Court.getInstance().getTwoCart();
            Court.getInstance().returnCarts(changeCart(carts));
            return Result.SUCCESSFUL;
        }
        return Result.DEFEAT_CHALLENGE;
    }

    // reaction methods
    public Result preventMurder(int playerIndex) {
        Command.getInstance().showPreventGuy(seatNumber);
        memory.memorizePreventMurder(seatNumber, playerIndex);
        boolean tmp = askForChallenge();

        Command.getInstance().unColorPlayer(seatNumber);
        return (tmp ? Result.SUCCESSFUL : Result.DEFEAT_CHALLENGE);
    }
    public Result preventCorruption(int playerIndex) {
        Command.getInstance().showPreventGuy(seatNumber);
        memory.memorizePreventCorruption(seatNumber, playerIndex);
        boolean tmp = askForChallenge();

        Command.getInstance().unColorPlayer(seatNumber);
        return (tmp ? Result.SUCCESSFUL : Result.DEFEAT_CHALLENGE);
    }
    public Result preventReceiveForeignAid(int playerIndex) {
        Command.getInstance().showPreventGuy(seatNumber);
        memory.memorizePreventReceiveForeignAid(seatNumber, playerIndex);
        boolean tmp = askForChallenge();

        Command.getInstance().unColorPlayer(seatNumber);
        return (tmp ? Result.SUCCESSFUL : Result.DEFEAT_CHALLENGE);
    }

    // risky method
    public Result challenge(int playerIndex) {
        Command.getInstance().showChallengeGuy(seatNumber);
        memory.memorizeChallenge(seatNumber, playerIndex);

        if (prove(memory.getClaimedCard())) {
            toBeKilled();
            Command.getInstance().unColorPlayer(seatNumber);
            return new Result(Result.Type.UNSUCCESSFUL, "make wrong!");
        } else {
            memory.getPlayer(playerIndex).toBeKilled();
            Command.getInstance().unColorPlayer(seatNumber);
            return new Result(Result.Type.SUCCESSFUL, "good guss");
        }
    }

    // operational methods
    protected boolean askForChallenge() {
        Command.getInstance().update();
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.isAlive() && player.decisionToChallenge()) {
                //it will challenge
                if (player.challenge(seatNumber).isSuccessful()) {
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
        Command.getInstance().update();
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.isAlive() && player.decisionToPreventMurder(seatNumber)) {
                //it will challenge
                if (player.preventMurder(seatNumber).isSuccessful()) {
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
        Command.getInstance().update();
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.isAlive() && player.decisionToPreventReceiveForeignAid(seatNumber)) {
                //it will challenge
                if (player.preventReceiveForeignAid(seatNumber).isSuccessful()) {
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
        Command.getInstance().update();
        Player player = this.next();
        for (int t = 0;t < 3;++t) {
            if(player.isAlive() && player.decisionToPreventCorruption(seatNumber)) {
                //it will challenge
                if (player.preventCorruption(seatNumber).isSuccessful()) {
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
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i) == claimedCard) return true;
        }
        return false;
    }
    protected void discard(int index) {
        deadCarts.add(carts.remove(index));
    }
    private int toBeRansom() {
        int pay = Math.min(2, coins);
        coins -= pay;
        return pay;
    }
    protected abstract void toBeKilled();

    protected abstract boolean decisionToPreventMurder(int playerIndex);
    protected abstract boolean decisionToPreventReceiveForeignAid(int playerIndex);
    protected abstract boolean decisionToPreventCorruption(int playerIndex);

    protected abstract boolean decisionToChallenge();
    protected abstract int takeOneCartToExchangeIndex();
    protected abstract ArrayList<Cart> changeCart(ArrayList<Cart> newCarts);

    protected void swap(ArrayList<Cart> newCarts, int cartFromNewCartsIndex, int cartFromCartsWantToChange) {
        newCarts.set(cartFromNewCartsIndex,
                carts.set(cartFromCartsWantToChange, newCarts.get(cartFromNewCartsIndex)));
    }
}
