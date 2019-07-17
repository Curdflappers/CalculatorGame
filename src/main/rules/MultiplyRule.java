package rules;

public class MultiplyRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                game.getValue() * getOperand1(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public MultiplyRule(int operand1) {
        super(Config.MULTIPLY, operand1);
    }
}
