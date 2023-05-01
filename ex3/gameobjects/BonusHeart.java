package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A Bonus-Heart Game Object, used for the LifePowerUp to get the player extra bonus lives.
 * Only goes straight down when added to the game, and only collides with the main paddle.
 */
public class BonusHeart extends GameObject {
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String HEART_TAG = "heart";
    private final float windowHeight;
    private final GameObjectCollection gameObjects;
    private final Counter livesCounter;

    /**
     * Constructs a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param livesCounter  The game's lives counter.
     * @param gameObjects   A collection of the game's object.
     * @param windowHeight  The game's window height.
     */
    public BonusHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      Counter livesCounter, GameObjectCollection gameObjects, float windowHeight) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        this.windowHeight = windowHeight;
        setTag(HEART_TAG);
    }

    /**
     * On collision (with main paddle) removes itself from the game
     * and increases the game's lives-counter by 1 only if the counter's value is lower than 4.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        if (livesCounter.value() < 4) {
            livesCounter.increment();
        }
    }

    /**
     * Determines with which object the Heart should collide with - only a Paddle which isn't a Puck.
     *
     * @param other The other GameObject.
     * @return boolean: true: if other object is the game's main paddle. false: otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Paddle.PADDLE_TAG)
                && !(other.getTag().equals(SecondaryPaddle.SECONDARY_PADDLE_TAG));
    }

    /**
     * If the Heart falls down out of the game's dimensions, deletes itself.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y() > windowHeight) {
            gameObjects.removeGameObject(this);
        }
    }
}
