package pepse;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * The game's GameManger class, initiates and runs the game, manages all the game objects and the updates.
 */
public class PepseGameManager extends danogl.GameManager {
    private static final int CYCLE_LENGTH = 30;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS+10;
    private static final int TREE_LAYER = Layer.STATIC_OBJECTS;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 15);
    private static final String BANANA_IMG_PATH = "assets/banana.png";
    private static final int INITIAL_SCORE = 0;
    private static final int INITIAL_TIME = 30;
    private static final int AVATAR_TERRAIN_DIST = 100;
    private static final int INFOBOARD_X = 50;
    private static final int INFOBOARD_Y = 50;
    private static final int INFOBOARD_SIZE = 90;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final String BG_MUSIC_PATH = "assets/bg_music.wav";
    private static final String MONKEY_SOUND_PATH = "assets/monkey.wav";
    private static final String BANANA_SOUND_PATH = "assets/coin.wav";
    private static final float EXPAND_FACTOR = 0.55f;
    private static final float MONKEY_SOUND_RANDOM_FACTOR = 0.005f;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private int leftBorder;
    private int rightBorder;
    private Terrain terrain;
    private Tree tree;
    private Counter score;
    private Counter timer;
    private int clock;
    private GameObject infoBoard;
    private Avatar avatar;
    private Sound monkeySound;
    private Sound gameOverSound;
    private Random rand;
    private Sound bgMusic;

    /**
     * The main function of the program.
     * @param args
     */
    public static void main(String[] args) {
            new PepseGameManager().run();
    }

    /**
     * The Game initializers - initializes the game's parameters and objects.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, danogl.gui.SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        score = new Counter(INITIAL_SCORE);
        timer = new Counter(INITIAL_TIME);
        clock = LocalDateTime.now().getSecond();
        int seed = new Random().nextInt();
        rand = new Random(seed);
        leftBorder = 0;
        rightBorder = (int) windowDimensions.x();
        createGameObjects(imageReader, inputListener, soundReader, seed);
        monkeySound = soundReader.readSound(MONKEY_SOUND_PATH);
        gameOverSound = soundReader.readSound("assets/game_over.wav");
        bgMusic = soundReader.readSound(BG_MUSIC_PATH);
        bgMusic.playLooped();
        gameObjects().layers().shouldLayersCollide(LEAF_LAYER, GROUND_LAYER, true);
    }

    /**
     * Creates all needed game objects.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *                 See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *      *                      a given key is currently pressed by the user or not. See its
     *      *                      documentation.
     * @param seed The random seed.
     */
    private void createGameObjects(ImageReader imageReader, UserInputListener inputListener,
                                   SoundReader soundReader, int seed) {
        float xWindowCenter = windowDimensions.mult(0.5f).x();
        Sky.create(gameObjects(), windowDimensions, Layer.BACKGROUND);
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, windowDimensions, CYCLE_LENGTH);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, HALO_COLOR);
        terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, seed);
        terrain.createInRange(0, (int) windowDimensions.x());
        tree = new Tree(gameObjects(), terrain, LEAF_LAYER, TREE_LAYER, seed,
                score, timer, imageReader.readImage(BANANA_IMG_PATH, true),
                soundReader.readSound(BANANA_SOUND_PATH), xWindowCenter);
        tree.createInRange(0, (int) windowDimensions.x());
        Vector2 initialAvatarLocation =
                new Vector2(xWindowCenter, terrain.groundHeightAt(xWindowCenter) - AVATAR_TERRAIN_DIST);
        avatar = Avatar.create(gameObjects(), Layer.DEFAULT, initialAvatarLocation,
                inputListener, imageReader);
        setCamera(new Camera(avatar,
                windowDimensions.mult(0.5f).subtract(initialAvatarLocation),
                windowDimensions, windowDimensions));
        infoBoard = new GameObject(new Vector2(INFOBOARD_X, INFOBOARD_Y),
                new Vector2(INFOBOARD_SIZE, INFOBOARD_SIZE), null);
        infoBoard.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(infoBoard, Layer.UI);
    }

    /**
     * Updates the game.
     * Updates the time and the information board's text according to time, score and energy.
     * Checks if time is up and game is over, and suggests another game.
     * Updates the game's world according to the avatar's movement and camera's coordinates.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (rand.nextFloat() < MONKEY_SOUND_RANDOM_FACTOR) {
            monkeySound.play();
        }
        int curTime = LocalDateTime.now().getSecond();
        if (curTime != clock) {
            timer.decrement();
            clock = curTime;
        }

        TextRenderable textRenderable =
                new TextRenderable(String.format("time: %d\nenergy: %d\nscore: %d",
                        timer.value(), avatar.getEnergy(), score.value()));
        infoBoard.renderer().setRenderable(textRenderable);
        isGameOver();
        updateWorld();
    }

    /**
     * Updates the game's world according to the avatar's movement and camera's coordinates.
     */
    private void updateWorld() {
        Vector2 cameraCenter = camera().getCenter();
        int leftEdge = (int) Math.floor((cameraCenter.x() -
                (windowDimensions.x() * EXPAND_FACTOR)) / Block.SIZE) * Block.SIZE;
        int rightEdge = (int) Math.ceil((cameraCenter.x() +
                (windowDimensions.x() * EXPAND_FACTOR)) / Block.SIZE) * Block.SIZE;
        removeObjects(leftEdge, rightEdge);
        createInRange(leftEdge, rightEdge);
    }

    /**
     * Checks if time is up and game is over, and suggests another game.
     */
    private void isGameOver() {
        if (timer.value() <= 0) {
            gameOverSound.play();
            bgMusic.stopAllOccurences();
            if (windowController.openYesNoDialog("Time is up!\n Your score is: " +
                    score.value() + "\n Want to play again?")) {
                windowController.resetGame();
            } else windowController.closeWindow();
        }
    }

    /**
     * Checks if the camera moved and creates new world elements (terrain, trees, etc) accordingly.
     * @param leftEdge The left edge of the world.
     * @param rightEdge The right edge of the world.
     */
    private void createInRange(int leftEdge, int rightEdge) {
        if (leftEdge < leftBorder) {
            terrain.createInRange(leftEdge, leftBorder);
            tree.createInRange(leftEdge, leftBorder);
        }
        if (rightEdge > rightBorder) {
            terrain.createInRange(rightBorder, rightEdge);
            tree.createInRange(rightBorder, rightEdge);
        }
        leftBorder = leftEdge;
        rightBorder = rightEdge;
    }

    /**
     * Checks if the camera moved and removes elements (terrain, trees, etc) which are out of bound.
     * @param leftEdge The left edge of the world.
     * @param rightEdge The right edge of the world.
     */
    private void removeObjects(int leftEdge, int rightEdge) {
        for (GameObject obj : gameObjects()) {
            if (obj.getCenter().x() < leftEdge - 10 * Block.SIZE |
                    obj.getCenter().x() > rightEdge + Block.SIZE) {
                if (obj.getTag().equals(Block.BLOCK_TAG) | obj.getTag().equals(Leaf.LEAF_TAG)) {
                    gameObjects().removeGameObject(obj, GROUND_LAYER);
                    gameObjects().removeGameObject(obj, Layer.BACKGROUND + 20);
                    gameObjects().removeGameObject(obj, TREE_LAYER);
                    gameObjects().removeGameObject(obj, LEAF_LAYER);
                }
            }
        }
    }
}
