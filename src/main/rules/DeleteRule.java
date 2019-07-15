package rules;

import base.Config;
import base.CalculatorGame;

public class DeleteRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        String valString = String.valueOf((int) game.getValue());
        valString = valString.substring(0, valString.length() - 1);
        if (valString.length() == 0 || valString.equals("-")) valString = "0";
        return CalculatorGame.generateGame(
            valString,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    public DeleteRule() {
        super(Config.DELETE);
    }
}
