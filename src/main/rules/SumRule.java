package rules;

import base.Config;
import base.CalculatorGame;

public class SumRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        int absValue = (int) game.getValue();
        int sum = 0;
        while (absValue != 0) {
            sum += absValue % 10;
            absValue /= 10;
        }
        return new CalculatorGame(
            sum,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public SumRule() {
        super(Config.SUM);
    }
}
