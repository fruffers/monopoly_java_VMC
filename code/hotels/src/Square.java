import javax.swing.*;

public class Square {

    private Hotel hotel;
    private String name;
    private int position;

    Square(String name, int price, int position) {
        this.position = position;
        this.hotel = new Hotel(name,price);
    }

    Square(String name, int position) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean hasHotel() {
        return this.hotel != null;
    }

    public String getName() {
        if (hasHotel()) {
            return hotel.getName();
        } else {
            return this.name;
        }
    }

    public int getHotelPrice() {
        if (hasHotel()) {
            return hotel.getPrice();
        } else {
            return 0;
        }
    }

    public int getHotelRating() {
        if (hasHotel()) {
            return hotel.getStarRating();
        }
        return 0;
    }

    public Player getHotelOwner() {
        if (hasHotel()) {
            return hotel.getOwner();
        }
        return null;
    }

    public int getHotelOvernightFee() {
        if (hasHotel()) {
            return hotel.getOvernightFee();
        }
        return 0;
    }

    public Player getOwner() {
        if (this.hasHotel()) {
            return hotel.getOwner();
        }
        return null;
    }

    public boolean isBuyable() {
        return this.hasHotel() && (this.getHotelOwner() == null);
    }

    public Hotel getHotel() {
        if (this.hasHotel()) {
            return this.hotel;
        }
        return null;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

}
