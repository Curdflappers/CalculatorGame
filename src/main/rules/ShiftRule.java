package rules;

import base.Config;
import base.Game;

public abstract class ShiftRule extends Rule {
    public Game apply(Game game) {
        boolean negative = game.getValue() < 0;
        int[] digits = ShiftRule.digits((int) game.getValue());
        rotate(digits);
        double newValue = valueOf(digits);
        newValue = negative ? -newValue : newValue;
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public ShiftRule(boolean left) {
        super(left ? Config.SHIFT_LEFT : Config.SHIFT_RIGHT);
    }

    protected abstract void rotate(int[] digits);

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
     * Rotates the given array right once
     * <p>
     * <code>rotateRight([1, 2, 3, 4])</code> changes the argument to
     * <code>[4, 1, 2, 3]</code>
     *
     * @param digits an array of integers
     */
    protected static void rotateRight(int[] digits) {
        int last = digits[digits.length - 1];
        for (int i = digits.length - 1; i > 0; i--) {
            digits[i] = digits[i - 1];
        }
        digits[0] = last; // and the last shall be first
    }

    /**
     * Rotates the given array right once
     * <p>
     * <code>rotateLeft([1, 2, 3, 4])</code> changes the argument to
     * <code>[2, 3, 4, 1]</code>
     *
     * @param digits an array of integers
     */
    protected static void rotateLeft(int[] digits) {
        int first = digits[0];
        for (int i = 0; i < digits.length - 1; i++) {
            digits[i] = digits[i + 1];
        }
        digits[digits.length - 1] = first; // and the first shall be last
    }

    /**
     * Returns the value represented by the given array
     * <p>
     * <code>valueOf([1, 2, 3, 4]) returns 1234</code>
     * <p>
     * <code>valueOf([0]) returns 0</code>
     * <p>
     * <code>valueOf([]) returns 0</code>
     * @param digits the array to evaluate
     */
    protected static int valueOf(int[] digits) {
        int value = 0;

        for (int i = 0; i < digits.length; i++) {
            value *= 10;
            value += digits[i];
        }

        return value;
    }
}
