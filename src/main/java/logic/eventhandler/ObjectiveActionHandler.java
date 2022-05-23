package logic.eventhandler;

import logic.Command;
import models.Player;
import models.Result;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class ObjectiveActionHandler implements ActionListener {
    private Function<Integer, Result> action;
    public ObjectiveActionHandler(Function<Integer, Result> action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Command.getInstance().handelObjectiveAction(action);
    }
}
