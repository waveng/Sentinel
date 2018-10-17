package com.taobao.csp.sentinel.dashboard.client.zk;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public interface Writable<T> extends AutoCloseable{
    public void write(String app, String ip, int port, T conf) throws Exception;
}
