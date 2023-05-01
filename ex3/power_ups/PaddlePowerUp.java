package src.power_ups;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.gameobjects.SecondaryPaddle;

/**
 * An Activatable - Power-Up, which once activated adds a secondary paddle to the game.
 */
public class PaddlePowerUp implements Activatable {
    private static final float SECONDARY_PADDLE_Y = 300;
    /**
     * The game power-up's name, used to identify the game object within a several power-ups.
     */
    public static final String PADDLE = "paddle";
    private final SecondaryPaddle secondaryPaddle;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a PaddlePowerUp.
     * Initializes a secondary paddle.
     *
     * @param gameObjects      An object which holds all game objects of the game running.
     * @param paddleDimensions Vector2 - The paddle's dimensions
     * @param renderable A renderable for the Paddle
     * @param inputListener A listener to the user's input
     * @param minDistFromEdge The minimum distance to keep the paddle from the edge
     * @param windowDimensions The game's window dimensions
     */
    public PaddlePowerUp(GameObjectCollection gameObjects, Renderable renderable, UserInputListener
            inputListener, Vector2 paddleDimensions, Vector2 windowDimensions, int minDistFromEdge) {
        this.gameObjects = gameObjects;
        secondaryPaddle = new SecondaryPaddle(Vector2.ZERO, paddleDimensions, renderable,
                inputListener, windowDimensions, minDistFromEdge, gameObjects);
        secondaryPaddle.setCenter(new Vector2(windowDimensions.x()/2, SECONDARY_PADDLE_Y));
    }

    /**
     * If there isn't a secondary paddle already in the game, adds the secondary paddle.
     */
    @Override
    public void activate() {
        for (GameObject obj:gameObjects) {
            if (obj.getTag().equals(SecondaryPaddle.SECONDARY_PADDLE_TAG)) {
                return;
            }
        }
        gameObjects.addGameObject(secondaryPaddle);
    }

    @Override
    public String getName() {
        return PADDLE;
    }
}
