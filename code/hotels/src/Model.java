import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
    //private ArrayList<Observer> observers;
    private Board board;
    private ArrayList<Player> players;
    public static final int MAXPLAYERS = 2;
    public static final int DICESIDES = 12;
    private int diceScore;
    private int currentPlayer;

    public Model() {
        this.diceScore = 0;
        this.board = new Board();
        initialisePlayers();
        //this.observers = new ArrayList<Observer>();

    }

    public void initialisePlayers() {
        this.players = new ArrayList<Player>();
        Player player1 = new Player("player1");
        player1.setPosition(this.board.getSquareFromIndex(0));
        Player player2 = new Player("player2");
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

    public boolean addNewPlayer(String name) {
        if (players.size() < MAXPLAYERS ) {
            players.add(new Player(name));
            return true;
        }
        return false;
    }

    public int rollDice() {
        // Gives random number from 0-1 then uses dicesides
        this.diceScore = (int)Math.random()*DICESIDES+1;
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
    }

    public void buyProperty(String playerName, String squareName) {
        Player player = getPlayerFromName(playerName);
        Square location = board.getSquareFromName(squareName);
        if (location.isBuyable() && player.getBalance() >= location.getHotelPrice()) {
            player.chargeMoney(location.getHotelPrice());
            location.getHotel().setOwner(player);
        } // TODO: add error messages if unable to buy a hotel
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
                    return true;
                }
            }
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

    public int getDiceScore() {
        return this.diceScore;
    }



}