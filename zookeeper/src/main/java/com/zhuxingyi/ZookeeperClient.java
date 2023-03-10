package com.zhuxingyi;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author zhuxingyi
 * @date 10/3/2023 下午4:45
 */
public class ZookeeperClient {
    public static void main(String[] args) throws Exception {
        //连接Zookeeper
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(60*1000)
                .connectionTimeoutMs(15*1000)
                .retryPolicy(new ExponentialBackoffRetry(3000,10))
                .namespace("hj")
                .build();
        client.start();

        String s = client.create().forPath("/zhuxingyi");
        System.out.println(s);
    }
}
