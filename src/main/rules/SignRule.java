package rules;

public class SignRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                -game.getValue(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public SignRule() {
        super(Config.SIGN);
    }
}
