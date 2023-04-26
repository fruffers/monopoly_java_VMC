import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @org.junit.jupiter.api.Test
    void getCheatMode() {
        Model model = new Model(true);
        assertTrue(model.getCheatMode(),"Error: Cheat mode is not enabled correctly.");
        Model modelFalse = new Model(false);
        assertFalse(modelFalse.getCheatMode(), "Error: Cheat mode is not disabled correctly.");
    }

    @org.junit.jupiter.api.Test
    void cheatGoTo() {
        Model model = new Model(true);
        int positionBefore = model.getCurrentPlayerPosition();
        model.cheatGoTo(positionBefore + 13);
        int samePosition = model.getCurrentPlayerPosition();
        assertEquals(positionBefore,samePosition,"Error: CheatGoTo did not fail to move the player when given a value higher than 12, " +
                "currentplayer does not stay in same place.");

        int newPosition = (positionBefore + 5) % model.getMaxSquares();
        model.cheatGoTo(newPosition);
        assertEquals(newPosition, model.getCurrentPlayerPosition(), "Error: Player new position from cheat is not +5.");
    }

    @org.junit.jupiter.api.Test
    void getCanBuy() {
        ModelTesting modelTester = new ModelTesting(true);
        // Scenario: Check canbuy is false if not enough money to buy hotel
        Player curPlayer = modelTester.getPlayer(modelTester.getCurrentPlayerName());
        modelTester.cheatGoTo(1);
        assertTrue(modelTester.getCanBuy(), "Error: Buying property should be enabled.");
        curPlayer.chargeMoney(1999);
        assertFalse(modelTester.getCanBuy(), "Error: Hotel is still buyable despite not having enough money.");

    }

    @org.junit.jupiter.api.Test
    void getCanPay() {
        ModelTesting modelTester = new ModelTesting(true);
        // Scenario: canpay is false if square is empty
        // Scenario: canpay is false if nobody owns the square
        // Scenario: canpay is true if square has opposite player owner and a hotel
        // Scenario: canpay true if player has enough money to upgrade and hotel is not 5 stars and current player owns this hotel


    }

    @org.junit.jupiter.api.Test
    void getCanRollPass() {
    }

    @org.junit.jupiter.api.Test
    void createImageIcon() {
    }

    @org.junit.jupiter.api.Test
    void initialisePlayers() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentPlayerName() {
    }

    @org.junit.jupiter.api.Test
    void getPlayerBalance() {
    }

    @org.junit.jupiter.api.Test
    void getInitialised() {
    }

    @org.junit.jupiter.api.Test
    void setInitialised() {
    }

    @org.junit.jupiter.api.Test
    void initialiseModel() {
    }

    @org.junit.jupiter.api.Test
    void isGameOver() {
    }

    @org.junit.jupiter.api.Test
    void getWinnerName() {
    }

    @org.junit.jupiter.api.Test
    void getSquareName() {
    }

    @org.junit.jupiter.api.Test
    void getHotelPrice() {
    }

    @org.junit.jupiter.api.Test
    void getHotelOvernightFee() {
    }

    @org.junit.jupiter.api.Test
    void getHotelRating() {
    }

    @org.junit.jupiter.api.Test
    void getHotelOwnerName() {
    }

    @org.junit.jupiter.api.Test
    void getPlayerImageIcon() {
    }

    @org.junit.jupiter.api.Test
    void getPlayerName() {
    }

    @org.junit.jupiter.api.Test
    void getBalance() {
    }

    @org.junit.jupiter.api.Test
    void getPlayerNamesOnSquare() {
    }

    @org.junit.jupiter.api.Test
    void getSmallImageIcon() {
    }

    @org.junit.jupiter.api.Test
    void switchPlayer() {
    }

    @org.junit.jupiter.api.Test
    void doBuy() {
    }

    @org.junit.jupiter.api.Test
    void doPay() {
    }

    @org.junit.jupiter.api.Test
    void rollPass() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentPlayerPosition() {
    }

    @org.junit.jupiter.api.Test
    void getSquares() {
    }

    @org.junit.jupiter.api.Test
    void getMaxSquares() {
    }

    @org.junit.jupiter.api.Test
    void rollDice() {
    }

    @org.junit.jupiter.api.Test
    void getMaxPlayers() {
    }

    @org.junit.jupiter.api.Test
    void moveCounterForwards() {
    }

    @org.junit.jupiter.api.Test
    void buyProperty() {
    }

    @org.junit.jupiter.api.Test
    void payRent() {
    }

    @org.junit.jupiter.api.Test
    void canAffordHotelUpgrade() {
    }

    @org.junit.jupiter.api.Test
    void upgradeHotel() {
    }

    @org.junit.jupiter.api.Test
    void getHotelsOwnedByPlayer() {
    }

    @org.junit.jupiter.api.Test
    void getPlayerColor() {
    }

    @org.junit.jupiter.api.Test
    void getColorComponentRed() {
    }

    @org.junit.jupiter.api.Test
    void getColorComponentBlue() {
    }

    @org.junit.jupiter.api.Test
    void getColorComponentGreen() {
    }

    @org.junit.jupiter.api.Test
    void getDiceScore() {
    }

    @org.junit.jupiter.api.Test
    void doTurn() {
    }
}