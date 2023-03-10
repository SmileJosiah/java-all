package com.zhuxingyi;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Unit test for simple App.
 */
public class AppTest {

    CuratorFramework client;

    @Before
    public void init() {
        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(3000, 10))
                .namespace("hj")
                .build();
        client.start();
    }

    /**
     * 创建普通节点
     *
     * @throws Exception
     */
    @Test
    public void creatNode() throws Exception {
        String result = client.create().forPath("/zhuxingyi/home", "xiaokang".getBytes(StandardCharsets.UTF_8));
        System.out.println(result);
    }

    /**
     * 获取节点
     */
    @Test
    public void getNode() throws Exception {
        String result = new String(client.getData().forPath("/zhuxingyi/home"));
        System.out.println(result);
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createTempNode() throws Exception {
        String s = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL).forPath("/zhuxingyi/libai");
        System.out.println(s);
    }

    @After
    public void close() {
        client.close();
    }
}
