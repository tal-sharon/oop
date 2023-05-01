package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The Night Class.
 * Darkens the screen with a circular transition.
 */
public class Night {

    public static final String NIGHT_TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates a night object and adds the brightness transition.
     * @param gameObjects GameObjectCollection: The game's objects.
     * @param layer Night object's layer.
     * @param windowDimensions The game's dimensions.
     * @param cycleLength The day-night cycle length.
     * @return GameObject - The night game object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength) {
        RectangleRenderable nightRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night, night.renderer()::setOpaqueness,MIDNIGHT_OPACITY, 0f,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }
}
