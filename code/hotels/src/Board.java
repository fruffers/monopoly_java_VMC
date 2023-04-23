import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Board {
    private ArrayList<Square> squares;
    private HashMap<String,ArrayList<Hotel>> hotelGroups;

    public Board() {
        createSquares();
    }

    public void createSquares() {
        // Data we will use to add onto the squares/JPanels as text
        this.squares = new ArrayList<Square>();
        String[] names = new String[]{"GO","A1", "", "A2", "A3", "", "B1", "", "B2", "B3", "", "C1", "", "C2", "C3", "", "D1", "", "D2", "D3", "", "E1", "", "E2", "E3", "", "F1", "", "F2", "F3", "", "G1", "", "G2", "G3", "", "H1", "", "H2", "H3"};
        int[] prices = new int[]{0, 50, 0, 50, 70, 0, 100, 0, 100, 120, 0, 150, 0, 150, 170, 0, 200, 0, 200, 220, 0, 250, 0, 250, 270, 0, 300, 0, 300, 320, 0, 350, 0, 350, 370, 0, 400, 0, 400, 420};

        // Property counter
        int p = 0;
        // Go through all names
        for (int i = 0; i < names.length; i++) {
            if (prices[i] > 0) {
                // Square with hotel
                this.squares.add(new Square(names[i],prices[i],i));
            } else {
                // Empty square
                this.squares.add(new Square(names[i],i));
            }

        }
        // Map first letter in a hotel group to the group of hotels, e.g: { "A" : hotela1,hotela2,hotela3 }
        this.hotelGroups = new HashMap<String, ArrayList<Hotel>>();
        for (int i = 0; i < names.length; i++) {
            if (names[i].length() > 1 && isNumeric(names[i].substring(1,2))) {
                String groupkey = names[i].substring(0,1);
                // Check if they key already exists, if not then make the group from next positions that are always the same
                if (!hotelGroups.containsKey(groupkey)) {
                    ArrayList<Hotel> hotelGroup = new ArrayList<Hotel>();
                    hotelGroup.add(squares.get(i).getHotel());
                    hotelGroup.add(squares.get(i+2).getHotel());
                    hotelGroup.add(squares.get(i+3).getHotel());
                    this.hotelGroups.put(groupkey,hotelGroup);
                }

            }
        }
    }

    private static boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }

    }

    public ArrayList<Square> getSquares() {
        return this.squares;
    }

    public Square getSquareFromName(String squareName) {
        for (int i = 0; i < squares.size(); i++) {
            if (squares.get(i).getName() == squareName) {
                return squares.get(i);
            }
        }
        return null;
    }

    public Square getSquareFromIndex(int index) {
        if (index < this.squares.size()) {
            return this.squares.get(index);
        }
        return null;
    }

    public Square findSquareAfterSteps(Square startSquare, int stepsForward) {
        // Mod is to not go out of index range of 40 squares or whatever is the squares length
        int forwards = (this.squares.indexOf(startSquare)+stepsForward) % this.squares.size();
        return squares.get(forwards);
    }


    public String getSquareName(int squareIndex) {
        return squares.get(squareIndex).getName();
    }

    public int getHotelPrice(int squareIndex) {
        return squares.get(squareIndex).getHotelPrice();
    }

    public int getHotelOvernightFee(int squareIndex) {
        return squares.get(squareIndex).getHotelOvernightFee();
    }

    public int getHotelRating(int squareIndex) {
        return squares.get(squareIndex).getHotelRating();
    }

    public String getHotelOwnerName(int squareIndex) {
        if (squares.get(squareIndex).hasHotel()) {
            Player owner = squares.get(squareIndex).getHotelOwner();
            if (owner != null) {
                return owner.getName();
            }
        }
        return null;
    }

    public ArrayList<Hotel> getHotelGroup(String hotelName) {
        if (this.hotelGroups.containsKey(hotelName.substring(0,1))) {
            return this.hotelGroups.get(hotelName.substring(0,1));
        }
        return null;
    }

    public int getNumberOfSquares() {
        return this.squares.size();
    }

}
