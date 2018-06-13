package main;

import java.util.Scanner;

/**
 * Runs the Calculator Game solver
 * 
 * @author mwwie
 *
 */
public class Main {
    private static int state = 0, goal = 0, moves = 0;
    private static Rule[] rules = new Rule[0];
    private static Game game;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            parseInput(scanner);
            scanner.close();
        } else {
            parseInput(args);
        }
        
        game = new Game(state, goal, moves, rules);
    }
    
    public static Game getGame() {
        return game;
    }
    
    public static int getState() { return state; }
    public static int getGoal() { return goal; }
    public static int getMoves() { return moves; }
    public static Rule[] getRules() { return rules; }
    
    /**
     * Parse input from the given scanner
     * @param scanner
     */
    public static void parseInput(Scanner scanner) {
        System.out.print("Enter start state: ");
        state = scanner.nextInt();

        System.out.print("Enter goal state: ");
        goal = scanner.nextInt();

        System.out.print("Enter number of moves: ");
        moves = scanner.nextInt();
        
        System.out.println("Enter comma-separated rules: ");
        parseRules(scanner.nextLine().split(","));
        
        scanner.close();
    }
    
    /**
     * Parse input from the given array
     * @param args
     */
    public static void parseInput(String[] args) {
        try {
            state = Integer.parseInt(args[0]);
            goal = Integer.parseInt(args[1]);
            moves = Integer.parseInt(args[2]);
            parseRules(args[3].split(","));
        } catch (NumberFormatException e) {
            // TODO handle bad input (lots of it)
        }        
    }
    
    public static void parseRules(String[] ruleStrings) {
        rules = new Rule[ruleStrings.length];
        for (int i = 0; i < rules.length; i++) {
            rules[i] = new Rule(ruleStrings[i]);
        }
    }
}
