package models;

import java.util.ArrayList;

public class Memory {
    public static Player[] players = new Player[4];

    public Player getPlayer(int index) {
        return players[index];
    }

    public Cart getClaimedCard() {
        return null;
    }

    public class Action {
        private int doer;

        public Action(int doer) {
            this.doer = doer;
        }
    }
    public enum ObjectiveActionType {
        COUP,
        MURDER,
        CORRUPTION,
        PREVENT_MURDER,
        PREVENT_CORRUPTION,
        PREVENT_RECEIVE_FOREIGN_AID,
        CHALLENGE,
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
        ERAN_MONEY,
        RECEIVE_FOREIGN_AID,
        EXCHANGE_CARD,
        TAX_COLLECTION,
        EXCHANGE,
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
            players[playerSeat] = player;
        }
    }

    public void memorizeEarnMoney(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.ERAN_MONEY));
    }
    public void memorizeReceiveForeignAid(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.RECEIVE_FOREIGN_AID));
    }
    public void memorizeExchangeCard(int player) {
        memory.add(new SubjectiveAction(player, SubjectiveActionType.EXCHANGE_CARD));
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
