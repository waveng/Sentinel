package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.taobao.csp.sentinel.dashboard.client.zk.AbstractReadable;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.client.zk.util.Util;


/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class ReadableDataSource<T> extends AbstractReadable<String, T> {

    private final String nodeType;

    private ZkClient zkClient = null;

    public ReadableDataSource(final ZkClient zkClient,final NodeType nodeType, Converter<String, T> parser) {
        super(parser);
        this.nodeType = nodeType.toString();
        this.zkClient = zkClient;
    }


    @Override
    public void close() throws Exception {
        if (this.zkClient != null) {
            this.zkClient.close();
        }
    }

    @Override
    public String read(String path) throws Exception {
        if (this.zkClient == null) {
            throw new IllegalStateException("Zookeeper has not been initialized or error occurred");
        }
        byte[] data = this.zkClient.forPath(path);
        if (data != null) {
            return new String(data);
        }
        return null;
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
