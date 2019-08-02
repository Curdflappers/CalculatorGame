package com.mathwithmark.calculatorgamesolver.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * Handles serialization and deserialization of test cases
 */
public class Serialize {
    /**
     * Converts the contents of the given file at the given path to a string
     * @param absolutePath the path to the file to convert
     * @return the contents of the file at the given path
     */
    public static String fileContents(String absolutePath) {
        File file = new File(absolutePath);
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
        map.put("game", game.toMap());
        map.put("solution", solution);
        return map;
    }

    /**
     * Loads a given map from the file within the test-cases directory
     * @param absolutePath the absolute path of the YAML file
     * @return a map of the loaded YAML
     */
    private static Map<String, Object> loadMapFromFile(String absolutePath) {
        Yaml yaml = new Yaml();
        String fileContents = fileContents(absolutePath);
        Map<String, Object> map = yaml.load(fileContents);
        return map;
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
    )
        throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> map = testCaseToMap(game, solution);
        FileWriter writer = new FileWriter(filePath);
        yaml.dump(map, writer);
        writer.close();
    }

    public static TestCase loadTestCase(String testCasePath) {
        Map<String, Object> map = loadMapFromFile(testCasePath);
        return TestCase.from(map);
    }
}
