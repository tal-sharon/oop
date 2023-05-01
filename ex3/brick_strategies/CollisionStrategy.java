package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * A superclass of a strategy design pattern for collision strategies for bricks.
 */
public class CollisionStrategy {
    /**
     * A collection of the game's objects.
     */
    protected final GameObjectCollection gameObjects;

    /**
     * The constructor of the Strategy object.
     *
     * @param gameObjects An object which holds all game objects of the game running.
     */
    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen.
     *
     * @param collidedObj   The object that was collided (the brick)
     * @param colliderObj   The object that collided with the brick (the ball).
     * @param bricksCounter Counts the current number of bricks in game.
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        if (gameObjects.removeGameObject(collidedObj)) {
            bricksCounter.decrement();
        }
    }
}
