package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class used to textually represent the amount of lives the player has.
 */
public class NumericLifeCounter extends GameObject {
    private static final String INIT_LIVES_NUMBER = "3";
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String NUMERIC_LIFE_TAG = "numeric life";
    private final TextRenderable textRenderable;
    private final Counter livesCounter;

    /**
     * The constructor of the textual representation object of how many strikes are left in the game.
     * Construct a new GameObject instance.
     *
     * @param livesCounter         The counter of how many lives are left right now.
     * @param topLeftCorner        The top left corner of the position of the text object.
     *                             Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           The size of the text object.
     *                             Width and height in window coordinates.
     * @param gameObjectCollection The collection of all game objects currently in the game
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.textRenderable = new TextRenderable(INIT_LIVES_NUMBER);
        this.livesCounter = livesCounter;
        textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
        gameObjectCollection.addGameObject(this, -1);
        setTag(NUMERIC_LIFE_TAG);
    }

    /**
     * This method is overwritten from GameObject.
     * It sets the string value of the text object to the number of current lives left.
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
        int numOfLives = livesCounter.value();
        switch (numOfLives) {
            case 4:
                textRenderable.setString("4");
                textRenderable.setColor(Color.GREEN);
                break;
            case 3:
                textRenderable.setString("3");
                textRenderable.setColor(Color.GREEN);
                break;
            case 2:
                textRenderable.setString("2");
                textRenderable.setColor(Color.ORANGE);
                break;
            case 1:
                textRenderable.setString("1");
                textRenderable.setColor(Color.RED);
                break;
        }

    }
}
