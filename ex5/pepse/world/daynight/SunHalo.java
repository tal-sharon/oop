package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The game's Sun Halo class.
 * Creates a Halo object and adds it to the game.
 */
public class SunHalo {

    public static final int HALO_SIZE = 300;
    public static final String HALO_TAG = "halo";

    /**
     * Creates and Halo object and adds it to the game;
     * @param gameObjects GameObjectCollection: The game's objects.
     * @param layer the Halo object's layer.
     * @param sun The game's Sun game object.
     * @param color The Halo's color.
     * @return Halo's game object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color) {

        class Halo extends GameObject {

            /**
             * Construct a new Halo GameObject instance.
             *
             * @param topLeftCorner Position of the object, in window coordinates (pixels).
             *                      Note that (0,0) is the top-left corner of the window.
             * @param dimensions    Width and height in window coordinates.
             * @param renderable    The renderable representing the object. Can be null, in which case
             *                      the GameObject will not be rendered.
             */
            public Halo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
                super(topLeftCorner, dimensions, renderable);
            }

            /**
             * Update the Halo's coordinates to the sun's coordinates.
             * @param deltaTime The time elapsed, in seconds, since the last frame. Can
             *                  be used to determine a new position/velocity by multiplying
             *                  this delta with the velocity/acceleration respectively
             *                  and adding to the position/velocity:
             *                  velocity += deltaTime*acceleration
             *                  pos += deltaTime*velocity
             */
            @Override
            public void update(float deltaTime) {
                super.update(deltaTime);
                setCenter(sun.getCenter());
            }
        }

        OvalRenderable sunHaloRenderable = new OvalRenderable(color);
        GameObject sunHalo = new Halo(sun.getTopLeftCorner(), new Vector2(HALO_SIZE, HALO_SIZE),
                sunHaloRenderable);
        sunHalo.setCenter(sun.getCenter());
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(HALO_TAG);
        gameObjects.addGameObject(sunHalo, layer);
        return sunHalo;
    }

}
