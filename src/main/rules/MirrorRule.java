package rules;

import base.Config;
import base.Game;

public class MirrorRule extends Rule {
    public Game apply(Game game) {
        double value = game.getValue();
        boolean negative = value < 0;
        String valString = String.valueOf((int) value);
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // add reversed string to end of current string
        valString += new StringBuilder(valString).reverse().toString();
        double newValue = Double.parseDouble(valString);
        newValue = negative ? -newValue : newValue;
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public MirrorRule() {
        super(Config.MIRROR);
    }
}
