package com.mathwithmark.calculatorgamesolver.calculatorgame;

class ShiftRightRule extends ShiftRule {
    protected void rotate(int[] digits) {
        rotateRight(digits);
    }

    ShiftRightRule() {
        super(false); // not left
    }
}
