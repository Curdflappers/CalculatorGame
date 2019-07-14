package rules;

import base.Config;
import base.CalculatorGame;

public class ReverseRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        boolean negative = game.getValue() < 0;
        String valString = String.valueOf((int) game.getValue());
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // Reverse the string
        valString = new StringBuilder(valString).reverse().toString();
        double newValue = Double.parseDouble(valString);
        newValue = negative ? -newValue : newValue; // fix the sign
        return new CalculatorGame(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public ReverseRule() {
        super(Config.REVERSE);
    }
}
