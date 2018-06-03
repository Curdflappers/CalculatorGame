package test;

import main.*;

public class Test {
    public static void main(String[] args) {
        testParse();
        System.out.println("Success, all tests passed");
    }

    public static void testParse() {
        Rule rule = new Rule("add 1");
        try {
            assert rule.getOperator().equals(Rule.class.getDeclaredMethod("add",
                new Class[] {Integer.class}));
        } catch (NoSuchMethodException e) {
            System.out
                .println("Unexpected NoSuchMethodException in Test.testParse");
            e.printStackTrace();
        }
        assert rule.getOperand() == 1;
        System.out.println("testParse passed");
    }
}
