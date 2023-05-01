/**
 * A Tournament class which initiates the Tournament and runs the whole program.
 * Includes and runs the main function.
 */
public class Tournament {
    public static final String PLAYER_1_SCORE_MSG = "Player 1, %s won: %d rounds%n";
    private static final int ROUND_COUNT_ARG = 0;
    private static final int SIZE_ARG = 1;
    private static final int WIN_STREAK_ARG = 2;
    private static final int RENDER_TARGET_ARG = 3;
    private static final int PLAYER1_ARG = 4;
    private static final int PLAYER2_ARG = 5;
    public static final String WRONG_PLAYER_MSG = "Choose a player, and start again";
    public static final String PLAYER_TYPES_MSG = "The players: [human, clever, whatever, genius]";
    public static final String RESULTS_HEADLINE = "######### Results #########";
    public static final String PLAYER_2_SCORE_MSG = "Player 2, %s won: %d rounds%n";
    public static final String TIES_MSG = "Ties: %d%n";

    private int rounds = 0;
    private Renderer renderer = null;
    private Player[] players = new Player[] {null, null};

    // constructor
    /**
     * A constructor for a Tournament class instance.
     * Sets given arguments as data members of the instance.
     * @param rounds: int - Number of round in the Tournament.
     * @param renderer: Renderer - The renderer of the Tournament's board.
     * @param players: Player[] - The two players of the Tournament.
     */
    Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        System.arraycopy(players, 0, this.players, 0, players.length);
    }

    // public methods

    /**
     * Runs a whole Tournament by its game preferences, players and round count.
     * @param size: int - The size of the Tournament's board.
     * @param winStreak: int - The size of the Win Streak to win a game in the Tournament.
     * @param playerNames: String[] - An array of all players names.
     */
    public void playTournament(int size, int winStreak, String[] playerNames) {
        int[] score = new int[] {0, 0};
        int ties = 0;
        for (int round = 0; round < rounds; round++) {
            ties = runGame(players, size, winStreak, renderer, score, ties, round);
        }
        printResults(playerNames, score, ties);
    }

    /**
     * Runs the main function, checks validity of arguments, initiate needed objects and run the program.
     * @param args: Given program arguments.
     */
    public static void main(String[] args) {
        // check if player names are valid
        if (nameIsNotValid(args[PLAYER1_ARG]) || nameIsNotValid(args[PLAYER2_ARG])) {
            System.out.println(WRONG_PLAYER_MSG);
            System.out.println(PLAYER_TYPES_MSG);
            return;
        }

        // initiate Tournament and other necessary objects
        PlayerFactory playerFactory = new PlayerFactory();
        Player[] players = new Player[]
                { playerFactory.buildPlayer(args[PLAYER1_ARG]),
                        playerFactory.buildPlayer(args[PLAYER2_ARG]) };
        int size = Integer.parseInt(args[SIZE_ARG]);
        int winStreak = Integer.parseInt(args[WIN_STREAK_ARG]);
        RendererFactory rendererFactory = new RendererFactory();
        Tournament tournament = new Tournament(Integer.parseInt(args[ROUND_COUNT_ARG]),
                rendererFactory.buildRenderer(args[RENDER_TARGET_ARG], size),
                players);

        // run tournament
        tournament.playTournament(size, winStreak, new String[] {args[PLAYER1_ARG], args[PLAYER2_ARG]});
    }

    // private methods

    /**
     * Checks if a player's name is valid ignoring lower-upper case permutations.
     * @param name: String - a player's name
     * @return boolean: true - if a name is valid, else - otherwise.
     */
    private static boolean nameIsNotValid(String name)
    {
        return (!name.equalsIgnoreCase(("human")) &&
                !name.equalsIgnoreCase(("whatever")) &&
                !name.equalsIgnoreCase(("clever")) &&
                !name.equalsIgnoreCase(("genius")));
    }

    /**
     * Runs a single game in the Tournament.
     * @param players: Player[] - An array of the Tournament's players.
     * @param size: int - The Tournament's board's size.
     * @param winStreak: int - The Tournament's WinStreak's size.
     * @param renderer: Renderer:  Tournament's Renderer.
     * @param score: int[] - The current Tournament score, indices match 'players' array.
     * @param ties: int - A count of the ties in the Tournament.
     * @param round: int - The round count.
     * @return int - The updated ties count of the tournament.
     */
    private static int runGame(Player[] players, int size, int winStreak, Renderer renderer,
                               int[] score, int ties, int round) {
        Game game = new Game(players[round % players.length], players[(round + 1) % players.length],
                size, winStreak, renderer);
        Mark winner = game.run();
        if (winner == Mark.BLANK) {
            ties++;
        } else if (winner == Mark.X) {
            score[round % score.length]++;
        } else {
            score[(round + 1) % score.length]++;
        }
        return ties;
    }

    /**
     * Prints the Tournament's results
     * @param playerNames: String[] - An array of all players names.
     * @param score: int[] - The current Tournament score.
     * @param ties: int - A count of the ties in the Tournament.
     */
    private static void printResults(String[] playerNames, int[] score, int ties) {
        System.out.println(RESULTS_HEADLINE);
        System.out.print(String.format(PLAYER_1_SCORE_MSG, playerNames[0], score[0]));
        System.out.printf(String.format(PLAYER_2_SCORE_MSG, playerNames[1], score[1]));
        System.out.printf(String.format(TIES_MSG, ties));
    }
}