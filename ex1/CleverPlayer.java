/**
 * A Player which is a bit smarter than a random one.
 */
public class CleverPlayer implements Player {
    private int coordinate = -1;

    /**
     * Override plaTurn of Player interface.
     * Plays a turn of the player according to the player's strategy and to the current board and mark.
     * Strategy:
     *     Starts from (0,0) and advances forward through (0,1), (0,2) and so on
     *         until reaches end of board ((size-1),(size-1)), and then goes back to (0,0).
     *     If the chosen square is taken, skips it.
     *     Doesn't restart his moves every game:
     *          previous game last play was (x,y) -> first play of the game after would be the next square.
     * @param board: Board - The board which the player is playing with.
     * @param mark: Mark - The mark which represents the player on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        int row = 0;
        int col = 0;
        do {    // increase coordinate by 1
            coordinate = (coordinate + 1) % (size * size);
            row = coordinate / size;
            col = coordinate % size;
        } while (!board.putMark(mark, row, col));   // check if coordinates are valid
    }
}
