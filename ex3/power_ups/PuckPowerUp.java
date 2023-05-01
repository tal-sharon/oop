package src.power_ups;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;

/**
 * An Activatable - Power-Up, which once activated adds a puck-ball to the game.
 */
public class PuckPowerUp implements Activatable {

    /**
     * The game power-up's name, used to identify the game object within a several power-ups.
     */
    public static final String PUCK = "puck";
    private final Vector2 brickDimensions;
    private final Vector2 brickTopLeftCorner;
    private final Sound sound;
    private final Renderable renderable;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a PaddlePowerUp and sets its data members.
     * @param gameObjects An object which holds all game objects of the game running.
     * @param renderable The renderable of game object.
     * @param sound The sound of the game object.
     * @param brickDimensions The dimensions of the brick of which the power-up will belong to.
     * @param brickTopLeftCorner The top left corner of the brick of which the power-up will belong to.
     */
    public PuckPowerUp(GameObjectCollection gameObjects, Renderable renderable, Sound sound,
                       Vector2 brickTopLeftCorner, Vector2 brickDimensions) {
        this.gameObjects = gameObjects;
        this.renderable = renderable;
        this.sound = sound;
        this.brickTopLeftCorner = brickTopLeftCorner;
        this.brickDimensions = brickDimensions;
    }

    /**
     * Activates the power-up and creates a puck and adds it to the game.
     */
    @Override
    public void activate() {
        gameObjects.addGameObject(createPuck(brickTopLeftCorner, brickDimensions));
    }

    @Override
    public String getName() {
        return PUCK;
    }

    /**
     * Creates a puck object.
     * @param topLeftCorner: Vector2 - The puck top left corner coordinates.
     * @param collidedDimensions: Vector2 - The dimensions of the collided object.
     * @return Puck
     */
    private Ball createPuck(Vector2 topLeftCorner, Vector2 collidedDimensions) {
        Ball puck = new Ball(Vector2.ZERO, new Vector2(collidedDimensions.y(), collidedDimensions.y()),
                renderable, sound);
        puck.setCenter(topLeftCorner);
        BrickerGameManager.setBallRandDir(puck);
        return puck;
    }
}
