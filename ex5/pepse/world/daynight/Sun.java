package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The game's Sun class.
 * Circles in the game's sky in a circular loop.
 */
public class Sun {
    public static final String SUN_TAG = "sun";
    private static final Vector2 SUN_SIZE = new Vector2(150, 150);
    private static final double SUN_END_ANGLE = 1.5;
    private static final int SUN_INIT_X = -175;
    private static final double SUN_INIT_ANGLE = -1.5;
    private static final int ANGLE_INC = 1;

    /**
     * Creates a Sun object with a half circular transition, and adds it to the game.
     * @param gameObjects GameObjectCollection: The game's objects.
     * @param layer Sun object's layer.
     * @param windowDimensions The game's dimensions.
     * @param cycleLength The day-night cycle length.
     * @return GameObject - The sun game object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun =
                new GameObject(new Vector2(SUN_INIT_X, windowDimensions.y()), SUN_SIZE, sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(sun, (angle) -> {
                if (angle == SUN_END_ANGLE) {
                    sun.setTopLeftCorner(new Vector2(SUN_INIT_X, windowDimensions.y()));
                } else {
                    sun.setTopLeftCorner(sun.getTopLeftCorner().add(new Vector2(ANGLE_INC, angle)));
                }
            }, (float) SUN_INIT_ANGLE, (float) SUN_END_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }

}
