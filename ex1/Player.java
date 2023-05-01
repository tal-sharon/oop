/**
 * A Player interface for all the types of players in the TicTacToe game.
 */
public interface Player {
    /**
     * Plays a turn of the player according to the player's strategy and to the current board and mark.
     * @param board: Board - The board which the player is playing with.
     * @param mark: Mark - The mark which represents the player on the board.
     */
    void playTurn(Board board, Mark mark);
}
