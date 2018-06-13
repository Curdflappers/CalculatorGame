package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Runs the Calculator Game solver
 * 
 * @author mwwie
 *
 */
public class Main {
    private static int value = 0, goal = 0, moves = 0;
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
        
        game = new Game(value, goal, moves, rules);
        solveGame();
    }
    
    /**
     * Run a DFS and print out the solution to the game
     */
    private static void solveGame() {
        Stack<State> stack = new Stack<>();
        stack.push(game.getState());
        
        while (true) {
            if (stack.isEmpty()) {
                System.out.println("Game unsolvable");
                return;
            }
            
            for (State successor : successors(stack.pop())) {
                if (successor.getValue() == successor.getGoal()) {
                    printSolution(successor);
                    return;
                }
                
                stack.push(successor);
            }
        }
    }
    
    private static List<State> successors(State state) {
        List<State> successors = new ArrayList<>();
        for(Rule rule : game.getRules()) {
            successors.add(new State(state, rule));
        }
        return successors;
    }
    
    private static void printSolution(State state) {
        List<State> states = orderedStates(state);
        for(State element : states) {
            System.out.println(element.getRule());
        }
    }
    
    /**
     * Returns a Stack of states, with the newest at the front, the oldest at the end.
     * @param state
     * @return
     */
    private static List<State> orderedStates(State state) {
        List<State> states = new LinkedList<>();
        while(state != null) {
            states.add(0, state); // add more recent to beginning of list
            state = state.getParent();
        }
        return states;
    }
    
    public static Game getGame() {
        return game;
    }
    
    public static int getValue() { return value; }
    public static int getGoal() { return goal; }
    public static int getMoves() { return moves; }
    public static Rule[] getRules() { return rules; }
    
    /**
     * Parse input from the given scanner
     * @param scanner
     */
    public static void parseInput(Scanner scanner) {
        System.out.print("Enter start state: ");
        value = scanner.nextInt();

        System.out.print("Enter goal state: ");
        goal = scanner.nextInt();

        System.out.print("Enter number of moves: ");
        moves = scanner.nextInt();
        scanner.nextLine();
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
            value = Integer.parseInt(args[0]);
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