package calculatorgame;

public class PowerRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                (int) Math.pow(game.getValue(), getOperand1()),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public PowerRule(int operand1) {
        super(Config.POWER, operand1);
    }
}
