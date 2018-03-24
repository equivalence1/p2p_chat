package ru.mit.spbau.model;

/**
 * Interface of task for wait screen
 */
public interface WaitTask {

    /**
     * Start executing this task in new thread
     */
    void start();

    /**
     * Stop executing this task
     */
    void stop();

    /**
     * get connection with which this task is working now
     * TODO may be its better to rename this interface because of existence of this method
     *
     * @return current connection
     */
    Connection getConnection();

    /**
     * Task can finish with success or fail
     * If you want to be notified about exit status,
     * register your method here.
     *
     * @param r method to call on success
     */
    void registerNotifySuccess(Runnable r);

    /**
     * Same as {@link WaitTask#registerNotifySuccess(Runnable)}
     * but for fail exit status
     *
     * @param r method to call on fail
     */
    void registerNotifyFail(Runnable r);

    /**
     * If you want to receive some more verbose
     * logging, register your method here
     */
    void registerLog(ViewLogger l);

}
