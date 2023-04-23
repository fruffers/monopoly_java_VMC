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
        while (optionchoice < 1 || optionchoice > options.size()) {
            System.out.println("Please select an option:");
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
        System.out.println("How many squares do you want to move forwards (between 1-12)?: ");
        int output = -1;
        while (output < 1 || output > 12) {
            try {
                String cheati = this.reader.readLine();
                output = new Integer(cheati);
            } catch (IOException e) {
                System.out.println("Invalid option. Try again.");
                //throw new RuntimeException(e);
            }
        }
        return output;
    }

    public CLIMain() throws IOException {
        // Constructor
        // Only uses the model
        this.model = new Model(true);
        this.reader = new BufferedReader((new InputStreamReader(System.in)));


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
            System.out.println("Player: " + playername + "Balance: £" + Integer.toString(playermoney));
            for (int j = 0; j < hotellist.size(); j++) {
                System.out.print(hotellist.get(j) + " ");
                // Keeps 10 hotels on one line.
                if ((j + 1) % 10 == 0) {
                    System.out.println();
                }
            }
            System.out.println("-----------------------------");
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

            String infostring = "Square " + i;
            infostring += squarename.length() < 1 ? "BLANK" : squarename;
            if (price > 0) {
                infostring += " Hotel price: £" + price;
                if (owner != null) {
                    infostring += "Owned by: " + owner;
                    infostring += "Star rating: " + starrating;
                }
            }
            infostring += " Counters on square: " + String.join(", ",countersOnSquare);
            System.out.println(infostring);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println((String)o);
    }


}
