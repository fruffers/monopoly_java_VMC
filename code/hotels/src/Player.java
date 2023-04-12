import javax.lang.model.type.NullType;
import javax.swing.*;

public class Player {
    private String name;
    private int bank;
    private JList<Hotel> hotelsOwned;
    private Square position;

    public Player(String name) {
        this.name = name;
        this.position = null;
        this.hotelsOwned = new JList<Hotel>();
        this.bank = 2000;
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
