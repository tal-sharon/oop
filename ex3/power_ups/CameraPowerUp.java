package src.power_ups;

import danogl.gui.rendering.Camera;
import src.BrickerGameManager;

/**
 * An Activatable - Power-Up, which once activated get the game's camera focus on the game's main ball.
 * Stops after the ball collides with anything 4 times - this is implemented by the game's CameraController.
 */
public class CameraPowerUp implements Activatable {
    /**
     * The game power-up's name, used to identify the game object within a several power-ups.
     */
    public static final String CAMERA = "camera";
    private final Camera camera;
    private final BrickerGameManager gameManager;

    /**
     * Constructor of the CameraPowerUp, sets data members.
     *
     * @param gameManager The BrickerGameManager.
     * @param camera      The game's camera to control.
     */
    public CameraPowerUp(BrickerGameManager gameManager,
                         Camera camera) {
        this.gameManager = gameManager;
        this.camera = camera;
    }

    /**
     * If the camera focus is not yet activated, activates it and gets the camera focus on the ball.
     */
    @Override
    public void activate() {
        if (gameManager.getCamera() == null) {
            gameManager.setCamera(camera);
        }
    }

    @Override
    public String getName() {
        return CAMERA;
    }
}
