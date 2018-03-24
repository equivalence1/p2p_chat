package ru.mit.spbau.model;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.common.MessengerGrpc;
import ru.mit.spbau.common.Proto;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class defines connection behavior
 * when we connect to remote host.
 */
public final class RemoteConnection extends Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(RemoteConnection.class.getName());

    @NotNull private final String host;
    private int port;
    private ManagedChannel channel;
    private StreamObserver<Proto.Message> requestMessageObserver;
    private StreamObserver<Empty> requestNotifyObserver;
    @NotNull private CountDownLatch finishLatch;

    public RemoteConnection(@NotNull String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitConnection(int timeout) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        MessengerGrpc.MessengerStub asyncStub = MessengerGrpc.newStub(channel);

        finishLatch = new CountDownLatch(1);
        requestMessageObserver = asyncStub.sendMessage(new StreamObserver<Proto.Message>() {
            @Override
            public void onNext(Proto.Message msg) {
                LOGGER.log(Level.INFO, "got a message.");
                messages.add(msg);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                LOGGER.log(Level.WARNING, "fail: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                LOGGER.log(Level.INFO, "finished client");
                finishLatch.countDown();
            }
        });

        requestNotifyObserver = asyncStub.notifyTyping(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {
                LOGGER.log(Level.INFO, "got notification.");
                final Date date = new Date();
                lastTimeNotified = date.getTime();
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                LOGGER.log(Level.WARNING, "fail: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                LOGGER.log(Level.INFO, "finished client");
                finishLatch.countDown();
            }
        });

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean send(@NotNull Proto.Message message) {
        if (requestMessageObserver == null) {
            return false;
        }
        requestMessageObserver.onNext(message);
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyTyping() {
        requestNotifyObserver.onNext(Empty.getDefaultInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        if (requestMessageObserver != null) {
            try {
                requestMessageObserver.onCompleted();
                finishLatch.await(1, TimeUnit.MINUTES);
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                // just ignore
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return host.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return port;
    }

}
