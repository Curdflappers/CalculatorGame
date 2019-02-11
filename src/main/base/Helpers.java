package base;

public class Helpers {

    /**
     * Returns an array of digits for this value
     * <p>
     * <code>digits(1234) returns [1, 2, 3, 4]</code>
     * <p>
     * <code>digits(-2) returns [2]</code>
     * <p>
     * <code>digits(0) returns [0]</code>
     *
     * @param value any integer
     * @return an array representation of the absolute value of value
     */
    public static int[] digits(int value) {
        value = Math.abs(value); // only interested in its digits, not its sign
        int numDigits = value == 0 ? 1 : (int) Math.ceil(Math.log10(value));
        int[] digits = new int[numDigits];

        for (int i = numDigits - 1; i >= 0; i--) { // start at the end, go back
            digits[i] = value % 10;
            value /= 10;
        }

        return digits;
    }
}
