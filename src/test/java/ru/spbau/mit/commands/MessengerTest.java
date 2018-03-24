package ru.spbau.mit.commands;

import ru.mit.spbau.common.Proto;
import ru.mit.spbau.model.ChatTask;
import ru.mit.spbau.model.HostConnectionTask;
import ru.mit.spbau.model.RemoteConnectionTask;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

/**
 * I didn't care about UI much, so obviously I'm not
 * gonna test it. So just one function test here.
 * (except for connections and ui there is nothing to test)
 */

public class MessengerTest {

    @NotNull private static final String USER1_NAME = "user1";
    @NotNull private static final String USER2_NAME = "user1";

    @NotNull private static final String USER1_MSG_CONTENT = "something";
    @NotNull private static final String USER2_MSG_CONTENT = "something else";

    private HostConnectionTask hostTask;
    private RemoteConnectionTask remoteTask;

    private boolean isNotifiedHost = false;
    private boolean isNotifiedRemote = false;

    private boolean isUser1LocalSent = false;
    private boolean isUser1RemoteReceived = false;
    private boolean isUser2LocalSent = false;
    private boolean isUser2RemoteReceived = false;

    @Test
    public void testConnectionsEstablish() throws Exception {
        createConnectionTasks();

        Thread.sleep(100);

        assertEquals(true, isNotifiedHost);
        assertEquals(true, isNotifiedRemote);

        hostTask.stop();
        remoteTask.stop();
    }

    @Test
    public void testMessageSend() throws Exception {
        /*
        It's not really cool to call test inside a test, but i think
        it's fine in this case -- i just need to establish connection
         */
        createConnectionTasks();

        Thread.sleep(300);

        final ChatTask hostChatTask = new ChatTask(USER1_NAME, hostTask.getConnection());
        final ChatTask remoteChatTask = new ChatTask(USER2_NAME, remoteTask.getConnection());

        hostChatTask.registerLocalDisplayer(this::displayUser1LocalMessage);
        hostChatTask.registerRemoteDisplayer(this::displayUser1RemoteMessage);

        remoteChatTask.registerLocalDisplayer(this::displayUser2LocalMessage);
        remoteChatTask.registerRemoteDisplayer(this::displayUser2RemoteMessage);

        hostChatTask.start();
        remoteChatTask.start();

        Thread.sleep(300);

        hostChatTask.addToSend(USER1_MSG_CONTENT);
        remoteChatTask.addToSend(USER2_MSG_CONTENT);

        Thread.sleep(300);

        assertEquals(true, isUser1LocalSent);
        assertEquals(true, isUser1RemoteReceived);
        assertEquals(true, isUser2LocalSent);
        assertEquals(true, isUser2RemoteReceived);

        remoteTask.stop();
        hostTask.stop();
        hostChatTask.stop();
        remoteChatTask.stop();
    }

    private void createConnectionTasks() throws Exception {
        hostTask = new HostConnectionTask();
        hostTask.registerNotifySuccess(this::notifySuccessHost);

        hostTask.start();
        Thread.sleep(1000);

        final String host = "127.0.0.1";
        final int port = hostTask.getConnection().getPort();
        remoteTask = new RemoteConnectionTask(host, port);
        remoteTask.registerNotifySuccess(this::notifySuccessRemote);

        remoteTask.start();
    }

    private void notifySuccessHost() {
        isNotifiedHost = true;
    }

    private void notifySuccessRemote() {
        isNotifiedRemote = true;
    }

    private void displayUser1LocalMessage(@NotNull Proto.Message message) {
        isUser1LocalSent = true;
        assertEquals(USER1_NAME, message.getName());
        assertEquals(USER1_MSG_CONTENT, message.getContent());
    }

    private void displayUser1RemoteMessage(@NotNull Proto.Message message) {
        isUser2RemoteReceived = true;
        assertEquals(USER2_NAME, message.getName());
        assertEquals(USER2_MSG_CONTENT, message.getContent());
    }

    private void displayUser2LocalMessage(@NotNull Proto.Message message) {
        isUser2LocalSent = true;
        assertEquals(USER2_NAME, message.getName());
        assertEquals(USER2_MSG_CONTENT, message.getContent());
    }

    private void displayUser2RemoteMessage(@NotNull Proto.Message message) {
        isUser1RemoteReceived = true;
        assertEquals(USER1_NAME, message.getName());
        assertEquals(USER1_MSG_CONTENT, message.getContent());
    }

}