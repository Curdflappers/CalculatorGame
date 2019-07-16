package rules;

import base.Config;

public class ReverseRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        boolean negative = game.getValue() < 0;
        String valString = String.valueOf((int) game.getValue());
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // Reverse the string
        valString = new StringBuilder(valString).reverse().toString();
        valString = negative ? "-" + valString : valString; // fix the sign
        return CalculatorGame
            .generateGame(
                valString,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public ReverseRule() {
        super(Config.REVERSE);
    }
}
