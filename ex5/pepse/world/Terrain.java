package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;

/**
 * The game's Terrain class.
 * Generates and manages the game's terrain randomly with a noise generator.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 30;
    public static final int FACTOR_NUM = 10;
    public static final double TERRAIN_Y_LEVEL = 2.0 / 3;
    public static final int DEEP_TERRAIN_LAYER = Layer.BACKGROUND + 20;
    private final GameObjectCollection gameObject;
    private final int surfaceLayer;
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private final Vector2 windowDimensions;

    /**
     * Constructs a Terrain instance.
     * Sets all the instance's parameters.
     * Sets the noiseGenerator with the given seed - which will determine the terrain surface function.
     * @param gameObjects GameObjectCollection, The game's objects.
     * @param groundLayer The terrain's layer.
     * @param windowDimensions The game's window dimensions.
     * @param seed The random seed.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObject = gameObjects;
        this.surfaceLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        groundHeightAtX0 = (float) (windowDimensions.y() * TERRAIN_Y_LEVEL);
        noiseGenerator = new NoiseGenerator(seed);
    }

    /**
     * Getter: gets the height of the ground at a given x value.
     * Uses the noiseGenerator class which calculates the terrains surfaces levels.
     * @param x The given x value.
     * @return The y (height) value at x.
     */
    public float groundHeightAt(float x) {
        int roundedX = (int) (Math.floor(x / Block.SIZE));
        return FACTOR_NUM * Block.SIZE * ((float) noiseGenerator.noise(roundedX)) + groundHeightAtX0;
    }

    /**
     * Creates a terrain in a given x range and adds in to the game.
     * @param minX left bound of the range to create terrain in.
     * @param maxX right bound of the range to create terrain in.
     */
    public void createInRange(int minX, int maxX) {
        int minXFloor = (int) Math.floor((float) minX / Block.SIZE);
        int maxXCeil = (int) Math.ceil((float) maxX / Block.SIZE);
        for (int x = minXFloor; x <= maxXCeil; x++) {
            int xValue = x * Block.SIZE;
            int yValue = (int)
                    Math.max(groundHeightAt(xValue), windowDimensions.y() - (TERRAIN_DEPTH * Block.SIZE));
            yValue = (int) Math.floor((float) yValue / Block.SIZE);
            for (int y = yValue; y < (yValue + TERRAIN_DEPTH); y++) {
                RectangleRenderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(xValue, y * Block.SIZE), blockRenderable);
                if (y == yValue | y == (yValue + 1)) gameObject.addGameObject(block, surfaceLayer);
                else gameObject.addGameObject(block, DEEP_TERRAIN_LAYER);
            }
        }
    }

}
