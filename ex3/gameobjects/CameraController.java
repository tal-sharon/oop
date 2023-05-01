package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.BrickerGameManager;

/**
 * A game object which is used to control the game's camera.
 * Used for the CameraPowerUp purposes.
 */
public class CameraController extends GameObject {
    private static final int MAX_HITS_CAMERA = 4;
    /**
     * The game object's tag, used to identify the game object within a several objects.
     */
    public static final String CAMERA_CONTROLLER_TAG = "camera controller";
    private final BrickerGameManager gameManager;
    private final Ball ball;
    private boolean isCameraOnBall = false;

    /**
     * Constructs a new CameraController instance.
     *
     * @param gameManager: BrickerGameManger - The game's manager object.
     * @param ball:        Ball - The ball object which the camera reacts to.
     */
    public CameraController(BrickerGameManager gameManager, Ball ball) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.gameManager = gameManager;
        this.ball = ball;
        setTag(CAMERA_CONTROLLER_TAG);
    }

    /**
     * Updates the Game Manager's camera.
     * If is currently activated, follows the ball's collision count since activation
     * and resets the camera at 4 collisions.
     * Else: does nothing.
     *
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
        if (gameManager.getCamera() != null) {
            if (!isCameraOnBall) {
                isCameraOnBall = true;
                ball.resetCollisionCount();
            }
            if (ball.getCollisionCount() >= MAX_HITS_CAMERA) {
                gameManager.setCamera(null);
                isCameraOnBall = false;
            }
        }
    }
}


