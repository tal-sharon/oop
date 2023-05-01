/**
 * A single Factory class for a Renderer interface.
 */
public class RendererFactory {

    public static final String CONSOLE = "console";
    public static final String NONE = "none";

    /**
     * Build a renderer by a given type and size.
     * @param type: String - The Renderer's type.
     * @param size: int - The board's size for the ConsoleRenderer's constructor.
     * @return a Renderer instance of given type.
     */
    public Renderer buildRenderer(String type, int size) {
        String lowerCaseType = type.toLowerCase();
        switch (lowerCaseType) {
            case CONSOLE:
                return new ConsoleRenderer(size);
            case NONE:
                return new VoidRenderer();
            default:
                return null;
        }
    }
}