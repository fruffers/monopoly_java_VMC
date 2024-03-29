import javax.lang.model.type.NullType;
import javax.swing.*;
import java.awt.*;

public class Player {
    private String name;
    private int bank;
    private JList<Hotel> hotelsOwned;
    private Square position;
    private Color color;
    ImageIcon imageIcon;

    public Player(String name, Color color,ImageIcon imageIcon) {
        this.name = name;
        this.position = null;
        this.hotelsOwned = new JList<Hotel>();
        this.bank = 2000;
        this.color = color;
        this.imageIcon = imageIcon;
    }

    public ImageIcon getImageIcon() {
        return this.imageIcon;
    }

    public Color getColor() {
        return this.color;
    }

    public int getColorComponentRed() {
        return this.color.getRed();
    }
    public int getColorComponentBlue() {
        return this.color.getBlue();
    }
    public int getColorComponentGreen() {
        return this.color.getGreen();
    }
    public String getName() {
        return name;
    }

    public void recieveMoney(int money) {
        this.bank += money;
    }

    public int getBalance() {
        return bank;
    }

    public void giveMoneyToPlayer(int amount, Player payee) {
        this.bank -= amount;
        payee.recieveMoney(amount);
    }

    public void chargeMoney(int amount) {
        this.bank -= amount;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public Square getPosition() {
        return this.position;
    }

    public boolean isBankrupt() {

        return this.bank <= 0;
    }
}
