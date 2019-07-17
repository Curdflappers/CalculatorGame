package calculatorgame;

public class PadRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        String valString = String.valueOf((int) game.getValue());
        valString += getOperand1();
        return CalculatorGame
            .generateGame(
                valString,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public PadRule(int operand1) {
        super(Config.PAD, operand1);
    }
}
