package ru.mit.spbau.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tasks waits remote connection
 */
public final class HostConnectionTask implements WaitTask {

    @NotNull private static final Logger LOGGER = Logger.getLogger(HostConnectionTask.class.getName());

    private HostConnection connection;

    @NotNull private final List<Runnable> forNotifySuccess;
    @NotNull private final List<Runnable> forNotifyFail;
    @NotNull private final List<ViewLogger> forLog;

    public HostConnectionTask() {
        forNotifySuccess = new LinkedList<>();
        forNotifyFail = new LinkedList<>();
        forLog = new LinkedList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        Thread thread = new Thread(this::run);
        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        connection.close(); // this will also stop the thread
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerNotifySuccess(Runnable r) {
        forNotifySuccess.add(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerNotifyFail(Runnable r) {
        forNotifyFail.add(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerLog(ViewLogger l) {
        forLog.add(l);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Connection getConnection() {
        return connection;
    }

    private void run() {
        try {
            connection = new HostConnection();
            log(String.format("Listening for connection on port %d", connection.getPort()));
            if (!connection.waitConnection(0)) {
                LOGGER.log(Level.WARNING, "Could not connect");
                connection.close();
                notifyConnectionFail();
                return;
            }
            notifyConnectionSuccess();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not start server", e);
            connection.close();
            notifyConnectionFail();
        }
    }

    private void notifyConnectionFail() {
        forNotifyFail.forEach(Runnable::run);
    }

    private void notifyConnectionSuccess() {
        forNotifySuccess.forEach(Runnable::run);
    }

    private void log(@NotNull String msg) {
        forLog.forEach((l) -> l.log(msg));
    }

}
