package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.gameobjects.Ball;
import src.gameobjects.Puck;
import src.power_ups.Activatable;

import java.util.ArrayList;
import java.util.Objects;

/**
 * PowerUpsStrategy is a subclass of CollisionStrategy.
 * Is used to assert Power-Ups (Activatable) to the strategy which are activated on brick's collision.
 */
public class PowerUpsStrategy extends CollisionStrategy {
    private static final String CAMERA_POWER_TYPE = "camera";
    private final ArrayList<Activatable> powerUps;

    /**
     * The constructor of the Power-Ups Strategy object.
     * Initiates the powerUp array as a data member.
     *
     * @param gameObjects An object which holds all game objects of the game running.
     * @param powerUps An ArrayList of the strategy's power-up to be activated.
     */
    public PowerUpsStrategy(GameObjectCollection gameObjects, ArrayList<Activatable> powerUps) {
        super(gameObjects);
        this.powerUps = powerUps;
    }

    /**
     * On collision activates all the strategies Power-Ups.
     * Only activates CameraPowerUp when collider object is a Ball and not a Puck.
     *
     * @param collidedObj   The object that was collided (the brick)
     * @param colliderObj   The object that collided with the brick (the ball).
     * @param bricksCounter Counts the current number of bricks in game.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        for (Activatable power : powerUps) {
            if (Objects.equals(power.getName(), CAMERA_POWER_TYPE)
                    && (!(colliderObj.getTag().equals(Ball.BALL_TAG))
                    || colliderObj.getTag().equals(Puck.PUCK_TAG))) {
                continue;
            }
            power.activate();
        }
    }
}
