package gui;

import models.Memory;

import javax.swing.*;
import java.awt.*;

public class EventRecorderPanel extends JScrollPane {
    private static EventRecorderPanel instance;
    static {
        instance = new EventRecorderPanel();
    }

    private JPanel panel = new JPanel();
    private GridBagConstraints gbcFiller = new GridBagConstraints();
    private int rowCounter = 0;
    public EventRecorderPanel() {
        setSize(new Dimension(200, 300));
        panel.setLayout(new GridBagLayout());
        setViewportView(panel);
        gbcFiller.gridx = 0;
        gbcFiller.anchor = GridBagConstraints.NORTHWEST;
    }

    public static EventRecorderPanel getInstance() {
        return instance;
    }


    private void addLine(String event) {
        JLabel label = (new JLabel( (rowCounter + 1)+") "+event));
        label.setHorizontalAlignment(0);
        gbcFiller.gridy = (rowCounter++);
        panel.add(label, gbcFiller);
    }
    public void addSubjectiveAction(Memory.SubjectiveAction action) {
        addLine(action.getName()+": "+action.getDoerName());
    }
    public void addObjectiveAction(Memory.ObjectiveAction action) {
        addLine(action.getName()+": "+action.getDoerName()+" -> "+action.getObjectName());
    }
}
