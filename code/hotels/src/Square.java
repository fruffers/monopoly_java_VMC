import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class Square {

    private Hotel hotel;
    private String name;
    private JLabel label;
    private int position;
    private String description;
    //private JPanel panel;
    JLabel nameLabel;
    JLabel priceLabel;
    JLabel imageLabel;
    ImageIcon[] stars;
    ImageIcon hotelIcon;

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

//    Square(JPanel boardPanel, String name,float price,int[] squareBound,int xval,int yval,Color colour) {
//        this.name = name;
//        this.panel = new JPanel();
//        this.panel.setLayout(null);
//        this.panel.setBounds(xval,yval,squareBound[0],squareBound[1]);
//        this.panel.setBackground(colour);
//        this.panel.setBorder(new LineBorder(Color.black,1));
//        if (price == 0) {
//            this.priceLabel = new JLabel();
//        } else {
//            this.priceLabel = new JLabel("£"+ Float.toString(price), SwingConstants.CENTER);
//        }
//        this.nameLabel = new JLabel(name,SwingConstants.CENTER);
//        this.nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,30));
//        if (squareBound[0] < squareBound[1]) {
//            // Vertical square
//            this.nameLabel.setBounds(0,0,squareBound[0],squareBound[1]/2);
//            this.priceLabel.setBounds(0,(squareBound[1]*2)/3,squareBound[0],squareBound[1]/3);
//        } else if (squareBound[0] > squareBound[1]) {
//            // Horizontal square
//            this.nameLabel.setBounds(0,squareBound[1]/3,squareBound[0]/2,squareBound[1]/2);
//            this.priceLabel.setBounds(squareBound[0]/2,squareBound[1]/2,squareBound[0]/2,squareBound[1]/3);
//        } else {
//            // GO square
//            this.nameLabel.setBounds(0,0,squareBound[0],squareBound[1]);
//            this.nameLabel.setFont(new Font(Font.SERIF,Font.BOLD,60));
//        }
//        // Add all the labels to the panel Square and board
//        this.panel.add(priceLabel);
//        this.panel.add(nameLabel);
//        boardPanel.add(this.panel);
//    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

//    public JPanel getPanel() {
//        return this.panel;
//    }

    public void addNeighbour() {

    }
}
