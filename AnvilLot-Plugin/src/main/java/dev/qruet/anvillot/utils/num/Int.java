package dev.qruet.anvillot.utils.num;

/**
 * @author qruet
 */
public class Int {

    /**
     * Parses String into Integer value
     *
     * @param val
     * @return -1 if String value is not an integer
     */
    public static int P(String val) {
        try {
            return (int) Double.parseDouble(val);
        } catch (NullPointerException | NumberFormatException e) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e1) {
                return -1;
            }
        }
    }

}
