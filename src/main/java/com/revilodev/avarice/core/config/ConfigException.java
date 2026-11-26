package com.revilodev.avarice.core.config;

/** Describes an invalid development configuration with its JSON location. */
public final class ConfigException extends Exception {
    public ConfigException(String message) { super(message); }
    public ConfigException(String message, Throwable cause) { super(message, cause); }
}
