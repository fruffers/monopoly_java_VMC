import javax.swing.*;

public class Hotel {
    private int price;
    private String name;
    private Player owner;
    private int rating;
    public static final int MAXRATING = 5;

    public Hotel(String name, int price) {
        this.name = name;
        this.price = price;
        this.rating = 0;
        this.owner = null;

    }

    public int getUpgradeFee() {
        return price / 2;
    }

    public int getOvernightFee() {
        if (owner == null) {
            return 0;
        } else {
            return (this.price/10)*(this.rating*this.rating);
        }
    }

    public String getName() {
        return this.name;
    }

    public int getStarRating() {
        return rating;
    }

    public boolean increaseStarRating() {
        if (rating < MAXRATING) {
            rating++;
            return true;
        }
        return false;
    }

    public int getPrice() {
        return this.price;
    }

    public boolean setOwner(Player player) {
        if (owner == null) {
            owner = player;
            return true;
        }
        return false;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }



}
