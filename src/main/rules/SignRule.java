package rules;

import base.Config;
import base.Game;

public class SignRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            -game.getValue(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public SignRule() {
        super(Config.SIGN);
    }
}
