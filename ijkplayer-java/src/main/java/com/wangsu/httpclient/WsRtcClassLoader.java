package com.wangsu.httpclient;

public class WsRtcClassLoader {
    public static Object getClassLoader() {
        ClassLoader classLoader = WsRtcClassLoader.class.getClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        throw new RuntimeException("Failed to get WebRTC class loader.");
    }
}
