package com.taobao.csp.sentinel.dashboard.client.zk.util;
/**
 * 
 * @author wangbo 2018年10月15日 上午8:51:41
 * @version 0.0.1
 * @since 0.0.1
 */
public class Util {
    public static String getTypePath(String app, String ip, String port, String typePath) {
        return "/" + app + "/" + ip +":"+ port + "/" + typePath;
    }
}
