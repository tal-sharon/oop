import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.power_ups.*;

import static src.power_ups.CameraPowerUp.CAMERA;
import static src.power_ups.LifePowerUp.LIFE;
import static src.power_ups.PaddlePowerUp.PADDLE;
import static src.power_ups.PuckPowerUp.PUCK;

//public class PowerUpFactory {
//
//    public Activatable BuildPowerUp(String powerUpType, BrickerGameManager gameManager,
//                                    GameObjectCollection gameObjects, Renderable renderable, Sound sound,
//                                    UserInputListener inputListener, Vector2 windowDimensions,
//                                    int minDistFromEdge, Vector2 brickTopLeftCorner, Vector2 brickCenter,
//                                    Vector2 brickDimensions, Counter livesCounter, Camera camera) {
//        switch (powerUpType) {
//            case PUCK:
//                return new PuckPowerUp(gameObjects, renderable, sound, brickTopLeftCorner, brickDimensions);
//            case PADDLE:
//                return new PaddlePowerUp(gameObjects, renderable,inputListener,
//                        new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), windowDimensions, minDistFromEdge);
//            case LIFE:
//                return new LifePowerUp(gameObjects, brickCenter, brickDimensions, livesCounter,
//                        renderable, windowDimensions.y());
//            case CAMERA:
//                return new CameraPowerUp(gameManager, camera);
//            default:
//                return null;
//        }
//    }
//}
