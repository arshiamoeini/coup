package gui;

import logic.Command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionsList extends JPanel {
    private static ActionsList generalInstance;
    private static ActionsList characterInstance;

    public ActionsList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public static ActionsList getGeneralInstance() {
        return generalInstance;
    }
    public static ActionsList getCharacterInstance() {
        return characterInstance;
    }
    public static ActionsList getOkButtonForExchange() {
        return new ActionsList(){
            {
                this.addAction("Done", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Command.getInstance().removeCourtHandler();
                        Command.getInstance().removeUserCartHandler();
                        Command.getInstance().unColorSelectedCart();
                        Command.getInstance().getUser().doneExchange();
                        CourtPanel.getInstance().unShowCarts();
                        Command.getInstance().closeActionLists();
                        Command.getInstance().update();
                        Command.getInstance().takeUserTurn();
                    }
                });
            }
        };
    }

    public static void construct() {
        generalInstance = new ActionsList();
        characterInstance = new ActionsList();
    }

    public void addAction(String name, ActionListener handler) {
        JButton button = new JButton(name);
        button.addActionListener(handler);
        this.add(button);
    }
}
