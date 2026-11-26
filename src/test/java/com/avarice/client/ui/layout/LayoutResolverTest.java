package com.avarice.client.ui.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.revilodev.avarice.client.ui.layout.LayoutResolver;
import com.revilodev.avarice.core.config.UiConfig;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LayoutResolverTest {
    @Test void centersWithinViewport() { UiConfig.ElementDefinition element = new UiConfig.ElementDefinition("panel", "", UiConfig.Anchor.CENTER, 0, 0, new UiConfig.Dimension(200, UiConfig.Dimension.Unit.PIXELS, 0, 999), new UiConfig.Dimension(100, UiConfig.Dimension.Unit.PIXELS, 0, 999), 1, .5, .5, true, ""); UiConfig.ScreenDefinition screen = new UiConfig.ScreenDefinition(0, 0, Map.of("panel", element)); LayoutResolver.Bounds result = new LayoutResolver().resolve(screen, new LayoutResolver.Bounds(0, 0, 800, 600)).get("panel"); assertEquals(300, result.x()); assertEquals(250, result.y()); }
    @Test void keepsTopRightChildAttachedWhenParentResizes() { UiConfig.ElementDefinition parent = new UiConfig.ElementDefinition("parent", "", UiConfig.Anchor.TOP_LEFT, 0, 0, new UiConfig.Dimension(50, UiConfig.Dimension.Unit.PERCENT, 0, 999), new UiConfig.Dimension(100, UiConfig.Dimension.Unit.PIXELS, 0, 999), 1, 0, 0, true, ""); UiConfig.ElementDefinition child = new UiConfig.ElementDefinition("child", "parent", UiConfig.Anchor.TOP_RIGHT, 0, 0, new UiConfig.Dimension(20, UiConfig.Dimension.Unit.PIXELS, 0, 999), new UiConfig.Dimension(20, UiConfig.Dimension.Unit.PIXELS, 0, 999), 1, 1, 0, true, ""); UiConfig.ScreenDefinition screen = new UiConfig.ScreenDefinition(0, 0, Map.of("parent", parent, "child", child)); LayoutResolver.Bounds result = new LayoutResolver().resolve(screen, new LayoutResolver.Bounds(0, 0, 800, 600)).get("child"); assertEquals(380, result.x()); }
}
