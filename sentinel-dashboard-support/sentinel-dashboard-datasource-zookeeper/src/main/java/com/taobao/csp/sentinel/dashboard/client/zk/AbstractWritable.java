package com.taobao.csp.sentinel.dashboard.client.zk;

import com.alibaba.csp.sentinel.datasource.Converter;
/**
 * 
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public abstract class AbstractWritable<T, S> implements Writable<T>{
    
    protected final Converter<T, S> parser;
    
    public AbstractWritable(Converter<T, S> parser) {
        super();
        this.parser = parser;
    }

    @Override
    public void write(String app, String ip, int port, T conf) throws Exception {
        write(getPath(app, ip, port), converter(conf));
    }


    private S converter(T conf) throws Exception {
        S value = parser.convert(conf);
        return value;
    }
    
    public abstract void write(String path, S conf) throws Exception;
    
    public abstract String getPath(String app, String ip, int port)  throws Exception;
    
}
