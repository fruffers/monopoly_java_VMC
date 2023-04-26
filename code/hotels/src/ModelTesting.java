public class ModelTesting extends Model {

    public ModelTesting(boolean cheatmode) {
        super(cheatmode);
    }

    public Player getPlayer(String playerName) {
        // For testing
        return getPlayerFromName(playerName);
    }

}
