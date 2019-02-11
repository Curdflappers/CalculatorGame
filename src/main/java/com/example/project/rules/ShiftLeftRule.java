package com.example.project.rules;

public class ShiftLeftRule extends ShiftRule {
    protected void rotate(int[] digits) {
        rotateLeft(digits);
    }

    public ShiftLeftRule() {
        super(true); // is left
    }
}
