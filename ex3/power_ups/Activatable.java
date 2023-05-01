package src.power_ups;

/**
 * An interface of Activatable objects called Power-Ups.
 * Each Activatable can be activated, and you can get its name.
 */
public interface Activatable {
    /**
     * Activates the instance.
     */
    void activate();

    /**
     * Getter: gets the instances name.
     *
     * @return String: The instances name.
     */
    String getName();
}
