/**
 * A Renderer interface for all the types of renderers of the TicTacToe Board.
 */
public interface Renderer {
    /**
     * Renders a Board to be displayed on the screen.
     * @param board: Board - The rendered board.
     */
    void renderBoard(Board board);
}
