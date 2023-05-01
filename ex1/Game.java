/**
 * A Game class - Manges a single game.
 */
public class Game {
    private static final int DEFAULT_SIZE = 4;
    private static final int DEFAULT_WIN_STREAK = 3;
    private static final int ROW = 0;
    private static final int COLUMN = 1;
    private static final int MIN_WIN_STREAK = 1;
    public static final int MOVE_ROW = 1;
    public static final int DONT_MOVE_COLUMN = 0;
    public static final int MOVE_COLUMN = 1;
    public static final int DONT_MOVE_ROW = 0;
    public static final int MOVE_ROW_OPPOSITE_DIRECTION = -1;
    private final Mark[] marks = {Mark.X, Mark.O};

    // data members
    private Player[] players = null;
    private int size = DEFAULT_SIZE;
    private int winStreak = DEFAULT_WIN_STREAK;
    private Renderer renderer = null;
    private Board board = null;

    // constructors

    /**
     * Constructor with default game preferences: board size = 4, win streak = 3.
     *
     * @param playerX:  Player - The 'X' player.
     * @param playerO:  Player - The 'O' player.
     * @param renderer: Renderer - The game's Renderer.
     */
    Game(Player playerX, Player playerO, Renderer renderer) {
        players = new Player[]{playerX, playerO};
        this.renderer = renderer;
    }

    /**
     * Constructor with given game preferences: board size and win streak.
     *
     * @param playerX:   Player - The 'X' player.
     * @param playerO:   Player - The 'O' player.
     * @param size:      int - The game's board size.
     * @param winStreak: int - The game's win streak length.
     * @param renderer:  Renderer - The game's Renderer.
     */
    Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        players = new Player[]{playerX, playerO};
        this.size = size;
        this.winStreak = Math.min(winStreak, size);
        this.renderer = renderer;
    }

    // public methods

    /**
     * Getter: Gets the game's Win Streak
     *
     * @return int: The game's WinStreak.
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * Runs a game until one of the players win or board if full (tie).
     *
     * @return Mark: The winning Mark or BLANK if it's a tie.
     */
    public Mark run() {
        board = new Board(size);

        // run infinite loop of turns until a player wins or board is full (tie)
        for (int i = 0; ; i++) {
            renderer.renderBoard(board);

            // save board's state before player's turn
            Mark[][] tempBoard = copyBoard();

            // play current player's turn
            Mark currentMark = playCurrentTurn(i);

            // find last square marked by player
            int[] playedSquare = findPlayedSquare(tempBoard);

            // check if game is over
            Mark res = calcTurnResult(currentMark, playedSquare[ROW], playedSquare[COLUMN], i);
            if (res != null) {
                renderer.renderBoard(board);    // render board one last time
                return res;
            }
        }
    }


    // private methods

    /**
     * Check if given coordinate (row, column) is a part of a win streak.
     *
     * @param mark: Mark - The Mark in (row,col).
     * @param row:  int - The row of the checked coordinate.
     * @param col:  int - The column of the checked coordinate.
     * @return true: if the coordinate is part of a win streak, false: otherwise.
     */
    private boolean isWinStreak(Mark mark, int row, int col) {
        // check vertical
        if (isStreak(mark, row, col, MOVE_ROW, DONT_MOVE_COLUMN)) return true;

        // check horizontal
        if (isStreak(mark, row, col, DONT_MOVE_ROW, MOVE_COLUMN)) return true;

        // check diagonal down-right
        if (isStreak(mark, row, col, MOVE_ROW, MOVE_COLUMN)) return true;

        // check diagonal up-right
        if (isStreak(mark, row, col, MOVE_ROW_OPPOSITE_DIRECTION, MOVE_COLUMN)) return true;

        return false;
    }

    /**
     * Checks if given row and column are in Board's borders.
     *
     * @param row: int - Given row.
     * @param col: int - Given column.
     * @return true: if given coordinates are in Board's borders. false: otherwise.
     */
    private boolean isInBoard(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Recursively searches for a win streak in a specific direction.
     *
     * @param mark:  Mark - The Mark in (row,col).
     * @param row:   int - The row of the checked coordinate.
     * @param col:   int - The column of the checked coordinate.
     * @param rowDir 1: if it needs to move row.
     *               0: if it doesn't need to move row.
     *               -1: if it needs to move row in opposite direction to column.
     * @param colDir 1: if it needs to move column. 0: otherwise.
     * @param count: int - Counts the current streak.
     * @return The final count of the direction.
     */
    private int searchWinStreak(Mark mark, int row, int col, int rowDir, int colDir, int count) {
        if (count == winStreak) {
            return count;
        }
        if (isInBoard(row, col) && board.getMark(row, col) == mark) {
            return searchWinStreak(mark, row + rowDir, col + colDir, rowDir, colDir, count + 1);
        }
        return count;
    }

    /**
     * Checks if there is a win streak on a given (row, column) and directions.
     *
     * @param mark:  Mark - The Mark in (row,col).
     * @param row:   int - The row of the checked coordinate.
     * @param col:   int - The column of the checked coordinate.
     * @param rowDir 1: if it needs to move row.
     *               0: if it doesn't need to move row.
     *               -1: if it needs to move row in opposite direction to column.
     * @param colDir 1: if it needs to move column. 0: otherwise.
     * @return true: if there is a win streak, false: otherwise.
     */
    private boolean isStreak(Mark mark, int row, int col, int rowDir, int colDir) {
        int curRes = searchWinStreak(mark, row, col, rowDir, colDir, 0);
        // search one direction
        if (curRes == winStreak) {
            return true;
        }
        // search other direction
        return (searchWinStreak(mark, row - rowDir, col - colDir, -rowDir, -colDir, curRes) == winStreak);
    }


    /**
     * Finds the last played square (coordinate) on the board
     * by comparing two 2D arrays of Mark (before and after the last turn).
     *
     * @param prevBoard: Mark[][] - represents the boards status from before the last turn was played.
     * @return int[] - {row, column} of the last played turn.
     */
    private int[] findPlayedSquare(Mark[][] prevBoard) {
        int[] playedSquare = {0, 0};
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (prevBoard[row][col] != board.getMark(row, col)) {
                    playedSquare[ROW] = row;
                    playedSquare[COLUMN] = col;
                    break;
                }
            }
        }
        return playedSquare;
    }

    /**
     * Determines if game is over, if so - returns the winner or a tie.
     *
     * @param currentMark: Mark - The Mark that was last placed on the game board.
     * @param row:         int - The row of the last played coordinate.
     * @param col:         int - The column of the last played coordinate.
     * @return Mark - CurrentMark: if there is a win streak, BLANK: if it's a tie, null: if game isn't over.
     */
    private Mark calcTurnResult(Mark currentMark, int row, int col, int round) {
        if (winStreak == MIN_WIN_STREAK || isWinStreak(currentMark, row, col)) {
            return currentMark;
        }
        if (round + 1 >= size * size) {
            return Mark.BLANK;
        }
        return null;
    }

    /**
     * Runs current player's turn.
     *
     * @param round: int - The current round count.
     * @return Mark - The mark which was played on the turn and placed on the board.
     */
    private Mark playCurrentTurn(int round) {
        Player currentPlayer = players[round % players.length];
        Mark currentMark = marks[round % marks.length];
        currentPlayer.playTurn(board, currentMark);
        return currentMark;
    }

    /**
     * Copies a 2D mark array representing the game's board.
     *
     * @return Mark[][] - The copied board.
     */
    private Mark[][] copyBoard() {
        Mark[][] tempBoard = new Mark[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                tempBoard[row][col] = board.getMark(row, col);
            }
        }
        return tempBoard;
    }
}
