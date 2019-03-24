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

    /**
     * Returns the digit of value at the specified index. Only works with
     * nonnegative numbers.
     * @param value the value to get the digit from
     * @param digitIndex the zero-based distance to the left of the ones place
     * (0 is ones place, 1 is tens place, 2 is hundreds place, etc.). Must be
     * nonnegative.
     */
    public static int getDigit(int value, int digitIndex) {
        return (int) (value / Math.pow(10, digitIndex)) % 10;
    }

    /**
     * Returns the value of only the digits to the left
     * Example: digitsToTheLeft(12345, 2) --> 12
     * Only works with positive values
     * @param value the value to take digits from
     * @param digitIndex the zero-based distance to the left of the ones place
     * (0 is ones place, 1 is tens place, 2 is hundreds place, etc.). Must be
     * nonnegative.
     */
    public static int digitsToTheLeft(int value, int digitIndex) {
        return value / (int) Math.pow(10, digitIndex + 1);
    }
}
