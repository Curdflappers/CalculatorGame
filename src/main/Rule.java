package main;

import java.lang.reflect.Method;

public class Rule {
    private int operand;
    Method operator;

    public Rule(String rule) {
        String[] input = rule.split(" ");
        setOperator(toOperator(input[0]));
        if (input.length > 1) {
            setOperand(input[1]);
        }
    }

    public int add(Integer start) {
        return start + operand;
    }

    /**
     * Transform given synonym into the method name of the corresponding
     * operator
     * 
     * @param synonym a potential synonym for an operator method name
     * @return the method name if the synonym is recognized
     * @throws RuntimeException when synonym is not recognized
     */
    private static String toOperator(String synonym) {
        switch (synonym) {
            case ("add"):
                return "add";
            case ("plus"):
                return "add";
            case ("+"):
                return "add";
            default:
                throw new RuntimeException("Invalid operator: " + synonym);
        }
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
            System.out.println(
                "Unexpected NumberFormatException in Rule.setOperand");
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
