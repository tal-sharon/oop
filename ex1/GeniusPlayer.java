/**
 * A Player which is smarter than the Clever Player.
 */
public class GeniusPlayer implements Player {
    private static final int FIRST_ROW = 0;
    private static final int SECOND_COLUMN = 1;
    private int coordinate = 0;
    private int middleColumnIndex = 0;

    /**
     * Override plaTurn of Player interface.
     * Plays a turn of the player according to the player's strategy and to the current board and mark.
     * Strategy:
     *      Starts by filling the 2nd column (indexed 1),
     *         and then goes by order over the rest of the board from end square to start by rows.
     *     If the chosen square is taken, skips it.
     *     Restarts his moves every game.
     * @param board: Board - The board which the player is playing with.
     * @param mark: Mark - The mark which represents the player on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        if (board.getMark(FIRST_ROW, SECOND_COLUMN) == Mark.BLANK) {
            middleColumnIndex = 0;
        } else if (board.getMark(FIRST_ROW, SECOND_COLUMN) != mark) {
            middleColumnIndex = 1;
        }
        int row = 0;
        int col = 0;
        do {
            if (middleColumnIndex < size)
            {
                coordinate = (middleColumnIndex * size) + 1;
                row = coordinate / size;
                col = coordinate % size;
                middleColumnIndex++;
                if (middleColumnIndex == size) {
                    coordinate = (size * size);
                }
            }
            else {
                coordinate = (coordinate - 1) % (size * size);
                row = coordinate / size;
                col = coordinate % size;
            }
        } while (!board.putMark(mark, row, col)); // check if coordinates are valid
    }
}
