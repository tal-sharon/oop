package src.power_ups;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.BonusHeart;

/**
 * An Activatable - Power-Up, which once activated drops down a heart representing a bonus life for
 *      the player to obtain.
 */
public class LifePowerUp implements Activatable {
    /**
     * The game power-up's name, used to identify the game object within a several power-ups.
     */
    public static final String LIFE = "life";
    private final GameObject heart;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs the LifePowerUp.
     * Initializes a Heart object representing the bonus life.
     * @param gameObjects: GameObjectCollection - The game's game-objects.
     * @param brickCenter: Vector2 - The bricks center coordinates.
     * @param brickDimensions - Vector2 - The bricks dimensions.
     * @param livesCounter - Counter - The games lives counter.
     * @param widgetRenderable - Renderable - The heart renderable.
     * @param windowHeight - float - The game's window height
     */
    public LifePowerUp(GameObjectCollection gameObjects, Vector2 brickCenter, Vector2 brickDimensions,
                       Counter livesCounter, Renderable widgetRenderable, float windowHeight) {
        this.gameObjects = gameObjects;
        heart = new BonusHeart(Vector2.ZERO, new Vector2(20, 20),
                widgetRenderable, livesCounter, gameObjects, windowHeight);
        heart.setCenter(brickCenter);
        heart.setTopLeftCorner(new Vector2(heart.getTopLeftCorner().x() - (brickDimensions.x()/2),
                heart.getTopLeftCorner().y() - (brickDimensions.y()/2)));
        heart.setVelocity(Vector2.DOWN.mult(100));
    }

    /**
     * Adds the heart to the game.
     */
    @Override
    public void activate() {
        gameObjects.addGameObject(heart);
    }

    @Override
    public String getName() {
        return LIFE;
    }
}
