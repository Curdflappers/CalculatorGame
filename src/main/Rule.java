package main;

public class Rule {
    private int operand;
    /** The index associated with the operator */
    private int operator;

    public Rule(String rule) {
        String[] input = rule.split(" ");
        setOperator(toOperator(input[0]));
        if (input.length > 1) {
            setOperand(input[1]);
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
        switch (synonym) {
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
}
