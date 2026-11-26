package com.revilodev.avarice.core.reload;

/** Extension point for atomically reloadable definitions. */
public interface ReloadableDomain<T> {
    String id();
    T load() throws Exception;
    void validate(T candidate) throws Exception;
    void apply(T candidate);
}
