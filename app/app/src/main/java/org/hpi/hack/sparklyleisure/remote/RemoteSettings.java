package org.hpi.hack.sparklyleisure.remote;

public class RemoteSettings {
    public static String SERVER_IP = "123";
    public static String SERVER_PORT = "80";

    public static String getBaseUrl() {
        return String.format("http://%s:%s/", SERVER_IP, SERVER_PORT);
    }

    public static String getRequestUrl(String function) {
        return getBaseUrl() + "/" + function;
    }
}
