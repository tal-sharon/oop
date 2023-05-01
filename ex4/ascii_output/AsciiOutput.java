package ascii_output;

/**
 * An object implementing this interface can output a 2D array of chars
 * in some fashion.
 * @author Dan Nirel
 */
public interface AsciiOutput {
    /**
     * Output the specified 2D array of chars
     * @param chars chars to output
     */
    void output(char[][] chars);
}
