package models;

import config.Config;
import config.Configured;
import logic.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Court implements Configured {
    public static Court instance;


    private ArrayList<Cart> carts = new ArrayList<>();
    public Court(Player player) {
        for (int t = 0;t < 3;++t) {
            //three time foreach cart
            Collections.addAll(carts, Cart.values());
        }

        //config two cart for each player str -> cart
        Config config = Configured.getConfig(Court.class.getName());
        for (int t = 0; t < 4; t++) {
            player.addCart(config.getCart(player.getName()+" first cart"));
            player.addCart(config.getCart(player.getName()+" second cart"));
            player = player.next();
        }
    }

    public static void splitCarts(Player player) {
        //get for take his cart
        instance = new Court(player);
    }
    public static Court getInstance() {
        return instance;
    }

    public ArrayList<Cart> getTwoCart() {
        shuffle();
        return new ArrayList<>(Arrays.asList(carts.remove(0), carts.remove(0)));
    }

    public void returnCarts(List<Cart> returned) {
        carts.addAll(returned);
    }

    public void shuffle() {
        Collections.shuffle(carts);
    }

    public Cart getACart() {
        shuffle();
        return carts.remove(0);
    }
}
