package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;

/**
 * A class used to graphically represent the amount of lives the player has.
 */
public class GraphicLifeCounter extends GameObject {
    private static final int HEART_SIZE = 20;
    private static final int SPACE_FROM_WALL = 40;
    private static final int HEARTS_Y = 475;
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String GRAPHIC_LIFE_TAG = "graphic life";
    private final Renderable widgetRenderable;
    private final Counter livesCounter;
    ArrayList<GameObject> hearts;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;

    /**
     * This is the constructor for the graphic lives counter.
     * It creates a 0x0 sized object (to be able to call its update method in game),
     * Creates numOfLives hearts, and adds them to the game.
     * Construct a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner  The top left corner of the left most heart.
     *                             Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions     The dimension of each heart.
     *                             Width and height in window coordinates.
     * @param livesCounter         The counter which holds current lives count.
     * @param widgetRenderable     The image renderable of the hearts.
     *                             The renderable representing the object. Can be null, in which case
     *                             the GameObject will not be rendered.
     * @param gameObjectCollection The collection of all game objects currently in the game
     * @param numOfLives           Number of current lives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.widgetRenderable = widgetRenderable;
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
        hearts = new ArrayList<>();
        for (int i = 0; i <= numOfLives; i++) {
            hearts.add(createHeart(i));
            if (i < numOfLives) {
                gameObjectCollection.addGameObject(hearts.get(i), Layer.BACKGROUND);
            }
        }
        setTag(GRAPHIC_LIFE_TAG);
    }

    /**
     * This method is overwritten from GameObject.
     * It removes hearts from the screen if there are more hearts than there are lives left.
     *
     * @param deltaTime unused.
     *                  The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            numOfLives = livesCounter.value();
            gameObjectCollection.removeGameObject(hearts.get(numOfLives), Layer.BACKGROUND);
        } else if (livesCounter.value() > numOfLives) {
            gameObjectCollection.addGameObject(hearts.get(numOfLives), Layer.BACKGROUND);
            numOfLives = livesCounter.value();
        }
    }

    /**
     * Creates and places a single heart game object according to a given index.
     *
     * @param heartIndex: int - The heart's index in range(numOfLives).
     * @return GameObject - The created instance.
     */
    private GameObject createHeart(int heartIndex) {
        GameObject heart = new GameObject(Vector2.ZERO, new Vector2(HEART_SIZE, HEART_SIZE),
                widgetRenderable);
        int heartX = SPACE_FROM_WALL + (heartIndex * HEART_SIZE);

        heart.setCenter(new Vector2(heartX, HEARTS_Y));
        return heart;
    }
}
