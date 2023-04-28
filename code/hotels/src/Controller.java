import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// connects View with the Model, gives commands
// it will store data in Model and update the View
public class Controller implements ActionListener, MouseListener {
    private Model model;
    private View view;

    public Controller(Model model) {
        // Model must be created first and then the controller and then the view
        // we can have multiple controllers and views but only one model
        this.model = model;

    }

    public void setView(View view) {
        // View needs controller to exist, call setView after creating a controller
        this.view = view;
    }

    public void doRollDice() {
        // Called by eventclickhandler from View and tell Model the dice roll
        this.model.rollDice();
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

    private void doTurn(String playerName, String squareName) {
        this.model.doTurn();
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Gives label on button that was clicked
        String action = actionEvent.getActionCommand();
            try {
                if (action == "roll/pass") {
                this.model.rollPass();
                } else if (action == "buy") {
                    this.model.doBuy();
                } else if (action == "pay") {
                    this.model.doPay();
                } else if (action == "newgame") {
                    this.model.initialiseModel();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

        }

    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Cheat mode clicking the JPanel squares
        int squareindex = Integer.parseInt(mouseEvent.getComponent().getName());
        model.cheatGoTo(squareindex);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
