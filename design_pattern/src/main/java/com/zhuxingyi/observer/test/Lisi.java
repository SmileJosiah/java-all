package com.zhuxingyi.observer.test;

/**
 * @author zhuxingyi
 * @date 2023/3/26 21:33
 */
public class Lisi implements Observer{
    @Override
    public void update(Subject subject) {
        Newspaper newspaper = (Newspaper) subject;
        System.out.println("我是Lisi,我收到了" + newspaper.getName() + "报社的，第" + newspaper.getNewspaperNo() + "期报纸");
    }
}
