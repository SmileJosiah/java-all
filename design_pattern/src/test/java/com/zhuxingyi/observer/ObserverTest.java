package com.zhuxingyi.observer;

import org.junit.Test;

/**
 * @author zhuxingyi
 * @date 2023/3/26 21:35
 */

public class ObserverTest {
    @Test
    public void notifyObserversTest(){
        //创建一个新华报社
        Newspaper xinhua = new Newspaper("新华社");
        ZhangSan zhangSan = new ZhangSan();
        Lisi lisi = new Lisi();
        xinhua.registerObserver(zhangSan);
        xinhua.registerObserver(lisi);
        xinhua.publishLatest("2023 02 28");

        Newspaper peoples_Daily = new Newspaper("人民日报");
        peoples_Daily.registerObserver(zhangSan);
        peoples_Daily.publishLatest("2023 03 01");
        peoples_Daily.removeObserver(zhangSan);

        peoples_Daily.publishLatest("2023 03 12");


    }



}
