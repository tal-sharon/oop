package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.gui.Sound;
import danogl.util.Vector2;

/**
 * Implements the Ball class: The main ball of the game.
 * Used to hit and destroy bricks, collides with every object of the game.
 */
public class Ball extends GameObject {
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String BALL_TAG = "ball";
    private final Sound sound;
    private int collisionCount;


    /**
     * This is the ball object constructor. It uses it's super's constructor and saves the sound file.
     * Construct a new GameObject-Ball instance.
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
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        this.sound = sound;
        this.collisionCount = 0;
        setTag(BALL_TAG);
    }

    /**
     * This method overwrites the OnCollisionEnter of GameObject.
     * When it collides with another object, it flips its direction.
     * When the Ball is going straight down, tilts it a bit get ball moving with an angle.
     * Everytime there is a collision, increases the collision count.
     *
     * @param other     The object that the ball collided with.
     *                  The GameObject with which a collision occurred.
     * @param collision The collision parameters.
     *                  Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCount++;
        sound.play();
    }

    /**
     * Getter: Gets the Ball's collision count.
     * Called when CameraPowerUp is active and when count is 4, camera focus on ball should be disabled.
     *
     * @return int: The Ball's collision count
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Setter: Resets the Ball's collision count to 0.
     * Called when CameraPowerUP is activated -> Camera focuses on the Ball.
     */
    public void resetCollisionCount() {
        collisionCount = 0;
    }
}
