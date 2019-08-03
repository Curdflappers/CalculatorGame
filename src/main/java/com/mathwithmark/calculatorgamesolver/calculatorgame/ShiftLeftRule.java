package com.mathwithmark.calculatorgamesolver.calculatorgame;

class ShiftLeftRule extends ShiftRule {
    protected void rotate(int[] digits) {
        rotateLeft(digits);
    }

    ShiftLeftRule() {
        super(true); // is left
    }
}
