package src;

import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Camera;
import src.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;
import src.power_ups.*;

import java.util.ArrayList;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The Game Manager class, runs the main method of the program.
 * Initializes all the game objects and manages the updates.
 */
public class BrickerGameManager extends GameManager {

    // files paths
    private static final String BG_IMG_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String HEART_IMG_PATH = "assets/heart.png";
    private static final String BRICK_IMG_PATH = "assets/brick.png";
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final String BALL_IMG_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";
    private static final String BOT_PADDLE_IMAGE_PATH = "assets/botGood.png";

    // window settings
    private static final String WINDOW_TITLE = "Bricker";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;


    // sizes
    private static final float BORDER_WIDTH = 20;
    private static final float BALL_SPEED = 225;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_Y = 450;
    private static final int BRICKS_NUMBER = 56;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_WIDTH = 85;
    private static final int BRICKS_ROW_LEN = 7;
    private static final int BRICKS_SPACE_FROM_WALL = 90;
    private static final int BRICKS_SPACE_FROM_CEILING = 50;
    private static final int BRICKS_SPACE = 2;
    private static final int NUM_OF_LIVES = 3;
    private static final int NUMERIC_LIFE_SIZE = 20;
    private static final int NUMERIC_LIFE_SPACE_X = 120;
    private static final int NUMERIC_LIFE_SPACE_Y = 36;

    // messages
    private static final String WIN_MSG = "You Won!";
    private static final String LOSE_MSG = "You Lost!";
    private static final String PLAY_AGAIN_MSG = " Play Again?";
    private static final String EMPTY_STRING = "";
    private static final int MAX_POWER_UPS = 3;
    private static final int NUM_OF_POWER_UPS = 6;
    private static final int NO_POWER_UP = 0;
    private static final int PUCK_POWER_UP = 1;
    private static final int PADDLE_POWER_UP = 2;
    private static final int LIFE_POWER_UP = 3;
    private static final int CAMERA_POWER_UP = 4;
    private static final int DOUBLE_POWER_UP = 5;

    // data members
    private Ball ball;
    private Paddle paddle;
    private Vector2 windowDimensions;
    private Counter bricksCounter;
    private Counter livesCounter;
    private WindowController windowController;
    private GraphicLifeCounter graphicLifeCounter;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private CameraController cameraController;

    /**
     * This is the constructor of a brick game, which calls its super (GameManager)'s constructor.
     * Creates a new window with the specified title and of the specified dimensions.
     *
     * @param windowTitle      The title of the window.
     *                         Can be null to indicate the usage of the default window title
     * @param windowDimensions A 2d vector representing the height and width of the window.
     *                         Dimensions in pixels. Can be null to indicate a full-screen window
     *                         whose size in pixels is the main screen's resolution
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * This method initializes a new game.
     * It creates all game objects, sets their values and initial positions and allow the start of a game.
     *
     * @param imageReader      An object used to read images from the disc and render them.
     *                         Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      An object used to read sound files from the disc and render them.
     *                         Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    A listener capable of reading user keyboard inputs
     *                         Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its documentation.
     * @param windowController A controller used to control the window and its attributes
     *                         Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        windowDimensions = windowController.getWindowDimensions();
        createBackground();
        createBorders();
        createLives();
        createBall();
        createPaddle();
        createAllBricks();
        cameraController = new CameraController(this, ball);
    }

    /**
     * This method overrides the GameManager update method.
     * It checks for game status, and triggers a new game popup.
     *
     * @param deltaTime Used in the super's update method.
     *                  The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        graphicLifeCounter.update(deltaTime);
        cameraController.update(deltaTime);
        String prompt = EMPTY_STRING;
        if (bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = WIN_MSG;
        }
        double ballHeight = ball.getCenter().y();
        if (ballHeight > WINDOW_HEIGHT) {
            prompt = calcLoseAndReset(prompt);
        }
        playAgain(prompt);
    }

    /**
     * The main driver of the program.
     *
     * @param args unused.
     */
    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }

    /**
     * Creates borders game objects: left right and top with BORDER_WIDTH width.
     */
    private void createBorders() {
        GameObject lefWall = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, WINDOW_HEIGHT), null);
        this.gameObjects().addGameObject(lefWall, Layer.STATIC_OBJECTS);
        GameObject rightWall = new GameObject(new Vector2(WINDOW_WIDTH - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, WINDOW_HEIGHT), null);
        this.gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
        GameObject ceiling = new GameObject(Vector2.ZERO,
                new Vector2(WINDOW_WIDTH, BORDER_WIDTH), null);
        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);

        // outer walls - in case ball lags out of regular walls
        GameObject lefOuterWall = new GameObject(Vector2.ZERO,
                new Vector2(0, WINDOW_HEIGHT), null);
        this.gameObjects().addGameObject(lefOuterWall, Layer.STATIC_OBJECTS);
        GameObject rightOuterWall = new GameObject(new Vector2(WINDOW_WIDTH, 0),
                new Vector2(0, WINDOW_HEIGHT), null);
        this.gameObjects().addGameObject(rightOuterWall, Layer.STATIC_OBJECTS);
    }

    /**
     * Creates all Life game objects: both GraphicLifeCounter and NumericLifeCounter
     * which represents the amount of life remaining to the game until losing, graphically and textually.
     * Number of Lives is 3.
     */
    private void createLives() {
        livesCounter = new Counter(NUM_OF_LIVES);
        Renderable heartImage = imageReader.readImage(HEART_IMG_PATH, true);
        this.graphicLifeCounter = new GraphicLifeCounter(Vector2.ZERO, Vector2.ZERO,
                livesCounter, heartImage, this.gameObjects(), NUM_OF_LIVES);
        new NumericLifeCounter(livesCounter,
                new Vector2(NUMERIC_LIFE_SPACE_X, windowDimensions.y() - NUMERIC_LIFE_SPACE_Y),
                new Vector2(NUMERIC_LIFE_SIZE, NUMERIC_LIFE_SIZE), this.gameObjects());
    }

    /**
     * Creates 56 bricks for the game: 8 rows, 7 columns.
     */
    private void createAllBricks() {
        bricksCounter = new Counter();
        Renderable brickImage = imageReader.readImage(BRICK_IMG_PATH, false);
        while (bricksCounter.value() < BRICKS_NUMBER) {
            this.gameObjects().addGameObject(createSingleBrick(brickImage));
            bricksCounter.increment();
        }
    }

    /**
     * Creates the player's paddle which he hits the ball with to destroy bricks.
     */
    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG_PATH, false);
        this.paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, (int) BORDER_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, PADDLE_Y));
        this.gameObjects().addGameObject(paddle);
    }

    /**
     * Creates the game's ball.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMG_PATH, true);
        Sound ballSound = soundReader.readSound(BALL_SOUND_PATH);
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, ballSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(windowDimensions.mult(0.5F));
        this.gameObjects().addGameObject(ball);
        this.ball = ball;
        setBallRandDir(this.ball);
    }

    /**
     * Sets a ball instance at a random direction (1,1), (1,-1), (-1,1), (-1,-1).
     * @param ball The game's ball
     */
    public static void setBallRandDir(GameObject ball) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates the background.
     */
    private void createBackground() {
        Renderable bgImage = imageReader.readImage(BG_IMG_PATH, false);
        GameObject bg = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(bg, Layer.BACKGROUND);
    }

    /**
     * Creates a single brick, and places it according to the current brick number (from bricksCounter).
     *
     * @param brickImage : Renderable - The brick image to be rendered.
     * @return Brick: The created Brick instance.
     */
    private Brick createSingleBrick(Renderable brickImage) {
        int brickX = BRICKS_SPACE_FROM_WALL +
                ((bricksCounter.value() % BRICKS_ROW_LEN) * (BRICK_WIDTH + BRICKS_SPACE));
        int brickY = BRICKS_SPACE_FROM_CEILING +
                ((bricksCounter.value() / BRICKS_ROW_LEN) * (BRICK_HEIGHT + BRICKS_SPACE));
        Vector2 brickTopLeftCorner = new Vector2(brickX, brickY);
        Vector2 brickDimensions = new Vector2(BRICK_WIDTH, BRICK_HEIGHT);
        CollisionStrategy strategy = getActivatables(brickTopLeftCorner, brickDimensions);
        Brick brick = new Brick(brickTopLeftCorner, brickDimensions, brickImage,
                strategy, bricksCounter);
        brick.setCenter(new Vector2(brickX, brickY));
        return brick;
    }

    /**
     * Initializes a PowerUpStrategy with a random ArrayList of Activatables for the brick's strategy.
     *
     * @param brickTopLeftCorner: Vector2 - The brick's top left corner.
     * @param brickDimensions:    Vector 2 - The brick's dimensions.
     * @return PowerUpStrategy - for the brick.
     */
    private CollisionStrategy getActivatables(Vector2 brickTopLeftCorner, Vector2 brickDimensions) {
        ArrayList<Activatable> powerUps = new ArrayList<>();
        Random rand = new Random();
        int powerCounter = 0;
        generatePowerUp(brickTopLeftCorner, brickDimensions, powerUps, rand, powerCounter, NUM_OF_POWER_UPS);
        return new PowerUpsStrategy(gameObjects(), powerUps);
    }

    /**
     * Recursive method to generate a random ArrayList of Behaviours for the brick.
     *
     * @param brickTopLeftCorner: Vector2 - The brick's top left corner.
     * @param brickDimensions:    Vector 2 - The brick's dimensions.
     * @param powerUps:           ArrayList - of Activatables - power-ups.
     * @param rand                - Random
     * @param powerCounter        - counts the number of activatbales
     * @param numOfPowerUps       - determines from which behaviours to randomize.
     */
    private void generatePowerUp(Vector2 brickTopLeftCorner, Vector2 brickDimensions,
                                 ArrayList<Activatable> powerUps, Random rand,
                                 int powerCounter, int numOfPowerUps) {
        int powerUp = rand.nextInt(numOfPowerUps);
        if (powerCounter < MAX_POWER_UPS) {
            switch (powerUp) {
                case NO_POWER_UP:
                    return;
                case PUCK_POWER_UP:
                    addPuckPowerUp(brickTopLeftCorner, brickDimensions, powerUps);
                    return;
                case PADDLE_POWER_UP:
                    addPaddlePowerUp(powerUps);
                    return;
                case LIFE_POWER_UP:
                    addLifPowerUp(brickTopLeftCorner, brickDimensions, powerUps);
                    return;
                case CAMERA_POWER_UP:
                    addCameraPowerUp(powerUps);
                    return;
                case DOUBLE_POWER_UP:
                    generatePowerUp(brickTopLeftCorner, brickDimensions, powerUps, rand, ++powerCounter,
                            numOfPowerUps - 1);
                    generatePowerUp(brickTopLeftCorner, brickDimensions, powerUps, rand, ++powerCounter,
                            numOfPowerUps);
            }
        }
    }

    /**
     * Creates a CameraPowerUp and adds it to the behaviours ArrayList.
     *
     * @param powerUps: ArrayList - of Activatables - power-ups.
     */
    private void addCameraPowerUp(ArrayList<Activatable> powerUps) {
        Camera camera = new Camera(ball, Vector2.ZERO, windowDimensions.mult(1.2f), windowDimensions);
        powerUps.add(new CameraPowerUp(this, camera));
    }

    /**
     * Creates a LifePowerUp and adds it to the behaviours ArrayList.
     *
     * @param brickTopLeftCorner: Vector2 - The brick's top left corner.
     * @param brickDimensions:    Vector 2 - The brick's dimensions.
     * @param powerUps:           ArrayList - of Activatables - power-ups.
     */
    private void addLifPowerUp(Vector2 brickTopLeftCorner, Vector2 brickDimensions,
                               ArrayList<Activatable> powerUps) {
        Renderable heartImage = imageReader.readImage(HEART_IMG_PATH, true);
        Vector2 brickCenter = new Vector2(brickTopLeftCorner.x() + (brickDimensions.x() / 2),
                brickTopLeftCorner.y() + (brickDimensions.y() / 2));
        powerUps.add(new LifePowerUp(gameObjects(), brickCenter, brickDimensions,
                livesCounter, heartImage, windowDimensions.y()));
    }

    /**
     * Creates a PaddlePowerUp and adds it to the behaviours ArrayList.
     *
     * @param powerUps: ArrayList - of Activatables - power-ups.
     */
    private void addPaddlePowerUp(ArrayList<Activatable> powerUps) {
        Renderable botPaddleImage = imageReader.readImage(BOT_PADDLE_IMAGE_PATH, false);
        powerUps.add(new PaddlePowerUp(gameObjects(), botPaddleImage,
                inputListener, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                windowDimensions, (int) BORDER_WIDTH));
    }

    /**
     * Creates a PuckPowerUp and adds it to the behaviours ArrayList.
     *
     * @param brickTopLeftCorner: Vector2 - The brick's top left corner.
     * @param brickDimensions:    Vector 2 - The brick's dimensions.
     * @param powerUps:           ArrayList - of Activatables - power-ups.
     */
    private void addPuckPowerUp(Vector2 brickTopLeftCorner, Vector2 brickDimensions,
                                ArrayList<Activatable> powerUps) {
        Renderable puckImage = imageReader.readImage(PUCK_IMG_PATH, true);
        Sound puckSound = soundReader.readSound(BALL_SOUND_PATH);
        powerUps.add(new PuckPowerUp(gameObjects(), puckImage, puckSound,
                brickTopLeftCorner, brickDimensions));
    }

    /**
     * Called if a ball got under the paddle and out of borders.
     * Reduces the number of lives by one.
     * If number of lives is greater than zero: resets the ball and paddle.
     * Otherwise: Determines a game lose, and updates the prompt message.
     *
     * @param prompt: String - The current prompt message.
     * @return String - The updated prompt message.
     */
    private String calcLoseAndReset(String prompt) {
        livesCounter.decrement();
        if (livesCounter.value() == 0) {
            prompt = LOSE_MSG;
        }
        setBallRandDir(ball);
        ball.setCenter(windowDimensions.mult(0.5F));
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, PADDLE_Y));
        return prompt;
    }

    /**
     * Check if there is a prompt message,
     * if so, ends the game and acts accordingly: for a 'win' or a 'lose'.
     *
     * @param prompt: String - The current prompt message.
     */
    private void playAgain(String prompt) {
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_MSG;
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

}