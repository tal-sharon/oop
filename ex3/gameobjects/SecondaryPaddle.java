package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A Secondary Paddle is a Paddle which shows up upon a collision with a brick holding
 * a strategy with a PaddlePowerUp.
 * Moves with the game's main paddle directions.
 * Is removed after 3 collisions.
 */
public class SecondaryPaddle extends Paddle {
    /**
     * The game object's tag, used to identify the game object within a several game objects.
     */
    public static final String SECONDARY_PADDLE_TAG = "secondary paddle";
    private int collisionCount;
    private final GameObjectCollection gameObjects;

    /**
     * This constructor creates the secondary paddle object.
     * Constructs a new GameObject-SecondaryPaddle instance.
     *
     * @param topLeftCorner    The top left corner of the position of the text object.
     *                         Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       The size of the text object.
     *                         Width and height in window coordinates.
     * @param renderable       The image file of the paddle.
     *                         The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls.
     * @param gameObjects      A collection of the game's objects.
     */
    public SecondaryPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           UserInputListener inputListener, Vector2 windowDimensions,
                           int minDistFromEdge, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.gameObjects = gameObjects;
        collisionCount = 0;
        setTag(SECONDARY_PADDLE_TAG);
    }

    /**
     * When collides with a Ball instance (game's main ball or pucks), increases the collision count.
     * If collision count reaches 3 -> removes itself.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Ball.BALL_TAG)) {
            collisionCount++;
            if (collisionCount >= 3) {
                gameObjects.removeGameObject(this);
            }
        }
    }


}
