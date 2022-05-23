package logic;

import gui.*;
import logic.eventhandler.ObjectiveActionHandler;
import logic.eventhandler.SelectingHandler;
import logic.eventhandler.SubjectiveActionHandler;
import models.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class Command {
    private static Command instance;
    static {
        instance = new Command();
    }

    private GameFrame gameFrame = new GameFrame();
    private PlayerHand[] playerHands = new PlayerHand[4];
    User user;
    private Function<Integer, Result> selectedAction;

    public Command() {
        user = new User("ali");
        Memory.setUser(user);
   //     Memory.SubjectiveActionType tr = Memory.SubjectiveActionType.TAX_COLLECTION;
        new Paranoid("paranoid");
        new Revolutionary("revolutionary");
        new CautiousKiller("cautious killer");
        new Cow("cow");
        Court.splitCarts(Memory.players[0]);

        madePanelHands();
        showHands();
        gameFrame.update();
    }

    //BOARD COLORING METHODS
    public void showWiner(Player player) {
        playerHands[player.getSeatNumber()].paintBorderYellow();
    }

    public void showPlayer(int seatNumber) {
        playerHands[seatNumber].paintBorderGreen();
    }
    public void showTarget(int seatNumber) {
        playerHands[seatNumber].paintBorderRed();
    }
    public void showPreventGuy(int seatNumber) {
        playerHands[seatNumber].paintBorderBlue();
    }
    public void showChallengeGuy(int seatNumber) {
        playerHands[seatNumber].paintBorderOrange();
    }
    public void unColorPlayer(int seatNumber) {
        playerHands[seatNumber].unColorBoard();
    }
    public void unColor() {
        for (int i = 0; i < 4; i++) {
            unColorPlayer(i);
        }
    }

    private PlayerHand getHand(Player player) {
        return playerHands[player.getSeatNumber()];
    }
    private void madePanelHands() {
    //    playerHands[user.getSeatNumber()] = new UserHand(user.getName());
    //    gameFrame.setHand(getHand(user).getPanel(), 0);
        Player player = Memory.players[user.getSeatNumber()];
        for (int t = 0; t < 4; t++) {
            playerHands[player.getSeatNumber()] = (player instanceof User ?
                    new UserHand(player.getName()) : new BotHand(player.getName()));
            gameFrame.setHand(playerHands[player.getSeatNumber()].getPanel(), t);
            player = player.next();
        }
    }
    private void showHands() {
        Player player = Memory.players[0];
        for (int t = 0;t < 4;++t) {
            showHandFor(player);
            player = player.next();
        }
    }
    private void showHandFor(Player player) {
        PlayerHand playerHand = getHand(player);
        playerHand.setCoins(player.getCoins());
        playerHand.removeAll();
        for (Cart cart: player.getCarts()) {
            playerHand.addCart(cart.getName());
        }
        for (Cart cart: player.getDeadCarts()) {
            playerHand.addDeadCart(cart.getName());
        }
    }

    public static Command getInstance() {
        return instance;
    }
    public static void start() {
        Memory.players[0].tryToPlay();
    }

    public User getUser() {
        return user;
    }


    public void update() {
        showHands();
        gameFrame.update();
        for (int i = 0; i < 4; i++) {
            playerHands[i].update();
        }
    }
    public void updateHands() {
        showHands();
        gameFrame.update();
    }
    public void removeAllHandler() {
        //it will make cart hand like real state and remove all mouse listener for selecting modes
        for (int i = 0; i < 4; i++) {
            playerHands[i].removeAllHandler();
        }
    }
    public void removeUserCartHandler() {
        getHand(user).removeAllHandler();
    }
    public void removeCourtHandler() {
        CourtPanel.getInstance().removeAllHandler();
    }
    public void showActionsLists() {
        if (ActionsList.getCharacterInstance() == null) {
            ActionsList.construct();
            for (Memory.SubjectiveActionType type: Memory.SubjectiveActionType.values()) {
                addGeneralAction(type);
            }
            for (Memory.ObjectiveActionType type: Memory.ObjectiveActionType.values()) {
                if (type.isAction()) {
                    addCharacterAction(type);
                }
            }
        }
        gameFrame.showActionsLists();
        update();
    }
    public void closeActionLists() {
        gameFrame.closeActionLists();
    }
    public void addGeneralAction(Memory.SubjectiveActionType type) {
        ActionsList.getGeneralInstance().addAction(
                type.getMessage(),
                new SubjectiveActionHandler(type.getFunction()));
    }
    public void addCharacterAction(Memory.ObjectiveActionType type) {
        ActionsList.getCharacterInstance().addAction(
                type.getMessage(),
                new ObjectiveActionHandler(type.getFunction()));
    }

    public void handelSubjectiveAction(Callable<Result> action) throws Exception {
        Result result = action.call();
        if (result.isNotComplete()) return;
        handelActionResult(result);
    }
    public void handelObjectiveAction(Function<Integer, Result> action) {
        this.selectedAction = action;
        addPlayerSelector();
    }
    public void handelActionResult(Result result) {
        switch (result.getType()) {
            case CAN_NOT:
                confirmUser(result.getMessage());
                break;
            case UNSUCCESSFUL:
                confirmUser(result.getMessage());
                takeUserTurn();
                break;
            case SUCCESSFUL:
                takeUserTurn();
        }
        gameFrame.update();
    }

    public void takeUserTurn() {
        removeAllHandler();
        closeActionLists();
        user.next().tryToPlay(); //continue for other bots
    }

    /*
    private void handelUnsuccessful() {
        JOptionPane.showMessageDialog(gameFrame.getPanel(), "unsuccessful!");
    }
    private void handelCanNot() {
        JOptionPane.showMessageDialog(gameFrame.getPanel(), "can't do it!");
    }*/

    public void addCartSelectorToUser(Consumer<Integer> action) {
        for (int t = 0; t < user.getCarts().size(); t++) {
            ((UserHand) getHand(user)).addSelectorForCart(t, new SelectingHandler(t) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    action.accept(index);
                }
            });
        }
    }

    public void addPlayerSelector() {
        Player player = user;
        for (int i = 0; i < 3; i++) {
            player = player.next();
            if (!player.isAlive()) continue;
            ((BotHand) playerHands[player.getSeatNumber()]).addSelector(new SelectingHandler(player.getSeatNumber()){
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    handelActionResult(selectedAction.apply(index));
                    removeAllHandler();
                }
            });
        }
    }

    public boolean askYesNoQuestionWithMessage(String message) {
        while (true) {
            int n = JOptionPane.showConfirmDialog(
                    gameFrame.getPanel(), message,
                    "No",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                return true;
            } else if (n == JOptionPane.NO_OPTION) {
                return false;
            }
        }
    }

    public void confirmUser(String message) {
        while (true) {
            int n = JOptionPane.showConfirmDialog(
                    gameFrame.getPanel(), message,
                    "OK",
                    JOptionPane.OK_OPTION);
            if (n == JOptionPane.OK_OPTION) {
                break;
            }
        }
    }

    public void colorUserHand(int index) {
        ((UserHand) getHand(user)).colorHand(index);
    }
    public void addCartSelectorToCourt(ArrayList<Cart> newCarts, Consumer<Integer> handelSelectedCartFromCourtForExchange) {
        for (int i = 0; i < 2; i++) {
            CourtPanel.getInstance().addCartToShow(i, newCarts.get(i).getName());
            CourtPanel.getInstance().addMouseListener(i, new SelectingHandler(i) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    handelSelectedCartFromCourtForExchange.accept(index);
                }
            });
        }
    }

    public void addOkButtonForExchange() {
        gameFrame.showOkButton();
    }

    public void colorCourt(int index) {
        CourtPanel.getInstance().colorHand(index);
    }
    public void unColorSelectedCart() {
        ((UserHand) getHand(user)).unColor();
        CourtPanel.getInstance().unColor();
    }

    public int addCartSelectorToDiscard() {
            String[] options = new String[] {"cart1", "cart2"};

            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane optionPane = new JOptionPane("select cart to discard",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION, null, options);


            JDialog dialog = optionPane.createDialog("Select cart");
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            Timer timer = new Timer(10000, e -> dialog.setVisible(false));
            timer.setRepeats(false);
            timer.start();

            dialog.setVisible(true);

            if (optionPane.getValue() instanceof String) {
                String option = (String) optionPane.getValue();

                for (int i = 0; i < 2; i++) {
                    if ((option).equals(options[i])) {
                        return i;
                    }
                }
            }
            return 0;
    }
}
