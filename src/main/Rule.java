package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    private int operand;
    /** The index associated with the operator */
    private int operator;

    public Rule(String rule) {
        Matcher matcher = Pattern.compile("\\d+").matcher(rule);
        boolean hasInt = matcher.find();
        String operator = hasInt ? rule.substring(0, matcher.start()) : rule;
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
                return Config.MULTIPLY;
            case "divide":
            case "div":
            case "divide by":
            case "/":
                return Config.DIVIDE;
            case "":
                return Config.PAD;
            case "sign":
                return Config.SIGN;
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
            if (this.operand > Config.NUM_OPERANDS || this.operand <= 0) {
                throw new RuntimeException(
                    "Operand out of range: " + this.operand);
            }
        } catch (NumberFormatException e) {
            System.out.println(
                "Unexpected NumberFormatException in Rule.setOperand");
            e.printStackTrace();
        }
    }

    public int getOperator() {
        return operator;
    }

    public int getOperand() {
        return operand;
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
        }
        s += operand;
        return s;
    }
}
