import java.util.Random;

/**
 * A Player which does not know what he's doing.
 */
public class WhateverPlayer implements Player {

    /**
     * Plays a turn of the player according to the player's strategy and to the current board and mark.
     * Strategy:
     *      Picks random square on the board and trys putting a Mark in it until successful.
     * @param board: Board - The board which the player is playing with.
     * @param mark: Mark - The mark which represents the player on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        Random rand = new Random();
        int size = board.getSize();
        int row = 0;
        int col = 0;
        do { // get random coordinates
            int coordinates = rand.nextInt(size * size);
            row = coordinates / size;
            col = coordinates % size;
        } while (!board.putMark(mark, row, col)); // check if coordinates are valid
    }
}
