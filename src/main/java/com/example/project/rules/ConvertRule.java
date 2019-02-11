package com.example.project.rules;

import com.example.project.base.Config;
import com.example.project.base.Game;

public class ConvertRule extends Rule {

    private final String OP_STRING_1;
    private String OP_STRING_2;

    public Game apply(Game game) {
        double value = game.getValue();
        String valString = String.valueOf((int) value);
        valString = valString.replace(OP_STRING_1, OP_STRING_2);
        double newValue = Double.parseDouble(valString);
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
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
}
