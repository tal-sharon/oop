package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * A game objects which is a paddle used to hit the ball to destroy the game's bricks.
 * Is used and moved by the player/user.
 */
public class Paddle extends GameObject {
    private static final int MOVE_SPEED = 500;
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String PADDLE_TAG = "paddle";
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;

    /**
     * This constructor creates the paddle object.
     * Construct a new GameObject-Paddle instance.
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
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
        setTag(PADDLE_TAG);
    }

    /**
     * This method is overwritten from GameObject.
     * If right and/or left key is recognised as pressed by the input listener,
     * it moves the paddle, and check that it doesn't move past the borders.
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
        Vector2 movementDir = calcMovementDir();
        setVelocity(movementDir.mult(MOVE_SPEED));
    }

    /**
     * Calculates the paddle's movement direction according to the key pressed.
     * If paddle is too close to the edge (by minDistFromEdge), blocks movement.
     *
     * @return Vector2 - The paddle's movement direction.
     */
    private Vector2 calcMovementDir() {
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }

        // if reached border
        if (getTopLeftCorner().x() < minDistFromEdge) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        if (getTopLeftCorner().x() > windowDimensions.x() - getDimensions().x() - minDistFromEdge) {
            movementDir = movementDir.add(Vector2.LEFT);
        }

        return movementDir;
    }
}
