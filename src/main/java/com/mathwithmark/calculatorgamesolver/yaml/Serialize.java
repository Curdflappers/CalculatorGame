package com.mathwithmark.calculatorgamesolver.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;

import org.yaml.snakeyaml.Yaml;

/**
 * Handles serialization and deserialization of test cases
 */
public class Serialize {
    /**
     * Converts the contents of the given file at the given path to a string
     * @param filePath the path to the file to convert
     * @return the contents of the file at the given path
     */
    private static String fileToString(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        String fileContents = "";
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileContents += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) scanner.close();
        }
        return fileContents;
    }

    /**
     * Converts the given object to an array of rules
     * @param obj an ArrayList<Rule> (type hidden by deserialization)
     * @return an array of rules constructed from the given object
     */
    private static Rule[] toRulesArray(Object obj) {
        ArrayList<?> list = (ArrayList<?>) obj;
        Rule[] rules = new Rule[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rules[i] = Rule.ruleFromString(list.get(i).toString());
        }
        return rules;
    }

    /**
     * Converts the given object to an array of ints, representing the portals
     * @param obj an ArrayList<Integer> (type hidden by deserialization)
     * @return an array of integers constructed from the given object
     */
    private static int[] toPortalsArray(Object obj) {
        if (obj == null) return null;
        ArrayList<?> list = (ArrayList<?>) obj;
        int[] portals = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            portals[i] = Integer.parseInt(list.get(i).toString());
        }
        return portals;
    }

    /**
     * Creates an external representation of the rules of the game
     */
    public static List<String> externalRuleStrings(Rule[] rules) {
        List<String> ruleStrings = new ArrayList<String>();
        for (int i = 0; i < rules.length; i++) {
            if (rules[i].EXTERNAL) ruleStrings.add(rules[i].toString());
        }
        return ruleStrings;
    }

    /**
     * Converts the given map to a solution
     * @param map a deserialized solution
     * @return the solution as described by the map
     */
    public static List<String> mapToSolution(Map<String, Object> map) {
        List<?> list = (ArrayList<?>) map.get("solution");
        List<String> solutionStrings = new ArrayList<String>();
        for (Object obj : list) {
            solutionStrings.add(obj.toString());
        }
        return solutionStrings;
    }

    /**
     * Converts the given map to a CalculatorGame
     * @param map a deserialized instance of a CalculatorGame
     * @return a CalculatorGame constructed from the given map
     */
    private static CalculatorGame mapToGame(Map<String, Object> map) {
        return CalculatorGame
            .generateGame(
                (int) map.get("value"),
                (int) map.get("goal"),
                (int) map.get("moves"),
                toRulesArray(map.get("rules")),
                toPortalsArray(map.get("portals"))
            );
    }

    /**
     * Converts the given test case to a map for serialization
     * @param game the game part of the test case to serialize
     * @param solution the solution to the game
     * @return a map of the test case
     */
    private static Map<String, Object> testCaseToMap(
        CalculatorGame game,
        List<String> solution
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", game.getValue());
        map.put("goal", game.getGoal());
        map.put("moves", game.getMovesLeft());
        map.put("rules", externalRuleStrings(game.getRules()));
        map.put("portals", game.getPortals());
        map.put("solution", solution);
        return map;
    }

    /**
     * Loads a CalculatorGame level from the given filename
     * @param filename the YAML file containing the level
     * @return a CalculatorGame instance
     */
    public static CalculatorGame loadGame(String filename) {
        Yaml yaml = new Yaml();
        String filePath = Config.TEST_CASES_PATH + "/" + filename;
        String fileContents = fileToString(filePath);
        Map<String, Object> map = yaml.load(fileContents);
        return mapToGame(map);
    }

    /**
     * Loads the solution to a level from the given filename
     * @param filename the YAML file containing a solution
     * @return a solution to a CalculatorGame level
     */
    public static List<String> loadSolution(String filename) {
        Yaml yaml = new Yaml();
        String filePath = Config.TEST_CASES_PATH + "/" + filename;
        String fileContents = fileToString(filePath);
        Map<String, Object> map = yaml.load(fileContents);
        return mapToSolution(map);
    }

    /**
     * Serialize the given test case in the given file
     * @param filePath the name of the file within the test case directory
     * @param game the game to test
     * @param solution the solution to the given game
     * @throws IOException if there are issues with the file
     */
    public static void saveTestCase(
        String filePath,
        CalculatorGame game,
        List<String> solution
    ) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> map = testCaseToMap(game, solution);
        FileWriter writer = new FileWriter(filePath);
        yaml.dump(map, writer);
        writer.close();
    }
}
