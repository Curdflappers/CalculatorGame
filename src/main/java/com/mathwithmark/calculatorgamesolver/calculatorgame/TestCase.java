package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mathwithmark.calculatorgamesolver.yaml.Mappable;

public class TestCase implements Mappable {
    public final CalculatorGame GAME;
    public final List<String> SOLUTION;

    public TestCase(CalculatorGame game, List<String> solution) {
        GAME = game;
        SOLUTION = solution;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("game", GAME.toMap());
        map.put("solution", SOLUTION);
        return map;
    }

    /**
     * Returns a test case generated from the given map
     *
     * Undefined behavior if the map is of the wrong format
     * @param map A map loaded from YAML. Expected pairs:
     * game: a game map
     * solution: a list of rule strings
     * @return a TestCase from the given map
     */
    @SuppressWarnings("unchecked")
    public static TestCase from(Map<String, Object> map) {
        CalculatorGame game =
            CalculatorGame.from((Map<String, Object>) map.get("game"));
        List<String> solution = (List<String>) map.get("solution");
        return new TestCase(game, solution);
    }
}
