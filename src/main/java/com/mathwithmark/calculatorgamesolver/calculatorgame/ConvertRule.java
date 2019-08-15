package com.mathwithmark.calculatorgamesolver.calculatorgame;

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
        super(CONVERT, operand1, operand2);
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
    ConvertRule(String opString1, String opString2) {
        super(
            CONVERT,
            Integer.parseInt(opString1),
            Integer.parseInt(opString2)
        );
        OP_STRING_1 = opString1;
        OP_STRING_2 = opString2;
    }

    public String toString() {
        String s = "";
        s += OP_STRING_1;
        s += Config.OPERATOR_STRINGS[CONVERT];
        s += OP_STRING_2;
        return s;
    }
}
