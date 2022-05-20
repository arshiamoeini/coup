package models;

import javax.swing.*;

public enum Cart {
    DUKE,
    CAPTAIN,
    AMBASSADOR,
    ASSASSIN,
    CONTESSA;

    Cart() {
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}
