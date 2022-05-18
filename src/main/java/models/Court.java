package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Court {
    public static Court instance;

    static {
        instance = new Court();
    }

    private ArrayList<Cart> carts = new ArrayList<>();
    public Court() {
        for (int t = 0;t < 3;++t) {
            Collections.addAll(carts, Cart.values());
        }
        //TODO read from json
        //config two cart for each player str -> cart
    }

    public static Court getInstance() {
        return instance;
    }

    public static ArrayList<Cart> getTwoCart() {
        return null;
    }

    public void returnCarts(List<Cart> returned) {
        carts.addAll(returned);
    }

    public void shuffle() {
        Collections.shuffle(carts);
    }
}
