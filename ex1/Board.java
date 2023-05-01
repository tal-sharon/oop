/**
 * A Board class - used to keep track of a current game's status.
 */
public class Board {
    // data members
    private int size = 4;
    private Mark[][] board = null;

    // constructors

    /**
     * Overrides Default ('blank') Constructor, initiates Board with default size 4.
     */
    Board() {
        initBoard(4);
    }

    /**
     * Board Constructor, initiates a Board instance of given size.
     * @param size: int - The Board's instance size (size*size).
     */
    Board(int size) {
        this.size = size;
        initBoard(size);
    }

    // private methods

    /**
     * Initializes a blank board.
     * @param size: int - The board's size.
     */
    private void initBoard(int size) {
        board = new Mark[size][size];
        for (int row = 0; row < size; row ++) {
            for (int col = 0; col < size; col ++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    // public methods

    /**
     * Puts a given Mark on the instance's board.
     * Assumes row and col arguments are in the boards boarders.
     * @param mark: Mark - The mark to put.
     * @param row: int - The row of the mark to be put in,
     * @param col: int - The column of the mark to be put in,
     * @return true: if action was successful and board was updated accordingly. false: otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (isOutOfRange(row, col) || board[row][col] != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * Getter: Gets the 2D Mark array representing the board.
     * @return Mark[][] - 2D Mark array of board.
     */
    public Mark[][] getBoard() {
        return board;
    }

    /**
     * Getter: Gets the size of the board.
     * @return int: The size of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter: Gets the Mark on given (row, column).
     * Assumes row and col arguments are in the boards boarders.
     * @param row: int - The requested row.
     * @param col: int - The requested column.
     * @return Mark: the Mark on (row, col)
     */
    public Mark getMark(int row, int col) {
        if (isOutOfRange(row, col)) {
            return Mark.BLANK;
        }
        return board[row][col];
    }

    /**
     * Checks if given row and column are out of board's range
     * @param row: int - The check row.
     * @param col: int - The check column.
     * @return: true: if row or column are out of range. false: otherwise.
     */
    private boolean isOutOfRange(int row, int col) {
        return row < 0 || row >= size || col < 0 || col >= size;
    }
}
