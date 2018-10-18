package com.taobao.csp.sentinel.dashboard.client.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class ZkClient implements AutoCloseable{
    private static Logger logger = LoggerFactory.getLogger(ZkClient.class);
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;
    
    private CuratorFramework zkClient = null;
    
    private boolean closed = false;
    
    private List<NodeCache> nodeCaches = new ArrayList<NodeCache>(); 
    
    public ZkClient(final String address){
        zkClient = initZookeeperr(address);
    }
    
    public CuratorFramework initZookeeperr(final String serverAddr) {
        try {
            CuratorFramework  client = CuratorFrameworkFactory.builder().
            connectString(serverAddr).
            retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES)).
            build();
            
            client.start();
            return client;
        } catch (Exception e) {
            logger.warn("[ZookeeperDataSource] Error occurred when initializing Zookeeper data source", e);
            e.printStackTrace();
        }
        return null;
    }

    public  CuratorFramework getZkClient() {
        return zkClient;
    }
    
    @Override
    public void close(){
        if(!closed && this.nodeCaches.isEmpty()){
            closed = true;
            zkClient.close();
        }
        
    }
    
    public byte[] forPath(String path) throws Exception{
        return zkClient.getData().forPath(path);
    }
    
    public Stat forPath(String path, byte[] data) throws Exception{
        return  zkClient.setData().forPath(path, data);
    }
    
    public Stat checkExists(String path) throws Exception{
        return zkClient.checkExists().forPath(path);
    }
    
    public String createForPath(String path) throws Exception{
        return zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
    }
    
    public String createForPath(String path, byte[] data) throws Exception{
        return zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
    }

    public synchronized void addNodeCaches(NodeCache nodeCache) {
        this.nodeCaches.add(nodeCache);
    }
    
    public synchronized void removeNodeCaches(NodeCache nodeCache) {
        this.nodeCaches.remove(nodeCache);
    }
    
}
