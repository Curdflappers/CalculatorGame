package main;

import java.lang.reflect.Method;

public class Rule {
    private int operand;
    Method operator;

    public Rule(String rule) {
        String[] input = rule.split(" ");
        setOperator(input[0]);
        if (input.length > 1) {
            setOperand(input[1]);
        }
    }

    public int add(Integer start) {
        return start + operand;
    }

    private void setOperator(String methodName) {
        try {
            this.operator = Rule.class.getDeclaredMethod(methodName,
                new Class[] {Integer.class});
        } catch (NoSuchMethodException e) {
            System.out.println(
                "Unexpected NoSuchMethodException in Rule.setOperator");
            e.printStackTrace();
        }
    }

    private void setOperand(String operand) {
        try {
            this.operand = Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            System.out
                .println("Unexpected NumberFormatException in Rule.setOperand");
            e.printStackTrace();
        }
    }

    public Method getOperator() {
        return operator;
    }

    public int getOperand() {
        return operand;
    }
}
