package rules;

import base.Helpers;

public abstract class ShiftRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        boolean negative = game.getValue() < 0;
        int[] digits = Helpers.digits(game.getValue());
        rotate(digits);
        int newValue = valueOf(digits);
        newValue = negative ? -newValue : newValue;
        return CalculatorGame
            .generateGame(
                newValue,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public ShiftRule(boolean left) {
        super(left ? Config.SHIFT_LEFT : Config.SHIFT_RIGHT);
    }

    protected abstract void rotate(int[] digits);

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
     * <p>
     * PRECONDITION: no chance of integer overflow
     * @param digits the array to evaluate
     */
    // TODO enforce no integer overflow
    protected static int valueOf(int[] digits) {
        int value = 0;

        for (int i = 0; i < digits.length; i++) {
            value *= 10;
            value += digits[i];
        }

        return value;
    }
}
