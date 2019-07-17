package rules;

public class InverseTenRule extends Rule {

    public CalculatorGame apply(CalculatorGame game) {
        char[] valCharArr = String.valueOf((int) game.getValue()).toCharArray();

        for (int i = 0; i < valCharArr.length; i++) {
            char element = valCharArr[i];
            if (Character.isDigit(element)) {
                int digit = element - '0'; // convert to digit
                digit = (10 - digit) % 10;
                // assign new value as character into array
                valCharArr[i] = (char) (digit + '0');
            }
        }

        return CalculatorGame
            .generateGame(
                new String(valCharArr),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    public InverseTenRule() {
        super(Config.INVERSE_TEN);
    }
}
