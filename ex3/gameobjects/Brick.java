package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Implements the Brick class.
 * Bricks are game targets to be destroyed to win the game.
 */
public class Brick extends GameObject {
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String BRICK = "brick";
    private final CollisionStrategy strategy;
    private final Counter counter;

    /**
     * This constructor extends the super's GameObject constructor, and also saves the strategy given.
     * Construct a new Brick instance.
     *
     * @param topLeftCorner The position in the window the top left corner of the object will be placed.
     *                      Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    The 2d dimensions of the object on the screen.
     *                      Width and height in window coordinates.
     * @param renderable    The image object to display on the screen.
     *                      The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param strategy      The strategy that will be used when the brick breaks.
     * @param counter       Counting the number of bricks? // TODO fill the description
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        this.counter = counter;
        setTag(BRICK);
    }

    /**
     * This is an override method for GameObject's onCollisionEnter.
     * When the game detects a collision between the two objects, it activates the strategy of the brick.
     *
     * @param other     The object this brick has collided with.
     *                  The GameObject with which a collision occurred.
     * @param collision The attributes of the collision that occurred.
     *                  Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other, counter);
    }
}
