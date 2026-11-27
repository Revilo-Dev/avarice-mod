package com.avarice.core.reload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.revilodev.avarice.core.reload.ReloadManager;
import com.revilodev.avarice.core.reload.ReloadableDomain;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ReloadManagerTest {
    @Test void retainsLastAppliedValueWhenValidationFails() {
        AtomicInteger next = new AtomicInteger(2);
        AtomicInteger active = new AtomicInteger(1);
        ReloadManager manager = new ReloadManager();
        manager.register(new ReloadableDomain<Integer>() {
            @Override public String id() { return "test"; }
            @Override public Integer load() { return next.get(); }
            @Override public void validate(Integer candidate) { if (candidate < 0) throw new IllegalArgumentException("negative"); }
            @Override public void apply(Integer candidate) { active.set(candidate); }
        });
        assertEquals(true, manager.reload("test").success());
        assertEquals(2, active.get());
        next.set(-1);
        assertEquals(false, manager.reload("test").success());
        assertEquals(2, active.get());
    }
}
