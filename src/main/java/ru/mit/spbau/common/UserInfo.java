package ru.mit.spbau.common;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

/**
 * All info about user (which is user's name and where he connected to).
 *
 * As we have only 1 user at a time we should make this class singleton
 *
 * TODO singletons in java should be implemented as enums
 */
public final class UserInfo {

    private static UserInfo INSTANCE;

    private String userName;
    private String host;
    private int port;

    private UserInfo() {

    }

    public static UserInfo getUserInfo() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfo();
        }
        return INSTANCE;
    }

    public void setUserName(@NotNull String userName) {
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setHost(@NotNull String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

}
