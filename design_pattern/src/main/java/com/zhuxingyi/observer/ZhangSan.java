package com.zhuxingyi.observer;

/**
 * @author zhuxingyi
 * @date 2023/3/26 21:30
 */
public class ZhangSan implements Observer {
    @Override
    public void update(Subject subject) {
        Newspaper newspaper = (Newspaper) subject;
        System.out.println("我是ZhangSan,我收到了" + newspaper.getName() + "报社的，第" + newspaper.getNewspaperNo() + "期报纸");
    }
}
