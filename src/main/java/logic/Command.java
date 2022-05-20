package logic;

import gui.*;
import logic.eventhandler.ObjectiveActionHandler;
import logic.eventhandler.SelectingHandler;
import logic.eventhandler.SubjectiveActionHandler;
import models.Cart;
import models.Court;
import models.Memory;
import models.Player;

import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Command {
    private static Command instance;
    static {
        instance = new Command();
    }

    private GameFrame gameFrame = new GameFrame();
    private PlayerHand[] playerHands = new PlayerHand[4];
    User user;
    private Function<Integer, Player.Result> selectedAction;

    public Command() {
        user = new User("ali");
        new Paranoid("paranoid");
        new Revolutionary("revolutionary");
        new CautiousKiller("cautious killer");
        Court.splitCarts(user);

        madePanelHands();
        showHands();
        gameFrame.update();
    }

    public void showWiner(Player player) {
        playerHands[player.getSeatNumber()].paintBorderYellow();
    }

    private PlayerHand getHand(Player player) {
        return playerHands[player.getSeatNumber()];
    }
    private void setHand(Player player) {
        playerHands[player.getSeatNumber()] = new BotHand(player.getName());
    }
    private void madePanelHands() {
        setHand(user);
        gameFrame.setHand(getHand(user).getPanel(), 0);
        Player bot = user.next();
        for (int t = 0; t < 3; t++) {
            setHand(bot);
            gameFrame.setHand(playerHands[bot.getSeatNumber()].getPanel(), t + 1);
            bot = bot.next();
        }
    }
    private void showHands() {
        Player player = user;
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
        instance.user.tryToPlay();
    }

    public Player getUser() {
        return user;
    }

    public void update() {
        showHands();
        gameFrame.update();
    }
    public void removeAllHandler() {
        //it will make cart hand like real state and remove all mouse listener for selecting modes
        for (int i = 0; i < 4; i++) {
            playerHands[i].removeAllHandler();
        }
    }

    public void showPlayer(int seatNumber) {
        playerHands[seatNumber].paintBorderGreen();
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
    public void addGeneralAction(Memory.SubjectiveActionType type) {
        ActionsList.getGeneralInstance().addAction(
                type.getMessage(),
                new SubjectiveActionHandler(type.getFunction()));
    }
    public void addCharacterAction(Memory.ObjectiveActionType type) {
        ActionsList.getCharacterInstance().addAction(
                type.getMessage(),
                new ObjectiveActionHandler(type.getFunction(),
                        type != Memory.ObjectiveActionType.EXCHANGE_CARD));
    }
    public void handelSubjectiveAction(Callable<Player.Result> action) throws Exception {
        handelActionResult(action.call());
    }

    public void setSelectedAction(Function<Integer, Player.Result> selectedAction) {
        this.selectedAction = selectedAction;
    }

    public void handelObjectiveAction(Function<Integer, Player.Result> action) {
        this.selectedAction = action;
    }
    private void handelActionResult(Player.Result result) {
        switch (result) {
            case CAN_NOT:
                handelCanNot();
                break;
            case UNSUCCESSFUL:
                handelUnsuccessful();
                takeUserTurn();
                break;
            case SUCCESSFUL:
                takeUserTurn();
        }
        gameFrame.update();
    }

    private void takeUserTurn() {
        user.next().tryToPlay(); //continue for other bots
    }
    private void handelUnsuccessful() {
        //TODO
    }
    private void handelCanNot() {
        //show joption pane
        //TODO
    }

    public void addCartSelectorForExchangeCart() {
        for (int t = 0; t < user.getCarts().size(); t++) {
            getHand(user).addSelectorForCart(t, new SelectingHandler(t) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Command.getInstance().handelActionResult(user.exchangeCard(index));
                }
            });
        }
    }

    public void addPlayerSelector() {
        for (int i = 0; i < 4; i++) if (i != user.getSeatNumber()) {
            playerHands[i].addSelector(new SelectingHandler(i){
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Command.getInstance().handelActionResult(selectedAction.apply(index));
                }
            });
        }
    }
}
