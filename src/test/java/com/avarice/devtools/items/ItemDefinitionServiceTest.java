package com.avarice.devtools.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.revilodev.avarice.devtools.items.ItemDefs;
import org.junit.jupiter.api.Test;

class ItemDefsTest {
    @Test void parsesOptionalDynamicDisplayFields() {
        var definitions = ItemDefs.parse("[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{},\"nameTranslation\":\"item.avarice.test\",\"rarity\":\"development\",\"metadata\":{\"source\":\"test\"}}]");
        assertEquals("item.avarice.test", definitions.get("test").nameTranslation());
        assertEquals("development", definitions.get("test").rarity());
    }

    @Test void rejectsDuplicateDefinitions() {
        String value = "[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}},{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}}]";
        assertThrows(IllegalArgumentException.class, () -> ItemDefs.parse(value));
    }

    @Test void parsesVersionedDefinitions() {
        String value = "{\"schema_version\":1,\"definitions\":[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}}]}";
        assertEquals("Test", ItemDefs.parse(value).get("test").name());
    }
}
