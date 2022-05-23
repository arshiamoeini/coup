package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public interface ColoringCarts {
    JPanel getFirstCart();
    JPanel getSecondCart();

    default void colorHand(int index) {
        unColor();
        if (index == 0) colorPanel(getFirstCart());
        if (index == 1) colorPanel(getSecondCart());
    }
    default void unColor() {
        getFirstCart().setBorder(null);
        getSecondCart().setBorder(null);
    }

    default void colorPanel(JPanel panel) {
        panel.setBorder(new LineBorder(Color.BLUE, 3));
    }
}
