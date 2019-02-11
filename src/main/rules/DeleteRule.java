package rules;

import base.Config;
import base.Game;

public class DeleteRule extends Rule {
    public Game apply(Game game) {
        String valString = String.valueOf((int) game.getValue());
        valString = valString.substring(0, valString.length() - 1);
        double newValue;
        if (valString.length() == 0 || valString.equals("-")) {
            newValue = 0;
        } else {
            newValue = Double.parseDouble(valString);
        }
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public DeleteRule() {
        super(Config.DELETE);
    }
}
