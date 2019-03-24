package rules;

import base.Config;
import base.Game;

public class AddRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            game.getValue() + getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public AddRule(int operand1) {
        super(Config.ADD, operand1);
    }
}
