package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RuleTests {
    @Test
    void shiftRule() {
        assertEquals(1, ShiftRule.digits(0));
    }
}
