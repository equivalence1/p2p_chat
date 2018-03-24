package ru.mit.spbau.model;

import com.google.protobuf.Timestamp;
import ru.mit.spbau.common.Proto;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * This task receives and sends messages
 */
public final class ChatTask implements Runnable {

    @NotNull private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    @NotNull private final String userName;
    @NotNull private final Queue<String> toSend;
    @NotNull private final Connection connection;

    private MessageDisplayer localDisplayer;
    private MessageDisplayer remoteDisplayer;

    private boolean shouldStop;
    private boolean shouldNotify = false;

    public ChatTask(@NotNull String userName, @NotNull Connection connection) {
        this.userName = userName;
        this.connection = connection;
        toSend = new LinkedList<>();
        shouldStop = false;
    }

    /**
     * start receiving/sending messages
     */
    public void start() {
        Thread execThread = new Thread(this);
        execThread.start();
    }

    /**
     * stop receiving/sending messages
     */
    public void stop() {
        shouldStop = true;
        connection.close();
    }

    @NotNull
    public Connection getConnection() {
        return connection;
    }

    /**
     * registering method for displaying messages which we sent
     * @param localDisplayer method to display message
     */
    public void registerLocalDisplayer(@NotNull MessageDisplayer localDisplayer) {
        this.localDisplayer = localDisplayer;
    }

    /**
     * registering method for displaying messages which we received
     * @param remoteDisplayer method to display message
     */
    public void registerRemoteDisplayer(@NotNull MessageDisplayer remoteDisplayer) {
        this.remoteDisplayer = remoteDisplayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        handleMessages();
    }

    /**
     * Add message to send queue
     *
     * @param content content of message
     */
    public void addToSend(String content) {
        toSend.add(content);
    }

    /**
     * notify connection that we are typing right now
     */
    public void notifyTyping() {
        shouldNotify = true;
    }

    private void handleMessages() {
        System.out.println("started chat task");
        while (!shouldStop) {
            sendAllNotifications();
            sendAll();
            receiveAll();
            /*
            In perfect case, here should be `select` on input channel and conditional
            variable with `signal` on `toSend` queue, but it's
            a bit more complicated and i dont want to do it, so just sleep
            for a little while -- this wont load CPU so much and user wont
            notice the delay.
             */
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // just ignore
            }
        }
    }

    private void sendAll() {
        while (!toSend.isEmpty()) {
            sendOne(toSend.poll());
        }
    }

    private void sendOne(String content) {
        final Proto.Message.Builder messageBuilder = Proto.Message.newBuilder();
        final Timestamp.Builder timeBuilder = Timestamp.newBuilder();

        final Date date = new Date();
        timeBuilder.setSeconds(date.getTime() / 1000);

        messageBuilder.setName(userName);
        messageBuilder.setContent(content);
        messageBuilder.setTime(timeBuilder);

        final Proto.Message message = messageBuilder.build();

        if (connection.send(message)) {
            localDisplayer.displayMessage(message); // dont display message if we could not send it
        }
    }

    private void sendAllNotifications() {
        if (shouldNotify) {
            shouldNotify = false;
            connection.notifyTyping();
        }
    }

    private void receiveAll() {
        while (connection.hasPendingMessages()) {
            receiveOne();
        }
    }

    private void receiveOne() {
        Proto.Message message = connection.receive();
        remoteDisplayer.displayMessage(message);
    }

}
