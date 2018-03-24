package ru.mit.spbau.model;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This task connects to remote host
 */
public final class RemoteConnectionTask implements WaitTask {

    @NotNull private static final Logger LOGGER = Logger.getLogger(RemoteConnectionTask.class.getName());
    private static final int DEFAULT_TIMEOUT = 5000; // 5 sec for connection

    @NotNull private final RemoteConnection connection;

    @NotNull private final List<Runnable> forNotifySuccess;
    @NotNull private final List<Runnable> forNotifyFail;
    @NotNull private final List<ViewLogger> forLog;

    public RemoteConnectionTask(@NotNull String host, int port) {
        connection = new RemoteConnection(host, port);

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
    @Override
    public Connection getConnection() {
        return connection;
    }

    private void run() {
        log(String.format("Connection to %s:%d", connection.getHost(), connection.getPort()));
        if (!connection.waitConnection(DEFAULT_TIMEOUT)) {
            LOGGER.log(Level.WARNING, "Could not connect");
            connection.close();
            notifyConnectionFail();
            return;
        }
        notifyConnectionSuccess();
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