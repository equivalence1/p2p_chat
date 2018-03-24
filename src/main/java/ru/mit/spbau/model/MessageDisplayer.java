package ru.mit.spbau.model;

import ru.mit.spbau.common.Proto;
import org.jetbrains.annotations.NotNull;

/**
 * interface for registering displayers in chat task
 */
@FunctionalInterface
public interface MessageDisplayer {

    /**
     * method to display message
     * @param message message to display
     */
    void displayMessage(@NotNull Proto.Message message);

}
