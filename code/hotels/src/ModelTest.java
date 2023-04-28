import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @org.junit.jupiter.api.Test
    void upgradeHotel() {
        ModelTesting model = new ModelTesting(true);
        // Scenario precondition: Upgrading hotel goes ahead
        // * Player has rolled dice to move to square A3
        // * Player has purchased hotel
        // * Player has enough money to upgrade hotel
        // * The hotel is 0 stars
        // * Player upgrades hotel

        // Setup scenario to be tested
        Player player = model.getPlayer(model.getCurrentPlayerName());
        model.cheatGoTo(4);
        model.doBuy();
        int beforeBalance = player.getBalance();

        // Check preconditions hold/are valid
        // Check player location is A3
        assert(player.getPosition().getName() == "A3") : "Error: Precondition failed. Player position is not A3";
        // Check hotel owner is player
        assert(player.getPosition().getHotel().getOwner() == player) : "Error: Precondition failed. Player does not own this hotel.";
        // Check player has enough money to upgrade hotel
        assert(player.getBalance() >= player.getPosition().getHotel().getUpgradeFee()) : "Error: Precondition failed. Player does not have enough money to upgrade hotel";
        // Check hotel is 0 stars
        assert(player.getPosition().getHotel().getStarRating() == 0) : "Error: Precondition failed. Hotel is not 0 stars";

        // Upgrade hotel
        model.upgradeHotel(player.getName(), player.getPosition().getName());


        // Postcondition
        // * New rating is 1
        assert(player.getPosition().getHotel().getStarRating() == 1) : "Error: Postcondition failed. Hotel is not 1 stars";
        // * Player balance is reduced by upgrade fee
        assert(player.getBalance() == (beforeBalance - player.getPosition().getHotel().getUpgradeFee())) : "Error: Postcondition failed. Player balance has not deducted upgrade fee.";

    }

    @org.junit.jupiter.api.Test
    void initialisePlayers() {
        // Intialise players is called within the constructor
        // ModelTesting has been added as a subclass of Model in order to access internal private objects for testing purposes,
        // without interfering with Model
        ModelTesting model = new ModelTesting(true);

        /** @post. 2 players created, both have £2000, both start at position 0 and both players are
         * in the players list.
         */

        // Check there are 2 players
        Player player1 = model.getPlayer("player1");
        Player player2 = model.getPlayer("player2");
        assertNotEquals(null,player1, "Error: player1 was not created correctly.");
        assertNotEquals(null,player2, "Error: player2 was not created correctly.");

        // Check both players have 2000 pounds
        assertEquals(2000,player1.getBalance(),"Error: Player1 does not start with 2000.");
        assertEquals(2000,player2.getBalance(),"Error: Player2 does not start with 2000.");

        // Check both players in position 0
        assertEquals(0,player1.getPosition().getPosition(), "Error: player1 does not start at index 0 squares.");
        assertEquals(0,player2.getPosition().getPosition(), "Error: player2 does not start at index 0 squares.");
    }

    @org.junit.jupiter.api.Test
    void getCanBuy() {
        // Scenario: Check canbuy is false if not enough money to buy hotel
        // * Current player's location is square A1
        // * Player's balance is 2000
        // * Square isn't owned, ie. !hasOwner()

        // Setup scenario
        ModelTesting modelTester = new ModelTesting(true);
        Player curPlayer = modelTester.getPlayer(modelTester.getCurrentPlayerName());
        modelTester.cheatGoTo(1);

        // Check preconditions hold/are valid
        // Check player location is A1
        assertTrue(curPlayer.getPosition().getName() == "A1", "Error: Precondition failed. Player position is not A1");
        assertEquals(2000,curPlayer.getBalance(),"Error: Precondition failed. Player does not start with 2000.");
        // Check hotel owner is player
        assertFalse(curPlayer.getPosition().getHotel().hasOwner(),"Error: Precondition failed. Player does not own this hotel.");

        // Check canbuy is enabled
        assertTrue(modelTester.getCanBuy(), "Error: Buying property should be enabled.");

    }


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
    void getCanPay() {
        ModelTesting modelTester = new ModelTesting(true);

        // Scenario: canpay is false if square is empty
        Player player = modelTester.getPlayer(modelTester.getCurrentPlayerName());
        modelTester.cheatGoTo(2);
        assertFalse(modelTester.getCanPay(), "Error: Pay button should be disabled on empty square.");


        // Scenario: canpay is false if nobody owns the square
        modelTester.cheatGoTo(3);
        assertEquals(null, player.getPosition().getHotelOwner(), "Error: Test square hotel should not have an owner.");
        assertFalse(modelTester.getCanPay(), "Error: No owner on hotel, should not be able to pay.");


        // Scenario: canpay true if player has enough money to upgrade and hotel is not 5 stars and current player owns this hotel


        // Scenario: canpay is true if square has opposite player owner and a hotel


    }


}