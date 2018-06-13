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
        if (hasInt) { setOperand(matcher.group()); }
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
            case ("add"):
            case ("plus"):
            case ("+"):
                return Config.ADD;
            case ("subtract"):
            case ("sub"):
            case ("minus"):
            case ("-"):
                return Config.SUBTRACT;
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
            if (this.operand > Config.NUM_OPERANDS) {
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
    
    public String toString() {
        String s = "";
        switch (operator) {
            case (Config.ADD):
                s += "+";
                break;
            case (Config.SUBTRACT):
                s += "-";
                break; 
        }
        s += operand;
        return s;
    }
}
