package logic.eventhandler;

import logic.Command;
import models.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class SubjectiveActionHandler implements ActionListener {
    private Callable<Player.Result> action;
    // for function like this Command.getInstance().getUser()::earnMoney;

    public SubjectiveActionHandler(Callable<Player.Result> action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Command.getInstance().handelSubjectiveAction(action);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
