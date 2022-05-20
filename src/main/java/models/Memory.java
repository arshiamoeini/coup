package models;

import logic.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Memory {
    public static Player[] players = new Player[4];
    public static User user;

    public Player getPlayer(int index) {
        return players[index];
    }

    public Cart getClaimedCard() {
        return null;
    }

    public int numberOfAlivePlayer() {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            if (players[i].isAlive()) ++counter;
        }
        return counter;
    }

    public class Action {
        private int doer;

        public Action(int doer) {
            this.doer = doer;
        }
    }
    public enum ObjectiveActionType {
        COUP(true, "coup", user::coup),
        MURDER(true, "murder", user::murder),
        CORRUPTION(true, "corruption", user::corruption),
        EXCHANGE_CARD(true, "exchange card", user::exchangeCard),
        PREVENT_MURDER(false, "prevent murder", user::preventMurder),
        PREVENT_CORRUPTION(false, "prevent corruption", user::preventCorruption),
        PREVENT_RECEIVE_FOREIGN_AID(false, "prevent receive foreign aid", user::preventReceiveForeignAid),
        CHALLENGE(false, "challenge", user::challenge);

        private String message;
        private Function<Integer, Player.Result> function;
        private boolean action;

        ObjectiveActionType(boolean action, String message, Function<Integer, Player.Result> function) {
            this.action = action;
            this.message = message;
            this.function = function;
        }

        public String getMessage() {
            return message;
        }
        public Function<Integer, Player.Result> getFunction() {
            return function;
        }
        public boolean isAction() {
            return action;
        }
    }
    public class ObjectiveAction extends Action {
        private ObjectiveActionType type;
        private int object;

        public ObjectiveAction(int doer, int object, ObjectiveActionType type) {
            super(doer);
            this.object = object;
            this.type = type;
        }
    }

    public enum SubjectiveActionType {
        ERAN_MONEY("earn money", user::earnMoney),
        RECEIVE_FOREIGN_AID("receive foreign aid", user::receiveForeignAid),
        TAX_COLLECTION("tax collection", user::taxCollection),
        EXCHANGE("exchange", user::exchange);

        private String message;
        private Callable<Player.Result> function;

        SubjectiveActionType(String message, Callable<Player.Result> function) {
            this.message = message;
            this.function = function;
        }

        public String getMessage() {
            return message;
        }
        public Callable<Player.Result> getFunction() {
            return function;
        }
    }
    public class SubjectiveAction extends Action {
        private SubjectiveActionType type;

        public SubjectiveAction(int doer, SubjectiveActionType type) {
            super(doer);
            this.type = type;
        }
    }

    private ArrayList<Action> memory = new ArrayList<>();
    public Memory() {
    }

    public void memorizePlayer(Player player, int playerSeat) {
        if (players[playerSeat] != null) {
            System.out.println("error same seat");
            throw new RuntimeException();
        } else {
            if (player instanceof User) user = (User) player;
            players[playerSeat] = player;
        }
    }

    public void memorizeEarnMoney(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.ERAN_MONEY));
    }
    public void memorizeReceiveForeignAid(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.RECEIVE_FOREIGN_AID));
    }
    public void memorizeExchangeCard(int player, int cartIndex) {
        memory.add(new ObjectiveAction(player, cartIndex, ObjectiveActionType.EXCHANGE_CARD));
    }
    public void memorizeCoup(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.COUP));
    }

    public void memorizeTaxCollection(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.TAX_COLLECTION));
    }
    public void memorizeMurder(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.MURDER));
    }
    public void memorizeCorruption(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.CORRUPTION));
    }
    public void memorizeExchange(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.EXCHANGE));
    }

    public void memorizePreventMurder(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.PREVENT_MURDER));
    }
    public void memorizePreventCorruption(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.PREVENT_CORRUPTION));
    }
    public void memorizePreventReceiveForeignAid(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.PREVENT_RECEIVE_FOREIGN_AID));
    }

    public void memorizeChallenge(int player, int object) {
        memory.add(new ObjectiveAction(player, object, ObjectiveActionType.CHALLENGE));
    }
}
