package calculatorgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Helpers {

    /**
     * Returns the number of digits in the given string.
     *
     * PRECONDITION the string matches the regex -?[0-9]*
     * @param valueString the string to evaluate
     * @return the number of digits in the string
     */
    public static int numDigits(String valueString) {
        int digits = (valueString.charAt(0) == '-') ? -1 : 0;
        digits += valueString.length();
        return digits;
    }

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
        int numDigits = numDigits(String.valueOf(value));
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

    public static <T> List<T> copyAsList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T element : array) {
            list.add(element);
        }
        return list;
    }

    static String expectedSolution(String level) {
        File file = new File(Config.TESTCASES_PATH + "/" + level);
        Scanner input = null;
        try {
            input = new Scanner(file);

            input.nextLine(); // value
            input.nextLine(); // goal
            input.nextLine(); // moves
            while (!input.nextLine().equals("")); // rules

            boolean portalsPresent = input.nextLine().charAt(0) == 'y';
            if (portalsPresent) {
                input.nextLine(); // left portal
                input.nextLine(); // right portal
            }

            String expectedSolutionString = "";
            while (input.hasNextLine()) {
                expectedSolutionString += input.nextLine() + "\n";
            }
            return expectedSolutionString;
        } catch (FileNotFoundException e) {
            System.out.print("FILE NOT FOUND");
            return null;
        } finally {
            if (input != null) input.close();
        }
    }

    /**
     * Loads the level with the given filename from the test-cases directory
     */
    static CalculatorGame loadLevel(String filename) {
        File file = new File(Config.TESTCASES_PATH + "/" + filename);
        Scanner input = null;
        try {
            input = new Scanner(file);

            int value = Integer.parseInt(input.nextLine());
            int goal = Integer.parseInt(input.nextLine());
            int moves = Integer.parseInt(input.nextLine());

            String currentRuleString;
            List<Rule> rules = new ArrayList<>();

            // While the current rule is not the empty string
            while (!(currentRuleString = input.nextLine()).equals("")) {
                Rule rule = Rule.ruleFromString(currentRuleString);
                rules.add(rule);
            }

            boolean portalsPresent = input.nextLine().charAt(0) == 'y';

            int leftPortal = -1;
            int rightPortal = -1;

            if (portalsPresent) {
                leftPortal = Integer.parseInt(input.nextLine());
                rightPortal = Integer.parseInt(input.nextLine());
            }

            int[] portals = null;
            if (portalsPresent) portals = new int[] {
                leftPortal, rightPortal
            };

            return CalculatorGame
                .generateGame(
                    value,
                    goal,
                    moves,
                    rules.toArray(new Rule[rules.size()]),
                    portals
                );
        } catch (FileNotFoundException e) {
            System.out.print("FILE NOT FOUND");
            return null;
        } finally {
            if (input != null) input.close();
        }
    }

    static List<String> testCases() {
        final File folder = new File(Config.TESTCASES_PATH);
        List<String> filenames = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            filenames.add(fileEntry.getName());
        }

        return filenames;
    }
}
