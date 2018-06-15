package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    private int operand;
    private int operand2; // for A=>B rules
    /** The index associated with the operator */
    private int operator;

    public Rule(String rule) {
        Matcher convertMatcher = Pattern.compile("\\d+=>\\d+").matcher(rule);
        boolean isConvertRule = convertMatcher.find();
        if (isConvertRule) {
            int arrowIndex = rule.indexOf("=>");
            setOperand(rule.substring(0, arrowIndex));
            setOperand2(rule.substring(arrowIndex + 2));
            setOperator(Config.CONVERT);
            return;
        }

        Matcher matcher = Pattern.compile("-?\\d+").matcher(rule);
        boolean hasInt = matcher.find();

        // Don't accidentally pad a negative
        String operator = hasInt ? rule.substring(0, matcher.start()) : rule;
        if (operator.equals("") && rule.charAt(0) == '-') {
            setOperator(toOperator("-")); // the minus was for subtraction
            setOperand(rule.substring(1)); // skip the minus sign in the operand
            return;
        }
        setOperator(toOperator(operator));
        if (hasInt) {
            setOperand(matcher.group());
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
        switch (synonym.trim()) {
            case "add":
            case "plus":
            case "+":
                return Config.ADD;
            case "subtract":
            case "sub":
            case "minus":
            case "-":
                return Config.SUBTRACT;
            case "multiply":
            case "mul":
            case "mult":
            case "multiply by":
            case "times":
            case "*":
            case "x":
                return Config.MULTIPLY;
            case "divide":
            case "div":
            case "divide by":
            case "/":
                return Config.DIVIDE;
            case "":
                return Config.PAD;
            case "sign":
            case "+/-":
                return Config.SIGN;
            case "delete":
            case "del":
            case "shift":
            case "rshift":
            case "rightshift":
            case "right shift":
            case "<<":
                return Config.DELETE;
            case "=>":
                return Config.CONVERT;
            case "x^":
            case "^":
                return Config.POWER;
            default:
                throw new RuntimeException("Invalid operator: " + synonym);
        }
    }

    private void setOperator(int operatorIndex) {
        this.operator = operatorIndex;
    }

    private void setOperand(String operand) {
        try {
            this.operand = Integer.parseInt(operand);
            if (this.operand > Config.MAX_OPERAND
                || this.operand < Config.MIN_OPERAND) {
                throw new RuntimeException(
                    "Operand out of range: " + this.operand);
            }
        } catch (NumberFormatException e) {
            System.out.println(
                "Unexpected NumberFormatException in Rule.setOperand");
            e.printStackTrace();
        }
    }

    private void setOperand2(String operand2) {
        try {
            this.operand2 = Integer.parseInt(operand2);
            if (this.operand2 > Config.MAX_OPERAND
                || this.operand2 < Config.MIN_OPERAND) {
                throw new RuntimeException(
                    "Operand out of range: " + this.operand2);
            }
        } catch (NumberFormatException e) {
            System.out.println(
                "Unexpected NumberFormatException in Rule.setOperand2");
            e.printStackTrace();
        }
    }

    public int getOperator() {
        return operator;
    }

    public int getOperand() {
        return operand;
    }

    public int getOperand2() {
        return operand2;
    }

    /**
     * Returns a string representation of this rule.
     * <p>
     * In the form [operator][operand] (no spaces) e.g. "+1", "*2"
     * <p>
     * Operators are: ADD: "+", SUBTRACT: "-", MULTIPLY: "*", DIVIDE: "/"
     */
    public String toString() {
        String s = "";
        switch (operator) {
            case Config.ADD:
                s += "+";
                break;
            case Config.SUBTRACT:
                s += "-";
                break;
            case Config.MULTIPLY:
                s += "*";
                break;
            case Config.DIVIDE:
                s += "/";
                break;
            case Config.SIGN:
                return "+/-";
            case Config.DELETE:
                return "<<";
            case Config.CONVERT:
                String op1String = String.valueOf(getOperand());
                String op2String = String.valueOf(getOperand2());
                return op1String + "=>" + op2String;
            case Config.POWER:
                s += "x^";
        }
        s += operand;
        return s;
    }
}
