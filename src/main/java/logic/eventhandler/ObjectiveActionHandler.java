package logic.eventhandler;

import logic.Command;
import models.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class ObjectiveActionHandler implements ActionListener {
    private Function<Integer, Player.Result> action;
    private boolean playerSelector;
    public ObjectiveActionHandler(Function<Integer, Player.Result> action, boolean playerSelector) {
        this.action = action;
        this.playerSelector = playerSelector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Command.getInstance().setSelectedAction(action);
        if (playerSelector) {
            Command.getInstance().addPlayerSelector();
        } else {
            Command.getInstance().addCartSelectorForExchangeCart();
        }
        Command.getInstance().handelObjectiveAction(action);
    }
}
