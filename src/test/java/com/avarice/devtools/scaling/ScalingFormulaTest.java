package com.avarice.devtools.scaling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.revilodev.avarice.devtools.scaling.ScalingFormula;
import org.junit.jupiter.api.Test;

class ScalingFormulaTest {
    @Test void evaluatesAndClampsFormula() { assertEquals(40, new ScalingFormula(10, 5, 2, 0, 40).evaluate(10)); }
}
