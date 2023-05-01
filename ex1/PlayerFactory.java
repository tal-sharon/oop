/**
 * A single Factory class for a Player interface.
 */
public class PlayerFactory {

    public static final String HUMAN = "human";
    public static final String WHATEVER = "whatever";
    public static final String CLEVER = "clever";
    public static final String GENIUS = "genius";

    /**
     * Build a player by a given type.
     * @param type: String - The player's type.
     * @return a Player instance of given type.
     */
    public Player buildPlayer(String type) {
        String lowerCaseType = type.toLowerCase();
        switch (lowerCaseType) {
            case HUMAN:
                return new HumanPlayer();
            case WHATEVER:
                return new WhateverPlayer();
            case CLEVER:
                return new CleverPlayer();
            case GENIUS:
                return new GeniusPlayer();
            default:
                return null;
        }
    }
}
