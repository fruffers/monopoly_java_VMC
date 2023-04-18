import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Observable;
import java.util.Observer;

// Model is given commands from controller
// it can then update the controller on data changes
// and ask it what to do
// the controller will tell it what to do, it doesn't decide to do
public class Model extends Observable {
    //private ArrayList<Observer> observers;
    private Board board;
    private ArrayList<Player> players;
    public static final int MAXPLAYERS = 2;
    public static final int DICESIDES = 12;
    private int diceScore;
    private int currentPlayer;
    private boolean canBuy = false;
    private boolean canPay = false;
    private boolean canRollPass = false;
    public enum ModelState{
        READY_TO_ROLL,
        ROLLED
    }
    ModelState state = ModelState.READY_TO_ROLL;

    public Model() {
        this.diceScore = 0;
        this.board = new Board();
        initialisePlayers();
        this.canRollPass = true;
        //this.observers = new ArrayList<Observer>();

    }



    public boolean getCanBuy() {
        return this.canBuy;
    }

    public boolean getCanPay() {
        return this.canPay;
    }

    public boolean getCanRollPass() {
        return this.canRollPass;
    }


    /** Returns an ImageIcon, or null if the path was invalid. */
    public ImageIcon createImageIcon(String path, String description) {
        File file = new File("./");
        try {
            System.out.println(file.getCanonicalPath()+"/"+path);
            String pathToIcon = new String(file.getCanonicalPath()+"/"+path);
            return new ImageIcon(pathToIcon, description);

        } catch (IOException e) {
            System.err.println("Couldn't find file: " + path);
        }
        return null;
    }


    public void initialisePlayers() {
        this.players = new ArrayList<Player>();
        ImageIcon icon1 = createImageIcon("resources/car4.png","player1");
        Player player1 = new Player("player1",Color.yellow,icon1);
        player1.setPosition(this.board.getSquareFromIndex(0));


        ImageIcon icon2 = createImageIcon("resources/car2.png","player2");
        Player player2 = new Player("player2",Color.cyan,icon2);
        player2.setPosition(this.board.getSquareFromIndex(0));
        this.players.add(player1);
        this.players.add(player2);
        this.currentPlayer = 0;
    }

    public String getCurrentPlayerName() {
        return this.players.get(this.currentPlayer).getName();
    }

    public int getPlayerBalance(String playerName) {
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).getName() == playerName) {
                return this.players.get(i).getBalance();
            }
        }
        // TODO: Return error, player doesn't exist
        return 0;
    }

    public boolean isGameOver() {
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).isBankrupt()) {
                return true;
            }
        }
        return false;
    }

    private Player getCurrentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public String getWinnerName() {
        if (isGameOver()) {
            if (getCurrentPlayer().isBankrupt()) {
                return this.players.get((currentPlayer+1)%this.players.size()).getName();
            } else {
                return getCurrentPlayerName();
            }
        }
        return null;
    }

    public String getSquareName(int squareIndex) {
        return board.getSquareName(squareIndex);
    }

    public int getHotelPrice(int squareIndex) {
        return board.getHotelPrice(squareIndex);
    }

    public int getHotelOvernightFee(int squareIndex) {
        return board.getHotelOvernightFee(squareIndex);
    }

    public int getHotelRating(int squareIndex) {
        return board.getHotelRating(squareIndex);
    }

    public String getHotelOwnerName(int squareIndex) {
        return board.getHotelOwnerName(squareIndex);
    }

    public ImageIcon getPlayerImageIcon(String playerName) {
        Player player = this.getPlayerFromName(playerName);
        return player.getImageIcon();
    }

    public String getPlayerName(int playerIndex) {
        return players.get(playerIndex).getName();
    }

    public int getBalance(int playerIndex) {
        return players.get(playerIndex).getBalance();
    }

    public ArrayList<String> getPlayerNamesOnSquare(int squareIndex) {
        ArrayList<String> names = new ArrayList<String>();
        Square square = this.board.getSquareFromIndex(squareIndex);
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).getPosition() == square) {
                names.add(this.players.get(i).getName());
            };
        }
        return names;
    }

    public ImageIcon getSmallImageIcon(String playerName) {
        return new ImageIcon(this.getPlayerImageIcon(playerName).getImage().getScaledInstance(32,32,Image.SCALE_DEFAULT));

    }

//    public boolean addNewPlayer(String name) {
//        if (players.size() < MAXPLAYERS ) {
//            players.add(new Player(name));
//            return true;
//        }
//        return false;
//    }

    public void switchPlayer() {
        // Increase index, and mod by players length to avoid index out of range
        int curPlayer = (this.currentPlayer + 1) % this.players.size();
        this.currentPlayer = curPlayer;
        setChanged();
        notifyObservers("Switch player turn to "+this.getCurrentPlayerName());
    }

    public void doBuy() {
        Player player = this.getCurrentPlayer();
        Square square = player.getPosition();
        this.buyProperty(player.getName(),square.getName());
    }

    public void doPay() {
        Player player = this.getCurrentPlayer();
        Square square = player.getPosition();
        Player owner = square.getOwner();
        if (player == owner) {
            // Free stay and upgrade hotel available
            this.upgradeHotel(player.getName(),square.getName());
        } else if (owner != null) {
            this.payRent(player.getName(),square.getName());
            if (this.isGameOver()) {
                setChanged();
                // TODO: Write who has won
                notifyObservers("Game over!");
            }
        }
    }

    public void rollPass() throws InterruptedException {
        // Decided whether to roll dice or pass to next player
        if (this.state == ModelState.READY_TO_ROLL) {
            int diceroll = this.rollDice();
            setChanged();
            notifyObservers("Dice roll is "+ diceroll);
            Thread.sleep((long)100);
            this.moveCounterForwards(this.getCurrentPlayerName(),diceroll);
            this.state = ModelState.ROLLED;
            doTurn();
            setChanged();
            notifyObservers(this.getCurrentPlayerName()+" has moved forwards by "+diceroll+" squares, to "+this.getCurrentPlayer().getPosition().getName());


        } else if (this.state == ModelState.ROLLED) {
            this.switchPlayer();
            this.canBuy = false;
            this.canPay = false;
            this.state = ModelState.READY_TO_ROLL;
        }
    }

    public int rollDice() {
        // Random number * MAXNUMBER + 1 and cast to int which truncates (cuts off the end/any floating numbers)
        // Gives random number from 0-1 then uses dicesides
        // 0.9 * 12 = 10.8 + 1 = 11.8 > truncate to int = 11
        // 0.95 * 12 = 11.4 + 1 = 12.4 > truncate to int = 12
        this.diceScore = (int)(Math.random()*DICESIDES+1);
        setChanged();
        notifyObservers("Dice roll is "+diceScore);
        System.out.println(this.diceScore);
        return this.diceScore;
    }

    // Helper method
    private Player getPlayerFromName(String playerName) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName() == playerName) {
                return players.get(i);
            }
        }
        return null;
    }

    public void moveCounterForwards(String playerName, int diceNumber) {
        Player player = getPlayerFromName(playerName);
        player.setPosition(this.board.findSquareAfterSteps(player.getPosition(),diceNumber));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void buyProperty(String playerName, String squareName) {
        Player player = getPlayerFromName(playerName);
        Square location = board.getSquareFromName(squareName);
        if (location.isBuyable() && player.getBalance() >= location.getHotelPrice()) {
            player.chargeMoney(location.getHotelPrice());
            location.getHotel().setOwner(player);
            // Change
            this.canBuy = location.isBuyable();
            this.canPay = player.getBalance() >= location.getHotel().getUpgradeFee();
            setChanged();
            notifyObservers(playerName+" has purchased "+squareName+" for £"+location.getHotelPrice());
        } // TODO: add error messages if unable to buy a hotel
        else if (player.getBalance() < location.getHotelPrice()) {
            setChanged();
            notifyObservers("Can't buy hotel, not enough money.");
        } else if (location.isBuyable() == false) {
            setChanged();
            notifyObservers("Can't buy Hotel.");
        }
    }

    public void payRent(String payerName, String squareName ) {
        Player payer = getPlayerFromName(payerName);
        Square location = board.getSquareFromName(squareName);
        Player payee = location.getHotelOwner();
        if (payee != null) {
            // Check if owner owns more than one hotel in hotel group
            ArrayList<Hotel> payeeHotelGroup = board.getHotelGroup(squareName);
            int counterHotelsOwnedPayee = 0;
            int counterHotelsOwnedPayer = 0;
            for (int i = 0; i < payeeHotelGroup.size(); i++) {
                Player owner = payeeHotelGroup.get(i).getOwner();
                if (owner == payer) {
                    counterHotelsOwnedPayer += 1;
                } else if (owner == payee) {
                    counterHotelsOwnedPayee += 1;
                }
            }
            // hotel gives standard fee
            int rent = 0;
            Hotel hotel = location.getHotel();
            rent += hotel.getOvernightFee();
            // Double fee if payee owns all hotels in group
            if (counterHotelsOwnedPayee == 3) {
                rent *= 2;
            }
            // Halve fee if guest owns one or more hotels in same group
            if (counterHotelsOwnedPayer > 0) {
                rent /= 2;
            }
            // Charge rent
            payer.giveMoneyToPlayer(rent,payee);
            ////////////// TODO: end game if payee is bankrupt
//            if (payer.isBankrupt()) {
//                this.isGameOver()
//            }
            setChanged();
            notifyObservers(payerName+" has paid £"+rent+" rent to "+payee.getName());
        }
    }

    public boolean canAffordHotelUpgrade(String playerName, String squareName) {
        Player player = getPlayerFromName(playerName);
        Square location = board.getSquareFromName(squareName);
        Hotel hotel = location.getHotel();
        return player.getBalance() >= hotel.getUpgradeFee();
    }

    public boolean upgradeHotel(String playerName, String squareName) {
        Player player = getPlayerFromName(playerName);
        Square location = board.getSquareFromName(squareName);
        Hotel hotel = location.getHotel();
        // Check player is owner of hotel
        if (hotel.getOwner() == player) {
            // Check owner has enough money
            if (player.getBalance() >= hotel.getUpgradeFee()) {
                if (hotel.increaseStarRating()) {
                    player.chargeMoney(hotel.getUpgradeFee());
                    notifyObservers(playerName+" has upgraded "+location.getName()+" which is now "+location.getHotelRating()+" stars.");
                    return true;
                }
                else {
                    setChanged();
                    notifyObservers("Cannot upgrade hotel because it is already at "+Hotel.MAXRATING+" stars.");
                }
            } else {
                // Don't have enough money to buy
                setChanged();
                notifyObservers("Not enough money to upgrade hotel.");
            }
        } else {
            setChanged();
            notifyObservers("Can't upgrade because you don't own the hotel");
        }
        return false;
    }

    public ArrayList<String> getHotelsOwnedByPlayer(String playerName) {
        ArrayList<String> hotels = new ArrayList<String>();
        Player player = getPlayerFromName(playerName);
        for (int i = 0; i < this.board.getNumberOfSquares(); i++) {
            String hotelowner = this.board.getHotelOwnerName(i);
            if (hotelowner == playerName) {
                hotels.add(this.board.getSquareName(i));
            }
        }
        return hotels;
    }

    public Color getPlayerColor(String playerName) {
        Player player = getPlayerFromName(playerName);
        return player.getColor();
    }

    public int getDiceScore() {
        return this.diceScore;
    }

    public void doTurn() {
        Player player = this.getCurrentPlayer();
        this.canBuy = player.getPosition().isBuyable();
        Player owner = player.getPosition().getHotelOwner();
        if (owner == player) {
            this.canPay = player.getBalance() >= player.getPosition().getHotel().getUpgradeFee();
            this.canRollPass = true;
        }
        if (owner != null) {
            this.canPay = true;
            this.canRollPass = false;
        }

    }


}
