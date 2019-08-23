package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Rule {
    // Operator keys for the applyFuncs maps in subclasses. Must all be unique
    static final int INVALID = -1;
    public static final int ADD = 0;
    public static final int SUBTRACT = 1;
    public static final int MULTIPLY = 2;
    public static final int DIVIDE = 3;
    public static final int PAD = 4;
    public static final int SIGN = 5;
    public static final int DELETE = 6;
    public static final int CONVERT = 7;
    public static final int POWER = 8;
    public static final int REVERSE = 9;
    public static final int SUM = 10;
    public static final int SHIFT_RIGHT = 11;
    public static final int SHIFT_LEFT = 12;
    public static final int MIRROR = 13;
    public static final int META_ADD = 14;
    public static final int STORE = 15;
    public static final int INVERSE_TEN = 16;
    static final int UPDATE_STORE = 17;

    /** The index associated with the operator */
    private int operator;

    Rule(int operator) {
        setOperator(operator);
    }

    public static Rule of(String ruleString) {
        int operator = INVALID, operand1 = 0;
        String convertString = Config.OPERATOR_STRINGS[CONVERT];
        String inverseTenString = Config.OPERATOR_STRINGS[INVERSE_TEN];
        Matcher convertMatcher =
            Pattern
                .compile("\\d+" + convertString + "\\d+")
                .matcher(ruleString);
        Matcher inverseTenMatcher =
            Pattern.compile(inverseTenString).matcher(ruleString);
        boolean isConvertRule = convertMatcher.find();
        boolean isInverseTenRule = inverseTenMatcher.find();
        if (isConvertRule) {
            int arrowIndex = ruleString.indexOf(convertString);
            String op1 = ruleString.substring(0, arrowIndex);
            String op2 = ruleString.substring(arrowIndex + 2);
            return of(CONVERT, op1, op2);
        }

        if (isInverseTenRule) {
            return of(INVERSE_TEN);
        }

        Matcher matcher = Pattern.compile("-?\\d+").matcher(ruleString);
        boolean hasOperand = matcher.find();

        // Don't accidentally pad a negative
        String operatorString =
            hasOperand ? ruleString.substring(0, matcher.start()) : ruleString;
        if (operatorString.equals("") && ruleString.charAt(0) == '-') {
            operator = SUBTRACT; // the minus was for subtraction
            // skip the minus sign in the operand
            operand1 = Integer.parseInt(ruleString.substring(1));
            return of(operator, operand1);
        }

        // We have a basic rule of the form "[operator][op1?]"
        operator = toOperator(operatorString);
        if (hasOperand) {
            operand1 = Integer.parseInt(matcher.group());
        }
        return of(operator, operand1);
    }

    public static Rule of(int operator) {
        return of(operator, 0, 0);
    }

    public static Rule of(int operator, int operand1) {
        return of(operator, operand1, 0);
    }

    /**
     * Makes a rule from the given operator and operands. If any operand is not
     * necessary for the rule, it is ignored.
     * @param operator
     * @param operand1
     * @param operand2
     * @return
     */
    public static Rule of(int operator, int operand1, int operand2) {
        switch (operator) {
            case ADD:
                return new OneRule(ADD, operand1);
            case SUBTRACT:
                return new OneRule(SUBTRACT, operand1);
            case MULTIPLY:
                return new OneRule(MULTIPLY, operand1);
            case DIVIDE:
                return new OneRule(DIVIDE, operand1);
            case PAD:
                return new OneRule(PAD, operand1);
            case SIGN:
                return new ZeroRule(SIGN);
            case DELETE:
                return new ZeroRule(DELETE);
            case CONVERT:
                return new ConvertRule(operand1, operand2);
            case POWER:
                return new OneRule(POWER, operand1);
            case REVERSE:
                return new ZeroRule(REVERSE);
            case SUM:
                return new ZeroRule(SUM);
            case SHIFT_RIGHT:
                return new ZeroRule(SHIFT_RIGHT);
            case SHIFT_LEFT:
                return new ZeroRule(SHIFT_LEFT);
            case MIRROR:
                return new ZeroRule(MIRROR);
            case META_ADD:
                return new OneRule(META_ADD, operand1);
            case STORE:
                return new StoreRule();
            case INVERSE_TEN:
                return new ZeroRule(INVERSE_TEN);
            case UPDATE_STORE:
                return new ZeroRule(UPDATE_STORE);
            default:
                throw new RuntimeException("invalid operator: " + operator);
        }
    }

    public static Rule of(int operator, String opString1, String opString2) {
        if (operator == CONVERT) {
            return new ConvertRule(opString1, opString2);
        } else {
            return of(
                operator,
                Integer.parseInt(opString1),
                Integer.parseInt(opString2)
            );
        }
    }

    /**
     * Transform given synonym into the method name of the corresponding
     * operator
     *
     * @param synonym a potential synonym for an operator method name
     * @return the method name if the synonym is recognized
     * @throws RuntimeException when synonym is not recognized
     */
    private static int toOperator(String synonym) {
        synonym = synonym.trim().toLowerCase();
        for (int i = 0; i < Config.OPERATOR_STRINGS.length; i++) {
            if (synonym.equalsIgnoreCase(Config.OPERATOR_STRINGS[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid operator: " + synonym);
    }

    private void setOperator(int operatorIndex) {
        this.operator = operatorIndex;
    }

    public int getOperator() {
        return operator;
    }

    /**
     * Return a new game that is the result of applying this rule to the given
     * game.
     */
    public abstract CalculatorGame apply(CalculatorGame game);

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rule)) return false;
        Rule otherOneRule = (Rule) other;
        return otherOneRule.getOperator() == getOperator();
    }

    @Override
    public String toString() {
        return Config.ruleString(getOperator());
    }

    /**
     * @return a level made from the given parameters. Returns null if any
     * parameters are invalid
     */
    protected static CalculatorGame makeCalculatorGame(
        int value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        return makeCalculatorGame(
            String.valueOf(value),
            goal,
            moves,
            rules,
            portals
        );
    }

    /**
     * @return a level made from the given parameters. Returns null if any
     * parameters are invalid
     */
    protected static CalculatorGame makeCalculatorGame(
        String valueString,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        try {
            return new CalculatorGame(
                Integer.parseInt(valueString),
                goal,
                moves,
                rules,
                portals
            );
        } catch (NumberFormatException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

class ZeroRule extends Rule {
    /**
     * The application functions for zero rules. Access via getApplyFuncs() as
     * it is a singleton
     */
    private static Map<
        Integer,
        BiFunction<CalculatorGame, ZeroRule, CalculatorGame>> applyFuncs = null;

    private BiFunction<CalculatorGame, ZeroRule, CalculatorGame> applyFunc;

    /**
     * Creates a new ZeroRule from the given operator
     * @param operator must be an operator that corresponds to a rule with zero
     * operands
     */
    ZeroRule(int operator) {
        super(operator);
        applyFunc = getApplyFuncs().get(operator);
    }

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return applyFunc.apply(game, this);
    }

    /**
     * @return the apply functions for ZeroRule instances
     */
    private static
        Map<Integer, BiFunction<CalculatorGame, ZeroRule, CalculatorGame>>
        getApplyFuncs() {

        if (applyFuncs != null) return applyFuncs;
        applyFuncs = new HashMap<>();
        applyFuncs.put(SIGN, (g, r) -> {
            return makeCalculatorGame(
                -g.getValue(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(DELETE, (g, r) -> {
            String valString = String.valueOf((int) g.getValue());
            valString = valString.substring(0, valString.length() - 1);
            if (valString.length() == 0 || valString.equals("-"))
                valString = "0";
            return makeCalculatorGame(
                valString,
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(REVERSE, (g, r) -> {
            boolean negative = g.getValue() < 0;
            String valString = String.valueOf((int) g.getValue());
            if (negative) {
                valString = valString.substring(1); // shave off minus sign
            }
            valString = new StringBuilder(valString).reverse().toString();
            valString = negative ? "-" + valString : valString; // fix the sign
            return makeCalculatorGame(
                valString,
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(SUM, (g, r) -> {
            int value = g.getValue();
            int sum = 0;
            while (value != 0) {
                sum += value % 10;
                value /= 10;
            }
            return makeCalculatorGame(
                sum,
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(SHIFT_RIGHT, (g, r) -> {
            return makeCalculatorGame(
                RuleUtils.shiftRight(g.getValue()),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(SHIFT_LEFT, (g, r) -> {
            return makeCalculatorGame(
                RuleUtils.shiftLeft(g.getValue()),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(MIRROR, (g, r) -> {
            int value = g.getValue();
            boolean negative = value < 0;
            String valString = String.valueOf((int) value);
            if (negative) {
                valString = valString.substring(1); // shave off minus sign
            }
            // add reversed string to end of current string
            valString += new StringBuilder(valString).reverse().toString();
            valString = negative ? "-" + valString : valString;
            return makeCalculatorGame(
                valString,
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(INVERSE_TEN, (g, r) -> {
            char[] valCharArr =
                String.valueOf((int) g.getValue()).toCharArray();

            for (int i = 0; i < valCharArr.length; i++) {
                char element = valCharArr[i];
                if (Character.isDigit(element)) { // why we need chars, not ints
                    int digit = element - '0'; // convert to digit
                    digit = (10 - digit) % 10;
                    // assign new value as character into array
                    valCharArr[i] = (char) (digit + '0');
                }
            }

            return makeCalculatorGame(
                new String(valCharArr),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(UPDATE_STORE, (g, r) -> {
            StoreRule updatedStoreRule = new StoreRule(g.getValue());
            Rule[] newRules = g.getRules();
            int storeRuleIndex = RuleUtils.getStoreRuleIndex(g.getRules());
            newRules[storeRuleIndex] = updatedStoreRule;
            return makeCalculatorGame(
                g.getValue(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                newRules,
                g.getPortals()
            );
        });
        return applyFuncs;
    }
}

class OneRule extends Rule {
    private final int OPERAND;
    private static Map<
        Integer,
        BiFunction<CalculatorGame, OneRule, CalculatorGame>> applyFuncs = null;

    private BiFunction<CalculatorGame, OneRule, CalculatorGame> applyFunc;

    OneRule(int operator, int operand) {
        super(operator);
        OPERAND = operand;
        applyFunc = getApplyFuncs().get(operator);
    }

    public int getOperand() {
        return OPERAND;
    }

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return applyFunc.apply(game, this);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof OneRule)) return false;
        OneRule otherOneRule = (OneRule) other;
        return otherOneRule.getOperator() == getOperator()
            && otherOneRule.getOperand() == getOperand();
    }

    @Override
    public String toString() {
        return Config.ruleString(getOperator(), getOperand());
    }

    private static
        Map<Integer, BiFunction<CalculatorGame, OneRule, CalculatorGame>>
        getApplyFuncs() {

        if (applyFuncs != null) return applyFuncs;
        applyFuncs = new HashMap<>();
        applyFuncs.put(ADD, (g, r) -> {
            return makeCalculatorGame(
                g.getValue() + r.getOperand(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(SUBTRACT, (g, r) -> {
            return makeCalculatorGame(
                g.getValue() - r.getOperand(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(MULTIPLY, (g, r) -> {
            return makeCalculatorGame(
                g.getValue() * r.getOperand(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(DIVIDE, (g, r) -> {
            return makeCalculatorGame(
                g.getValue() / r.getOperand(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(PAD, (g, r) -> {
            return makeCalculatorGame(
                String.valueOf(g.getValue()) + r.getOperand(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(POWER, (g, r) -> {
            return makeCalculatorGame(
                (int) Math.pow(g.getValue(), r.getOperand()),
                g.getGoal(),
                g.getMovesLeft() - 1,
                g.getRules(),
                g.getPortals()
            );
        });
        applyFuncs.put(META_ADD, (g, r) -> {
            Rule[] oldRules = g.getRules();
            Rule[] newRules = new Rule[oldRules.length];
            int newOperand1;
            for (int i = 0; i < newRules.length; i++) {
                Rule oldRule = oldRules[i];
                // Changes the operands of all non-"Meta add" rules
                if (
                    oldRule instanceof OneRule
                        && oldRule.getOperator() != META_ADD
                ) {
                    newOperand1 = ((OneRule) oldRule).getOperand();
                    newOperand1 += r.getOperand();
                    newRules[i] = Rule.of(oldRule.getOperator(), newOperand1);
                } else {
                    newRules[i] = oldRules[i];
                }
            }
            return makeCalculatorGame(
                g.getValue(),
                g.getGoal(),
                g.getMovesLeft() - 1,
                newRules,
                g.getPortals()
            );
        });

        return applyFuncs;
    }
}

class ConvertRule extends Rule {
    private final String OP_STRING_1;
    private final String OP_STRING_2;

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        int value = game.getValue();
        String valString =
            String.valueOf(value).replace(OP_STRING_1, OP_STRING_2);
        return makeCalculatorGame(
            valString,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    ConvertRule(int operand1, int operand2) {
        this(String.valueOf(operand1), String.valueOf(operand2));
    }

    /**
     * Creates a Convert Rule with the given operand strings
     *
     * Assumes each operand string is a valid integer
     * @param opString1 the "from" operand
     * @param opString2 the "to" operand
     */
    ConvertRule(String opString1, String opString2) {
        super(CONVERT);
        OP_STRING_1 = opString1;
        OP_STRING_2 = opString2;
    }

    @Override
    public String toString() {
        String s = "";
        s += OP_STRING_1;
        s += Config.OPERATOR_STRINGS[CONVERT];
        s += OP_STRING_2;
        return s;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ConvertRule)) return false;
        ConvertRule otherConvertRule = (ConvertRule) other;
        return OP_STRING_1.equals(otherConvertRule.OP_STRING_1)
            && OP_STRING_2.equals(otherConvertRule.OP_STRING_2);
    }
}

/**
 * Operates similar to the PadRule, but must be initialized. Also, it can change
 * operand
 */
class StoreRule extends OneRule {
    final boolean INITIALIZED;

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        // Do nothing if uninitialized or set to pad negative
        if (!INITIALIZED || getOperand() < 0) return null;

        // pad the value
        String valString = String.valueOf((int) game.getValue());
        valString += getOperand();
        return makeCalculatorGame(
            valString,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    StoreRule() {
        super(STORE, 0);
        INITIALIZED = false;
    }

    StoreRule(int value) {
        super(STORE, value);
        INITIALIZED = true;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) return false;
        StoreRule otherStoreRule = (StoreRule) other;
        return INITIALIZED == otherStoreRule.INITIALIZED;
    }

    @Override
    public String toString() {
        return Config.OPERATOR_STRINGS[STORE];
    }
}

class RuleUtils {
    /**
     * @return the index of the Store rule of this game.
     * @throws IllegalArgumentException if there isn't a store rule
     */
    static int getStoreRuleIndex(Rule[] rules) {
        for (int i = 0; i < rules.length; i++) {
            if (rules[i].getOperator() == Rule.STORE) {
                return i;
            }
        }
        throw new IllegalArgumentException(
            "The given array must have a Store rule"
        );
    }

    /**
     * Shifts each digit in the value one left.
     * f(1234) -> 2341.
     * PRECONDITION: no chance of integer overflow (i.e. not shifting >=
     * 1,220,000,000)
     * @return the shifted value
     */
    static int shiftLeft(int value) {
        return shift(value, true);
    }

    /**
     * Shifts each digit in the value one right.
     * f(1234) -> 4123.
     * PRECONDITION: no chance of integer overflow (i.e. not shifting >=
     * 1,000,000,003)
     * @return the shifted value
     */
    static int shiftRight(int value) {
        return shift(value, false);
    }

    /**
     * Returns the value represented by the given array.
     * f([1, 2, 3, 4]) -> 1234.
     * f([]) -> undefined.
     * f(null) -> undefined.
     * PRECONDITION: no chance of integer overflow
     * @param digits the array to evaluate
     */
    static int valueOf(int[] digits) {
        int value = 0;

        for (int i = 0; i < digits.length; i++) {
            value *= 10;
            value += digits[i];
        }

        return value;
    }

    /**
     * Returns an array of digits for this value
     * f(1234) -> [1, 2, 3, 4].
     * f(-2) -> [2].
     * f(0) -> [0].
     *
     * @return an array representation of the absolute value of the given value
     */
    private static int[] digits(int value) {
        value = Math.abs(value); // only interested in its digits, not its sign
        int numDigits = Helpers.numDigits(value);
        int[] digits = new int[numDigits];

        for (int i = numDigits - 1; i >= 0; i--) { // start at the end, go back
            digits[i] = value % 10;
            value /= 10;
        }

        return digits;
    }

    private static int shift(int value, boolean left) {
        boolean positive = value >= 0;
        int[] digits = digits(value);

        // shift the digits
        if (left) {
            int first = digits[0];
            for (int i = 0; i < digits.length - 1; i++) {
                digits[i] = digits[i + 1];
            }
            digits[digits.length - 1] = first; // and the first shall be last
        } else {
            int last = digits[digits.length - 1];
            for (int i = digits.length - 1; i > 0; i--) {
                digits[i] = digits[i - 1];
            }
            digits[0] = last; // and the last shall be first
        }

        int newValue = RuleUtils.valueOf(digits);
        return positive ? newValue : -newValue;

    }
}
