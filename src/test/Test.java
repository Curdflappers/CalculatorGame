package test;

import main.*;

public class Test {
    public static void main(String[] args) {
        testParse();
        System.out.println("Success, all tests passed");
    }

    private static void testParse() {
        Rule rule = new Rule("add 1");
        testOperator(rule, "add");
        assert rule.getOperand() == 1;

        rule = new Rule("+ 1");
        testOperator(rule, "add");

        System.out.println("testParse passed");
    }

    private static void testOperator(Rule rule, String operatorName) {
        try {
            assert rule.getOperator().equals(Rule.class
                .getDeclaredMethod(operatorName, new Class[] {Integer.class}));
        } catch (NoSuchMethodException e) {
            System.out
                .println("Unexpected NoSuchMethodException in Test.testParse");
            e.printStackTrace();
        }
    }
}
