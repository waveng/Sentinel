package com.taobao.csp.sentinel.dashboard.client.zk;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public interface Readable <S, T>  extends AutoCloseable{
    T read(String app, String ip, int port) throws Exception;
    
}
