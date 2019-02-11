package rules;

import base.Config;
import base.Game;

public class InverseTenRule extends Rule {

    public Game apply(Game game) {
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

        int newValue = Integer.parseInt(new String(valCharArr));

        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public InverseTenRule() {
        super(Config.INVERSE_TEN);
    }
}
