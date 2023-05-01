package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A Puck is a "Secondary" Ball which shows up upon a collision with a brick holding
 * a strategy with a PuckPowerUp.
 * Collides with all other objects, when goes out of games borders, is removed.
 */
public class Puck extends Ball {
    /**
     * The game object's tag, used to identify the game object within a several game objects.
     */
    public static final String PUCK_TAG = "puck";
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;

    /**
     * This is the ball object constructor. It uses it's super's constructor and saves the sound file.
     * Construct a new GameObject-Puck instance.
     *
     * @param topLeftCorner Position of the top left corner of the ball in the window.
     *                      Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    The dimensions of the ball.
     *                      Width and height in window coordinates.
     * @param renderable    The image object of the ball.
     *                      The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param sound         The sound file object of the ball's collision.
     * @param gameObjects   A collection of the game's objects.
     * @param windowDimensions The game's window dimensions.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound sound, Vector2 windowDimensions, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, sound);
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        setTag(PUCK_TAG);
    }

    /**
     * If goes out of games borders, removes itself.
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
        if (getTopLeftCorner().x() < 0 || getTopLeftCorner().x() > windowDimensions.x() ||
                getTopLeftCorner().y() < 0 || getTopLeftCorner().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
