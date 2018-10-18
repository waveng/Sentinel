package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.taobao.csp.sentinel.dashboard.client.zk.AbstractWritable;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.client.zk.util.Util;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class WritableDataSource<T> extends AbstractWritable<T, byte[]> {
    
    private final String nodeType;
    private ZkClient zkClient;
    
    public WritableDataSource(final ZkClient zkClient, final NodeType nodeType, Converter<T, byte[]> parser) {
        super(parser);
        this.nodeType = nodeType.toString();
        this.zkClient = zkClient;
    }
    
    
    @Override
    public void write(String path, byte[] rule) throws Exception {
        if (this.zkClient == null) {
            throw new IllegalStateException("Zookeeper has not been initialized or error occurred");
        }
        zkClient.forPath(path, rule);
    }

    @Override
    public void close() throws Exception {
        if (this.zkClient != null) {
            this.zkClient.close();
        }
    }

    @Override
    public String getPath(String app, String ip, int port) throws Exception {
        if (this.zkClient == null) {
            throw new IllegalStateException("Zookeeper has not been initialized or error occurred");
        }
        byte[] data = zkClient.forPath(Util.getTypePath(app, ip, String.valueOf(port), this.nodeType));
        String path = new String(data);
        if (zkClient.checkExists(path) == null) {
            zkClient.createForPath(path);
        }
        return path;
    }

}
