import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private Model model;
    private View view;

    public Controller(Model model) {
        // Model must be created first and then the controller and then the view
        // we can have multiple controllers and views but only one model
        this.model = model;
        //view = new View(model);
        //view.getButton().addActionListener(e->movePlayer());

    }

    public void setView(View view) {
        // View needs controller to exist, call setView after creating a controller
        this.view = view;
    }

    public void doRollDice() {
        // Called by eventclickhandler from View and tell Model the dice roll
        this.model.rollDice();
    }

    public void doPass() {

    }

    public void doBuy() {

    }

    public void doPay() {

    }

    public void doMoveCounter(String playerName, int diceNumber) {
        this.model.moveCounterForwards(playerName, diceNumber);
    }

    public void buyProperty(String playerName, String squareName) {
        this.model.buyProperty(playerName,squareName);
    }

    public void payRent(String payerName, String squareName ) {
        this.model.payRent(payerName, squareName);
    }

    public void upgradeHotel(String playerName, String squareName) {
        this.model.upgradeHotel(playerName, squareName);
    }

    private void playerMove() {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Gives label on button that was clicked
        String action = actionEvent.getActionCommand();
        if (action == "Roll/pass") {
            this.doRollDice();
        }
    }
}
