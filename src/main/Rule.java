package main;

import java.lang.reflect.Method;

public class Rule {
    private int operand;
    Method operator;

    public Rule(String rule) {
        String[] input = rule.split(" ");
        setOperator(input[0]);
    }
    
    public int add(Integer start) {
        return start + operand;
    }
    
    private void setOperator(String methodName) {
        try {
            this.operator = Rule.class.getDeclaredMethod(methodName, new Class[] {Integer.class});
        } catch (NoSuchMethodException e) {
            System.out.println(
                "Unexpected NoSuchMethodException in Rule.setOperator");
            e.printStackTrace();
        }
    }
    
    private void setOperand(String operand) {
        
    }

    public Method getOperator() {
        return operator;
    }
}
