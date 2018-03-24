package ru.mit.spbau.model;

import com.google.protobuf.Empty;
import ru.mit.spbau.common.MessengerGrpc;
import ru.mit.spbau.common.Proto;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CompletableFuture;

public final class HostConnection extends Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(HostConnection.class.getName());

    @NotNull private final CompletableFuture<StreamObserver<Proto.Message>> responseMessageObserverGetter;
    private StreamObserver<Proto.Message> responseMessageObserver;
    @NotNull private final CompletableFuture<StreamObserver<Empty>> responseNotifyObserverGetter;
    private StreamObserver<Empty> responseNotifyObserver;
    private Server server;

    public HostConnection() throws IOException {
        super();
        responseMessageObserverGetter = new CompletableFuture<>();
        responseNotifyObserverGetter = new CompletableFuture<>();
        server = ServerBuilder.forPort(0).addService(new ConnectionService()).build();
        start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitConnection(int timeout) {
        try {
            responseMessageObserver = responseMessageObserverGetter.get();
            responseNotifyObserver = responseNotifyObserverGetter.get();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not accept connection", e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return "localhost";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return server.getPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean send(@NotNull Proto.Message message) {
        if (responseMessageObserver == null) {
            return false;
        }
        responseMessageObserver.onNext(message);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyTyping() {
        responseNotifyObserver.onNext(Empty.getDefaultInstance());
    }

    private void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                HostConnection.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private class ConnectionService extends MessengerGrpc.MessengerImplBase {
        @Override
        public StreamObserver<Proto.Message> sendMessage(StreamObserver<Proto.Message> responseObserver) {
            LOGGER.log(Level.INFO, "gRPC connection established");
            responseMessageObserverGetter.complete(responseObserver);
            return new StreamObserver<Proto.Message>() {
                @Override
                public void onNext(Proto.Message msg) {
                    LOGGER.log(Level.INFO, "got a message.");
                    messages.add(msg);
                }

                @Override
                public void onError(Throwable t) {
                    LOGGER.log(Level.WARNING, "error occurred", t);
                }

                @Override
                public void onCompleted() {
                    LOGGER.log(Level.INFO, "finished server");
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<Empty> notifyTyping(StreamObserver<Empty> responseObserver) {
            LOGGER.log(Level.INFO, "gRPC notifications established");
            responseNotifyObserverGetter.complete(responseObserver);
            return new StreamObserver<Empty>() {
                @Override
                public void onNext(Empty empty) {
                    LOGGER.log(Level.INFO, "got notification.");
                    final Date date = new Date();
                    lastTimeNotified = date.getTime();
                }

                @Override
                public void onError(Throwable t) {
                    LOGGER.log(Level.WARNING, "error occurred", t);
                }

                @Override
                public void onCompleted() {
                    LOGGER.log(Level.INFO, "finished server");
                    responseObserver.onCompleted();
                }
            };
        }
    }


}
