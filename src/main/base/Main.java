package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import game.State;
import rules.Rule;

/**
 * Runs the Calculator CalculatorGame solver
 *
 */
public class Main {
    private static int value = 0, goal = 0, moves = 0;
    private static Rule[] rules = new Rule[0];
    private static boolean portalsPresent = false;
    private static int[] portals = null;
    private static CalculatorGame calculatorGame;
    private static boolean again;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            getInput(args, scanner);
            System.out.println(Config.SOLUTION_PROMPT);
            List<State> solutionStates = CGSolver.solve(calculatorGame);
            String solutionString = State.allTransitions(solutionStates);
            System.out.print(solutionString);
            promptAgain(scanner);
            args = new String[0]; // can't use the same args again
        } while (again);
        scanner.close();
    }

    public static void getInput(String[] args, Scanner scanner) {
        if (args.length == 0) {
            parseInput(scanner);
        } else {
            parseInput(args);
        }

        calculatorGame = new CalculatorGame(value, goal, moves, rules, portals);
    }

    private static void promptAgain(Scanner scanner) {
        System.out.print(Config.AGAIN_PROMPT);
        String answer = scanner.nextLine();
        again = answer.charAt(0) == 'y';
    }

    public static CalculatorGame getCalculatorGame() {
        return calculatorGame;
    }

    public static int getValue() {
        return value;
    }

    public static int getGoal() {
        return goal;
    }

    public static int getMoves() {
        return moves;
    }

    public static Rule[] getRules() {
        return rules;
    }

    /**
     * Parse input from the given scanner
     *
     * @param scanner
     */
    public static void parseInput(Scanner scanner) {
        System.out.print(Config.START_PROMPT);
        value = scanner.nextInt();

        System.out.print(Config.GOAL_PROMPT);
        goal = scanner.nextInt();

        System.out.print(Config.MOVES_PROMPT);
        moves = scanner.nextInt();
        scanner.nextLine();
        System.out.print(Config.RULES_PROMPT);
        ArrayList<String> ruleStrings = new ArrayList<>();
        do {
            String input = scanner.nextLine();
            if (input.length() > 0) {
                ruleStrings.add(input);
            } else {
                break;
            }
        } while (true);
        parseRules(ruleStrings);

        System.out.print(Config.PORTALS_PRESENT_PROMPT);
        portalsPresent = scanner.nextLine().charAt(0) == 'y';
        if (portalsPresent) {
            portals = new int[2];
            System.out.print(Config.LEFT_PORTAL_PROMPT);
            portals[0] = scanner.nextInt();
            scanner.nextLine();
            System.out.print(Config.RIGHT_PORTAL_PROMPT);
            portals[1] = scanner.nextInt();
            scanner.nextLine();
        } else {
            portals = null;
        }
    }

    /**
     * Parse input from the given array
     *
     * @param args
     */
    public static void parseInput(String[] args) {
        try {
            value = Integer.parseInt(args[0]);
            goal = Integer.parseInt(args[1]);
            moves = Integer.parseInt(args[2]);
            parseRules(Arrays.asList(args[3].split(Config.CMDLINE_SEPARATOR)));
            if (args[4].equals("y")) {
                portals = new int[2];
                portals[0] = Integer.parseInt(args[5]);
                portals[1] = Integer.parseInt(args[6]);
            } else {
                portals = null;
            }
        } catch (NumberFormatException e) {
            // TODO handle bad input (lots of it)
        }
    }

    public static void parseRules(List<String> ruleStrings) {
        rules = new Rule[ruleStrings.size()];
        for (int i = 0; i < rules.length; i++) {
            rules[i] = Rule.ruleFromString(ruleStrings.get(i));
        }
    }
}
