package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Objects;

/**
 * The game's Block class.
 * The basic Unit which most of the game's environment and objects are built with.`
 */
public class Block extends GameObject {
    public static final int SIZE = 30;
    public static final String BLOCK_TAG = "block";

    /**
     * Construct a new Block GameObject instance.
     * Sets the block's physics and parameters.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(BLOCK_TAG);
    }

    /**
     * If block collides with leaf, leaf's velocity is set to zero.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (Objects.equals(other.getTag(), "leaf")) {
            other.setVelocity(Vector2.ZERO);
        }
    }

}
