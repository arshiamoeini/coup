package logic;

import gui.CourtPanel;
import models.Cart;
import models.Court;
import models.Player;
import models.Result;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class User extends Player {
    private ArrayList<Cart> newCarts;
    private int selectedCartFromHandForExchangeIndex;
    private int selectedCartFromCourtForExchangeIndex;

    public User(String name) {
        super(name);
    }

    @Override
    public void play() {
        Command.getInstance().showActionsLists();
    }

    @Override
    protected void toBeKilled() {
        if (carts.size() == 1) {
            discard(0);
            Command.getInstance().confirmUser("you lost!");
            return;
        }
       discard(Command.getInstance().addCartSelectorToDiscard());
    }
    private void handelSelectedCartForDiscard(int index) {
        Command.getInstance().removeAllHandler();
        discard(index);
    }

    @Override
    protected boolean decisionToPreventMurder(int playerIndex) {
        //TODO
        return Command.getInstance().askYesNoQuestionWithMessage("do you prevent murder!");
    }

    @Override
    protected boolean decisionToPreventReceiveForeignAid(int playerIndex) {
        return Command.getInstance().askYesNoQuestionWithMessage("do you prevent receive aid!");
    }

    @Override
    protected boolean decisionToPreventCorruption(int playerIndex) {
        return Command.getInstance().askYesNoQuestionWithMessage("do prevent corruption!");
    }

    @Override
    protected boolean decisionToChallenge() {
        return Command.getInstance().askYesNoQuestionWithMessage("Challenge");
    }

    @Override
    public Result exchangeCard() {
        if (coins < 1) return Result.CAN_NOT(1);

        memory.memorizeExchangeCard(getSeatNumber());
        --coins;
        Command.getInstance().closeActionLists();
        takeOneCartToExchangeIndex();

        return Result.NOT_COMPLETE;
    }
    public void handelSelectedCartForExchange(int index) {
        Cart cart = carts.remove(index);
        Court.getInstance().returnCarts(Arrays.asList(cart));
        carts.add(Court.getInstance().getACart());
        Command.getInstance().handelActionResult(Result.SUCCESSFUL);
    }

    @Override
    protected int takeOneCartToExchangeIndex() {
        Command.getInstance().addCartSelectorToUser(this::handelSelectedCartForExchange);
        Command.getInstance().update();
        return -1;
    }


    @Override
    public Result exchange() {
        if (coins < 2) return Result.CAN_NOT(2 - coins);

        memory.memorizeExchange(getSeatNumber());

        if (askForChallenge()) {
            Command.getInstance().closeActionLists();
            coins -= 2;
            ArrayList<Cart> carts = Court.getInstance().getTwoCart();
            changeCart(carts);
            Command.getInstance().addOkButtonForExchange();
            return Result.NOT_COMPLETE;
        }
        return Result.DEFEAT_CHALLENGE;
    }


    private void handelSelectedCartFromHandForExchange(int index) {
        selectedCartFromHandForExchangeIndex = index;
        Command.getInstance().removeUserCartHandler();
        Command.getInstance().colorUserHand(index);
        if (selectedCartFromCourtForExchangeIndex != -1) {
            swap(newCarts, selectedCartFromCourtForExchangeIndex, selectedCartFromHandForExchangeIndex);
            changeCart(newCarts);
        }
    }
    private void handelSelectedCartFromCourtForExchange(int index) {
        selectedCartFromCourtForExchangeIndex = index;
        Command.getInstance().removeCourtHandler();
        Command.getInstance().colorCourt(index);
        if (selectedCartFromHandForExchangeIndex != -1) {
            swap(newCarts, selectedCartFromCourtForExchangeIndex, selectedCartFromHandForExchangeIndex);
            changeCart(newCarts);
        }
    }
    @Override
    protected ArrayList<Cart> changeCart(ArrayList<Cart> newCarts) {
        this.newCarts = newCarts;
        selectedCartFromHandForExchangeIndex = -1;
        selectedCartFromCourtForExchangeIndex = -1;
        Command.getInstance().unColorSelectedCart();
        Command.getInstance().removeCourtHandler();
        Command.getInstance().removeUserCartHandler();
        Command.getInstance().update();
        Command.getInstance().addCartSelectorToUser(this::handelSelectedCartFromHandForExchange);
        Command.getInstance().addCartSelectorToCourt(newCarts, this::handelSelectedCartFromCourtForExchange);
        return null;
    }

    public void doneExchange() {
        Court.getInstance().returnCarts(newCarts);
    }
}
