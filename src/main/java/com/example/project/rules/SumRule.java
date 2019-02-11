package com.example.project.rules;

import com.example.project.Config;
import com.example.project.Game;

public class SumRule extends Rule {
    public Game apply(Game game) {
        int absValue = (int) game.getValue();
        int sum = 0;
        while (absValue != 0) {
            sum += absValue % 10;
            absValue /= 10;
        }
        return new Game(
            sum,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public SumRule() {
        super(Config.SUM);
    }
}
