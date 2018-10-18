# sentinel-dashboard-datasource-zookeeper

## 0. 概述

sentinel-dashboard 是对控制台动态规则推送zookeeper存储的实现，它为提供了将动态规则推送到zookeeper中。在 Sentinel 控制台上，我们可以配置规则并实时查看效果。

## 1. 实现描述
客户端通接入 sentinel-datasource-zookeeper 初始化时，将会把动态规则存放的zk path 存储在以下规则path中 

```
"/" + AppNameUtil.getAppName() + "/" + HostNameUtil.getIp() +":"+ TransportConfig.getPort() + "/" + nodeType;
```
服务端通过同样的规则(客户端接入dashboard时会将appname、ip、prot推送到dashboard)获取到动态规则存放的zk path后，再通过path到zk中读取规则信息。


## 2. 客户端接入

客户端需要引用 sentinel-datasource-zookeeper 包
```
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-zookeeper</artifactId>
    <version>x.x.x</version>
</dependency>
```
客户端配置使用见[sentinel-demo-zookeeper-datasource](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-zookeeper-datasource)

## 3. dashboard端接入

dashboard端引用 sentinel-dashboard-datasource-zookeeper 包
```
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-dashboard-datasource-zookeeper</artifactId>
    <version>x.x.x</version>
</dependency>
```
添加配置项
```
csp.sentinel.dashboard.zk.address=127.0.0.1:2181
```

接入完成

## 3. 验证是否接入成功

客户端正确配置并启动后，会主动向控制台发送心跳包，汇报自己的存在；控制台收到客户端心跳包之后，会在左侧导航栏中显示该客户端信息。控制台能够看到客户端的机器信息，则表明客户端接入成功了。
可以对动态规则进行添加和修改，查看zk中可以发现数据已经推送到zk,查看客户端日志可发现客户端也接收到规则


更多：[控制台功能介绍](./Sentinel_Dashboard_Feature.md)。