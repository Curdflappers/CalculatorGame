package calculatorgame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Rule {
    private int operand1;
    private int operand2; // for convert op1 to op2 rules
    /** The index associated with the operator */
    private int operator;
    private String string;

    public static Rule ruleFromString(String ruleString) {
        int operator = Config.INVALID, operand1 = 0, operand2 = 0;
        String convertString = Config.OPERATOR_STRINGS[Config.CONVERT];
        String inverseTenString = Config.OPERATOR_STRINGS[Config.INVERSE_TEN];
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
            return makeRule(Config.CONVERT, op1, op2);
        }

        if (isInverseTenRule) {
            return makeRule(Config.INVERSE_TEN);
        }

        Matcher matcher = Pattern.compile("-?\\d+").matcher(ruleString);
        boolean hasInt = matcher.find();

        // Don't accidentally pad a negative
        String operatorString =
            hasInt ? ruleString.substring(0, matcher.start()) : ruleString;
        if (operatorString.equals("") && ruleString.charAt(0) == '-') {
            operator = Config.SUBTRACT; // the minus was for subtraction
            // skip the minus sign in the operand
            operand1 = Integer.parseInt(ruleString.substring(1));
            return makeRule(operator, operand1, operand2);
        }

        // We have a basic rule of the form "[operator][op1?]"
        operator = toOperator(operatorString);
        if (hasInt) {
            operand1 = Integer.parseInt(matcher.group());
        }
        return makeRule(operator, operand1, operand2);
    }

    public Rule() {
        this(Config.INVALID, 0, 0);
    }

    public Rule(int operator) {
        setOperator(operator);
        setString();
    }

    public Rule(int operator, int operand1) {
        setOperator(operator);
        setOperand1(operand1);
        setString();
    }

    public Rule(int operator, int operand1, int operand2) {
        setOperator(operator);
        setOperand1(operand1);
        setOperand2(operand2);
        setString();
    }

    public static Rule makeRule(int operator) {
        return makeRule(operator, 0, 0);
    }

    public static Rule makeRule(int operator, int operand1) {
        return makeRule(operator, operand1, 0);
    }

    /**
     * Makes a rule from the given operator and operands. If any operand is not
     * necessary for the rule, it is ignored.
     * @param operator
     * @param operand1
     * @param operand2
     * @return
     */
    public static Rule makeRule(int operator, int operand1, int operand2) {
        switch (operator) {
            case Config.ADD:
                return new AddRule(operand1);
            case Config.SUBTRACT:
                return new SubtractRule(operand1);
            case Config.MULTIPLY:
                return new MultiplyRule(operand1);
            case Config.DIVIDE:
                return new DivideRule(operand1);
            case Config.PAD:
                return new PadRule(operand1);
            case Config.SIGN:
                return new SignRule();
            case Config.DELETE:
                return new DeleteRule();
            case Config.CONVERT:
                return new ConvertRule(operand1, operand2);
            case Config.POWER:
                return new PowerRule(operand1);
            case Config.REVERSE:
                return new ReverseRule();
            case Config.SUM:
                return new SumRule();
            case Config.SHIFT_RIGHT:
                return new ShiftRightRule();
            case Config.SHIFT_LEFT:
                return new ShiftLeftRule();
            case Config.MIRROR:
                return new MirrorRule();
            case Config.META_ADD:
                return new MetaAddRule(operand1);
            case Config.STORE:
                return new StoreRule();
            case Config.INVERSE_TEN:
                return new InverseTenRule();
            default:
                throw new RuntimeException("invalid operator: " + operator);
        }
    }

    public static Rule makeRule(
        int operator,
        String opString1,
        String opString2
    ) {
        if (operator == Config.CONVERT) {
            return new ConvertRule(opString1, opString2);
        } else {
            return makeRule(
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

    private void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    private void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public int getOperator() {
        return operator;
    }

    public int getOperand1() {
        return operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    private void setString() {
        if (operator < 0) {
            string = "INVALID";
            return;
        }
        int numOperands = Config.NUM_OPERANDS[operator];
        switch (numOperands) {
            case 0:
                string = Config.ruleString(operator);
                return;
            case 1:
                string = Config.ruleString(operator, operand1);
                return;
            case 2:
                string = Config.ruleString(operator, operand1, operand2);
                return;
            default:
                throw new RuntimeException(
                    "Unexpected number of operands: " + numOperands
                );
        }
    }

    /**
     * Returns a string representation of this rule.
     * <p>
     * In the form [operator][operand1] (no spaces) e.g. "+1", "*2"
     * <p>
     * Operators are: ADD: "+", SUBTRACT: "-", MULTIPLY: "*", DIVIDE: "/"
     */
    public String toString() {
        return string;
    }

    /**
     * Return a new game that is the result of applying this rule to the given
     * game.
     */
    public abstract CalculatorGame apply(CalculatorGame game);

    @Override
    public boolean equals(Object other) {
        if (other instanceof Rule) {
            Rule otherRule = (Rule) other;
            return otherRule.getOperator() == getOperator()
                && otherRule.getOperand1() == getOperand1()
                && otherRule.getOperand2() == getOperand2();
        }
        return false;
    }
}
