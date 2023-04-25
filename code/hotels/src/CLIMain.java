import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIMain implements Observer {
//    static ArrayList<List<String>> squaresPrinter;
    //static ArrayList<String> squaresPrinter;
    //static ArrayList<Square> squares;
    static Model model;
    Scanner scanner;
    BufferedReader reader;
    public static final String RESET = "\033[0m";
    public static final String ALERTCOLOR = "\033[38;2;255;0;255m";

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        System.out.println("---------------CLI Hotels---------------");
        CLIMain cli = null;
        try {
            cli = new CLIMain();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cli.playGame();

    }

    private String getPlayerColorCode(String playername) {
        int red = model.getColorComponentRed(playername);
        int blue = model.getColorComponentBlue(playername);
        int green = model.getColorComponentGreen(playername);
        // ANSI escape sequence format - 38 is foreground-48 is background, 2 means it is static/solid color
        String rgbformat = "\033[48;2;" + red + ";" + green + ";" + blue + "m";
        return rgbformat;
    }

    public String getUserCommand() {
        ArrayList<String> options = new ArrayList<String>();
        if (model.getCanRollPass()) {
            options.add("roll/pass");
        }
        if (model.getCanBuy()) {
            options.add("buy");
        }
        if (model.getCanPay()) {
            options.add("pay");
        }
        if (model.getCheatMode()) {
            options.add("cheat");
        }
        int optionchoice = -1;
        String playername = model.getCurrentPlayerName();
        String playerColorCode = this.getPlayerColorCode(playername);
        while (optionchoice < 1 || optionchoice > options.size()) {
            System.out.println("Please select an option " + playerColorCode + playername + RESET + ":");
            //System.out.println("\033[0;31m  red text");
            for (int i = 0; i < options.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + options.get(i));

            }
            try {
                String getline = this.reader.readLine();
                optionchoice = new Integer(getline);
            } catch (IOException e) {
                System.out.println("Invalid input, please try again.");
                //throw new RuntimeException(e);
            }
        }
        return options.get(optionchoice - 1);

    }

    public void endGame() {
        System.out.println(this.model.getWinnerName()+" has won the game!!!!!!!!");
    }

    public void playGame() {
        while (!this.model.isGameOver()) {
            this.printBoard();
            this.printPlayersInfo();
            String command = getUserCommand();
            this.processCommand(command);
        }
        this.endGame();
    }

    public void processCommand(String command) {
        if (command == "roll/pass") {
            try {
                model.rollPass();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if (command == "buy") {
            model.doBuy();
        }
        else if (command == "pay") {
            model.doPay();
        }
        else if (command == "cheat") {
            int steps = cheatMove();
            int curPlayerPosition = model.getCurrentPlayerPosition();
            int out = (curPlayerPosition + steps) % model.getMaxSquares();
            model.cheatGoTo(out);
        }
    }

    private int cheatMove() {
        // System.out.println("How many squares do you want to move forwards (between 1-12)?: ");
        int output = -1;
        while (output < 1 || output > 12) {
            try {
                System.out.println("How many squares do you want to move forwards (between 1-12)?: ");
                String cheati = this.reader.readLine();
                output = new Integer(cheati);
            } catch (IOException e) {
                //throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("You must enter a number.");

            } finally {
                if (output < 1 || output > 12) {
                    System.out.println("Invalid option. Try again.");
                }

            }
        }
        return output;
    }

    public CLIMain() throws IOException {
        // Constructor
        // Only uses the model
        this.model = new Model(true);
        this.reader = new BufferedReader((new InputStreamReader(System.in)));
        this.model.addObserver(this);


        // GUI has no ring structure, it's a long list of square spaces, and then you move through that list
//        for (Square i : model.getSquares()) {
//            String squarename = i.getName();
//            int price = i.getHotelPrice();
//            String owner = i.getHotelOwner().getName();
//            int starrating = i.getHotelRating();
//
//            String infostring = "Square "+i;
//            infostring += squarename.length() < 1 ? "BLANK" : squarename;
//            if (price > 0) {
//                infostring += "\nHotel price: £"+ price;
//                if (owner != null) {
//                    infostring += "Owned by: " + owner;
//                    infostring += "Star rating: " + starrating;
//                }
//            }

            //System.out.println();
            //squaresPrinter.add(squarename+(Integer.toString(price))+owner+Integer.toString(starrating));

        }
//        System.out.println("<html>"+model.getSquares()+"</html>");
//        this.reader = new BufferedReader((new InputStreamReader(System.in)));
//        while (!model.isGameOver()) {
//            // Display CLI version of the button presses
//            System.out.println("Press enter to roll the dice");
//            String scanString = scanner.nextLine();
//            // If enter key pressed
//            if (scanner.hasNextLine()) {
//                try {
//                    model.rollPass();
//                    // TODO: How will message be displayed if we can't use observer?
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            //String input = reader.readLine();

            //model.doTurn();
            //System.out.println("<html>"+model.getSquares()+"</html>");
        //}
        //this.squares = model.getSquares();
        // Create players
    //}

    public void printPlayersInfo() {
        for (int i = 0; i < model.getMaxPlayers(); i++) {
            String playername = model.getPlayerName(i);
            int playermoney = model.getBalance(i);
            ArrayList<String> hotellist = model.getHotelsOwnedByPlayer(playername);
            String playerColorString = this.getPlayerColorCode(playername);
            System.out.println(playerColorString + "Player: " + playername + "\n" + "Balance: £" + Integer.toString(playermoney));
            for (int j = 0; j < hotellist.size(); j++) {
                System.out.print(hotellist.get(j) + " ");
                // Keeps 10 hotels on one line.
                if ((j + 1) % 10 == 0) {
                    System.out.println();
                }
            }
            // RESET color
            System.out.println(RESET + "-----------------------------");
        }
    }

    public void printBoard() {
        //System.out.println("<html>"+model.getSquares()+"</html>");
        for (int i = 0; i < model.getMaxSquares(); i++) {
            String squarename = model.getSquareName(i);
            int price = model.getHotelPrice(i);
            String owner = model.getHotelOwnerName(i);
            int starrating = model.getHotelRating(i);
            ArrayList<String> countersOnSquare = model.getPlayerNamesOnSquare(i);
            for (int j = 0; j < countersOnSquare.size(); j++) {
                countersOnSquare.set(j, this.getPlayerColorCode(countersOnSquare.get(j)) + countersOnSquare.get(j) + RESET);
            }

            String infostring = "Square " + i + " ";
            infostring += squarename.length() < 1 ? "BLANK" : squarename;
            if (price > 0) {
                infostring += " Hotel price: £" + price;
                if (owner != null) {
                    String ownerColor = getPlayerColorCode(owner);
                    infostring += ownerColor;
                    infostring += " Owned by: " + owner;
                    infostring += " Star rating: " + starrating;
                    infostring += RESET;
                }
            }
            infostring += " Counters on square: " + String.join(", ",countersOnSquare);
            System.out.println(infostring);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println(ALERTCOLOR + (String)o + RESET);
    }


}
