package com.revilodev.avarice.devtools.items;

import java.util.List;
import java.util.Map;

/** Server-owned mutable definition; stacks retain only the stable definition id. */
public record ItemDefinition(String id, String name, String description, String color, List<String> tooltip,
                             Map<String, Double> stats, Map<String, Double> percentages, Map<String, Double> multipliers,
                             String nameTranslation, String rarity, Map<String, String> metadata) { }
