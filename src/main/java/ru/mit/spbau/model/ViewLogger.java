package ru.mit.spbau.model;

/**
 * Interface for ru.mit.spbau.view loggers
 * Used primarily in WaitTask
 */
@FunctionalInterface
public interface ViewLogger {

    /**
     * log message to user on screen
     *
     * @param msg message content
     */
    void log(String msg);

}
