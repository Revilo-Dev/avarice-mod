package com.avarice.devtools.values;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.revilodev.avarice.devtools.values.Percentage;
import org.junit.jupiter.api.Test;

class PercentageTest { @Test void distinguishesPointAndRelativeChanges() { Percentage value = Percentage.points(20); assertEquals(25, value.addPoints(5).asPercent(), 0.000001); assertEquals(21, value.increaseRelative(5).asPercent(), 0.000001); } }
