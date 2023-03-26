package com.zhuxingyi.observer.test;

/**
 * @author zhuxingyi
 * @date 2023/3/25 22:43
 */
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
