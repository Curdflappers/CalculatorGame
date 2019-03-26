package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import rules.Rule;

/**
 * Runs the Calculator Game solver
 *
 * @author mwwie
 *
 */
public class Main {
    private static int value = 0, goal = 0, moves = 0;
    private static Rule[] rules = new Rule[0];
    private static boolean portalsPresent = false;
    private static int[] portals = null;
    private static Game game;
    private static boolean again;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            getInput(args, scanner);
            System.out.println(Config.SOLUTION_PROMPT);
            State endState = solveGame(game);
            String solution = extractSolution(endState);
            System.out.print(solution);
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

        game = new Game(value, goal, moves, rules, portals);
    }

    private static void promptAgain(Scanner scanner) {
        System.out.print(Config.AGAIN_PROMPT);
        String answer = scanner.nextLine();
        again = answer.charAt(0) == 'y';
    }

    /**
     * Run a DFS and return the State that contains the end game.
     */
    static State solveGame(Game game) {
        Stack<State> stack = new Stack<>();
        stack.push(game.getState());

        while (true) {
            if (stack.isEmpty()) {
                return null;
            }
            for (State successor : successors(stack.pop())) {
                if (successor.getValue() == successor.getGoal()) {
                    return successor;
                }
                stack.push(successor);
            }
        }
    }

    /**
     * Valid successors have no decimals, have different games than their
     * predecessors, and have values of 6 digits or less
     * @param state
     * @return
     */
    private static List<State> successors(State state) {
        List<State> successors = new ArrayList<>();
        addSuccessors(state, successors, true);
        addSuccessors(state, successors, false);
        return successors;
    }

    private static void addSuccessors(
        State state,
        List<State> successors,
        boolean apply
    ) {
        if (state.getMovesLeft() > 0) {
            for (Rule rule : state.getRules()) {
                State successor = new State(state, rule, apply);
                if (isValidSuccessor(successor, state)) {
                    successors.add(successor);
                }
            }
        }
    }

    private static boolean isValidSuccessor(State successor, State parent) {
        boolean valid = true;

        valid &= successor.getValue() % 1 == 0; // no decimals
        // max 6 digits
        valid &= Math.abs(successor.getValue()) < Math.pow(10, 6);

        // not redundant
        while (parent != null) {
            valid &= !successor.getGame().equalsExceptMoves(parent.getGame());
            parent = parent.getParent();
        }

        return valid;
    }

    /**
     * Returns a string that represents the solution to the game. Each step is 
     * separated by a newline character.
     */
    private static String extractSolution(State state) {
        String solution = "";

        List<State> states = orderedStates(state);
        cleanUp(states);
        for (State element : states) {
            if (element.getRule() != null) {
                solution +=
                    element.getApplied()
                        ? Config.APPLY_PROMPT
                        : Config.UPDATE_PROMPT;
                solution += element.getRule() + "\n";
            }
        }

        return solution;
    }

    /**
     * Removes extra "Update Store"s that clutter the solution
     * @param states The ordered list of states to clean up
     */
    public static void cleanUp(List<State> states) {
        boolean foundOne = false;
        int foundIndex = -1;

        // skip first state, it has a null rule
        for (int i = 1; i < states.size(); i++) {
            State state = states.get(i);
            if (state.getRule().getOperator() == Config.STORE) {
                if (state.getApplied()) {
                    foundOne = false;
                } else if (foundOne) { // already found one, this is second
                    states.remove(foundIndex);
                    i--;
                    foundIndex = i;
                } else { // haven't found one yet, don't remove this one yet
                    foundOne = true;
                    foundIndex = i;
                }
            }
        }

        // Remove potential trailing "Update Store"s
        if (foundOne) states.remove(foundIndex);
    }

    /**
     * Returns a list of states, with the earliest at the front, the latest at
     * the end.
     *
     * @param state
     * @return
     */
    private static List<State> orderedStates(State state) {
        List<State> states = new LinkedList<>();
        while (state != null) {
            states.add(0, state); // add more recent to beginning of list
            state = state.getParent();
        }
        return states;
    }

    public static Game getGame() {
        return game;
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
