package calculatorgame;

public class ConvertRule extends Rule {

    private final String OP_STRING_1;
    private String OP_STRING_2;

    public CalculatorGame apply(CalculatorGame game) {
        int value = game.getValue();
        String valString = String.valueOf((int) value);
        valString = valString.replace(OP_STRING_1, OP_STRING_2);
        return CalculatorGame
            .generateGame(
                valString,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public ConvertRule(int operand1, int operand2) {
        super(Config.CONVERT, operand1, operand2);
        OP_STRING_1 = String.valueOf(getOperand1());
        OP_STRING_2 = String.valueOf(getOperand2());
    }

    /**
     * Creates a Convert Rule with the given operand strings
     *
     * Assumes each operand string is a valid integer
     * @param opString1 the "from" operand
     * @param opString2 the "to" operand
     */
    public ConvertRule(String opString1, String opString2) {
        super(
            Config.CONVERT,
            Integer.parseInt(opString1),
            Integer.parseInt(opString2)
        );
        OP_STRING_1 = opString1;
        OP_STRING_2 = opString2;
    }

    public String toString() {
        String s = "";
        s += OP_STRING_1;
        s += Config.OPERATOR_STRINGS[Config.CONVERT];
        s += OP_STRING_2;
        return s;
    }
}
