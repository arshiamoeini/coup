package gui;

import logic.eventhandler.SubjectiveActionHandler;

import javax.swing.*;
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
