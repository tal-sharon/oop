package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.trees.Banana;

import java.awt.event.KeyEvent;

/**
 * The game's Avatar class.
 * The avatar is the user's way to interact with the simulation or game.
 * The avatar can move left and right, jump.
 * The avatar has energy, which he uses to fly.
 *      While flying energy reduces and while on solid block such as terrain or tree can be gained.
 * This game's avatar is a monkey named 'Kofiko' (a famous VIP in Israel),
 *      therefore, he can eat bananas to gain more energy.
 */
public class Avatar extends GameObject {
    private static final int MAX_ENERGY = 100;
    private static final int MOVEMENT_SPEED = 300;
    private static final double ENERGY_UPDATE = 0.5;
    public static final String AVATAR_TAG = "avatar";
    private static final int AVATAR_SIZE = 60;
    private static final String STAND_IMG_PATH = "assets/monkey_stand.png";
    private static final String JUMP_IMG_PATH = "assets/monkey_jump.png";
    private static final String FLY_IMG_PATH = "assets/monkey_fly.png";
    private static final String EAT_IMG_PATH = "assets/monkey_eating.png";
    private static final String DEAD_IMG_PATH = "assets/monkey_dead.png";
    private static final String FIRST_WALK_IMG_PATH = "assets/monkey_walk_0.png";
    private static final String SECOND_WALK_IMG_PATH = "assets/monkey_walk_1.png";
    private static final int BANANA_ENERGY_BONUS = 50;
    private static final float FALL_GRADIENT = 0.57f;
    private static final int ATE_BANANA_TIME = 20;
    private static final int WALKING_CYCLE_LENGTH = 10;
    private static final float JUMP_SPEED = 10f;
    private int ateBanana;
    private final UserInputListener inputListener;
    private float energy;
    private int walkingPosition;
    private ImageRenderable monkeyJumpImg;
    private ImageRenderable monkeyFlyImg;
    private ImageRenderable monkeyEatImg;
    private ImageRenderable monkeyDeadImg;
    private ImageRenderable monkeyStandImg;
    private ImageRenderable firstMonkeyWalkImg;
    private ImageRenderable secondMonkeyWalkImg;

    /**
     * Construct a new Avatar GameObject instance.
     * Initializes all the Avatar's parameters.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener The game's input listener - used to get input from the user.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        walkingPosition = 0;
        ateBanana = 0;
    }

    /**
     * Create a new Avatar and adds it to the game.
     * Sets the Avatar's physics.
     * @param gameObjects GameObjectCollection: The game's objects.
     * @param layer The Avatar's layer.
     * @param topLeftCorner The Avatar's tpo left corner.
     * @param inputListener The game's input listener - used to get input from the user.
     * @param imageReader The game's image reader - used to read new images into renderables.
     * @return a new Avatar instance.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        ImageRenderable monkeyStandImg = imageReader.readImage(STAND_IMG_PATH, true);
        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_SIZE, AVATAR_SIZE), monkeyStandImg,
                inputListener);
        setImages(imageReader, monkeyStandImg, avatar);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.setTag(AVATAR_TAG);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * Reads all the avatar's images to renderables.
     * @param imageReader The game's image reader - used to read new images into renderables.
     * @param monkeyStandImg ImageRenderable of the avatar standing position.
     * @param avatar The game's avatar instance.
     */
    private static void setImages(ImageReader imageReader, ImageRenderable monkeyStandImg, Avatar avatar) {
        avatar.monkeyJumpImg = imageReader.readImage(JUMP_IMG_PATH, true);
        avatar.monkeyFlyImg = imageReader.readImage(FLY_IMG_PATH, true);
        avatar.monkeyEatImg = imageReader.readImage(EAT_IMG_PATH, true);
        avatar.monkeyDeadImg = imageReader.readImage(DEAD_IMG_PATH, true);
        avatar.firstMonkeyWalkImg = imageReader.readImage(FIRST_WALK_IMG_PATH, true);
        avatar.secondMonkeyWalkImg = imageReader.readImage(SECOND_WALK_IMG_PATH, true);
        avatar.monkeyStandImg = monkeyStandImg;
    }

    /**
     * Defines avatar's behaviour upon collision.
     * When Avatar collides with a banana object,
     *      sets the avatar renderable to an eating image, and increases energy.
     *      Sets AteBanana - which influences the object's update method.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Banana.BANANA_TAG)) {
            renderer().setRenderable(monkeyEatImg);
            energy = Math.min(MAX_ENERGY, energy + BANANA_ENERGY_BONUS);
            ateBanana = ATE_BANANA_TIME;
        }
    }

    /**
     * If avatar ate banana, reduces the factor until it's 0.
     * Updates the Avatar's location according to the user's key press.
     * If avatar is flying or standing, updates its energy accordingly.
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
        if (ateBanana > 0) {
            ateBanana --;
            return;
        }
        Vector2 movementDir = Vector2.ZERO;
        movementDir = calcAvatarMovement(movementDir);
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        if (getVelocity().y() == 0) {
            energy = (float) Math.min(MAX_ENERGY, energy + ENERGY_UPDATE);
        }
        if (energy == 0) {
            renderer().setRenderable(monkeyDeadImg);
        }
    }

    /**
     * Gets the user's input and calculates the avatar's movement direction and parameters accordingly.
     * @param movementDir The avatar's movement direction.
     * @return The updated avatar's movement direction.
     */
    private Vector2 calcAvatarMovement(Vector2 movementDir) {
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            updateWalkingImg();
            movementDir = movementDir.add(Vector2.LEFT);
            renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            updateWalkingImg();
            movementDir = movementDir.add(Vector2.RIGHT);
            renderer().setIsFlippedHorizontally(false);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            renderer().setRenderable(monkeyJumpImg);
            movementDir = new Vector2(movementDir.x(), 0);
            movementDir = movementDir.add(Vector2.UP.mult(JUMP_SPEED));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && energy > 0) {
            renderer().setRenderable(monkeyFlyImg);
            movementDir = new Vector2(movementDir.x(), 0);
            movementDir = movementDir.add(Vector2.UP);
            energy = (float) Math.max(0, energy - ENERGY_UPDATE);
        } else {
            movementDir = movementDir.add(Vector2.DOWN.mult(FALL_GRADIENT));
            setStandOrLand();
        }
        return movementDir;
    }

    /**
     * Checks if avatar is standing or landing, and act accordingly.
     */
    private void setStandOrLand() {
        if (getVelocity().y() == 0) {
            if (!inputListener.isKeyPressed(KeyEvent.VK_LEFT) &
                    !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                renderer().setRenderable(monkeyStandImg);
            }
            energy = (float) Math.min(MAX_ENERGY, energy + ENERGY_UPDATE);
        } else {
            renderer().setRenderable(monkeyJumpImg);
        }
    }

    /**
     * Switches the avatar's walking renderable according to a running mechanism and a walking cycle.
     */
    private void updateWalkingImg() {
        walkingPosition = (walkingPosition + 1) % WALKING_CYCLE_LENGTH;
        if (walkingPosition < (WALKING_CYCLE_LENGTH / 2)) {
            renderer().setRenderable(firstMonkeyWalkImg);
        } else {
            renderer().setRenderable(secondMonkeyWalkImg);
        }
    }

    /**
     * Getter: gets the avatar's current energy level.
     * @return int: the avatar's energy.
     */
    public int getEnergy() {
        return (int) energy;
    }
}
