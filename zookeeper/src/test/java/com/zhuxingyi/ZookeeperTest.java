package com.zhuxingyi;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Unit test for simple App.
 */
public class ZookeeperTest {

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


    @Test
    public void createParentNodeTest() throws Exception {
        //创建父节点，如果父节点不存在
        String s = client.create().creatingParentsIfNeeded().forPath("/moximoxi/bobo");
        System.out.println(s);

    }

    /**
     * 事务操作测试
     */
    @Test
    public void transactionTest() throws Exception {
        CuratorOp curatorOp = client.transactionOp().create().forPath("/haha141");
        CuratorOp curatorOp1 = client.transactionOp().create().forPath("/zhuxingyi1341");
        List<CuratorTransactionResult> curatorTransactionResults = client.transaction().forOperations(curatorOp, curatorOp1);
        curatorTransactionResults.forEach(item -> System.out.println(item.getResultStat()));
    }

    @Test
    public void existsTest() throws Exception {
        Stat stat = client.checkExists().forPath("/haha1411");
        System.out.println(stat != null);
    }

    /**
     * 异步执行
     */
    @Test
    public void inback() throws Exception {
        String s = client.create().withMode(CreateMode.PERSISTENT).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        }).forPath("/zhuxingyi789");
        System.out.println(s);
    }

    /**
     * node cache 监听某一特定的节点
     */
    @Test
    public void testNodeCache() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final NodeCache nodeCache = new NodeCache(client,"/zhuxingyi");
        nodeCache.getListenable().addListener(() -> {
            System.out.println("/zhuxingyi节点有变化了");
            byte[] data = nodeCache.getCurrentData().getData();

            countDownLatch.countDown();
        });
        nodeCache.start(true);
        countDownLatch.await();
    }

    @After
    public void close() {
        client.close();
    }
}
