package models;

public enum Cart {
    DUKE(Memory.SubjectiveActionType.TAX_COLLECTION, Memory.ObjectiveActionType.PREVENT_RECEIVE_FOREIGN_AID),
    CAPTAIN(Memory.ObjectiveActionType.CORRUPTION, Memory.ObjectiveActionType.PREVENT_CORRUPTION),
    AMBASSADOR(Memory.SubjectiveActionType.EXCHANGE, Memory.ObjectiveActionType.PREVENT_CORRUPTION),
    ASSASSIN(Memory.ObjectiveActionType.MURDER, null),
    CONTESSA(null, Memory.ObjectiveActionType.PREVENT_MURDER);

    private Object action;
    private Memory.ObjectiveActionType reaction;

    Cart(Object action, Memory.ObjectiveActionType reaction) {
        this.action = action;
        this.reaction = reaction;
    }

    public String getName() {
        return this.name().toLowerCase();
    }

    public Object getAction() {
        return action;
    }

    public Memory.ObjectiveActionType getReaction() {
        return reaction;
    }
}
