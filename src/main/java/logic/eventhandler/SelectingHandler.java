package logic.eventhandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SelectingHandler implements MouseListener {
    private JPanel selectable;
    protected int index;

    public SelectingHandler(int index) {
        this.index = index;
    }

    public void setSelectable(JPanel selectable) {
        this.selectable = selectable;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        unColor();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        selectable.setBorder(new LineBorder(new Color(89, 86, 86), 3));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        unColor();
    }

    protected void unColor() {
        selectable.setBorder(null);
    }
}
