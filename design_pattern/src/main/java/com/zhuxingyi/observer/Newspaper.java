package com.zhuxingyi.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxingyi
 * @date 2023/3/26 21:29
 */
public class Newspaper implements Subject {
    private String name;
    private String newspaperNo;

    public Newspaper(String name) {
        this.name = name;
    }

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getNewspaperNo() {
        return newspaperNo;
    }

    //发布最新的报刊
    public void publishLatest(String newspaperNo) {
        this.newspaperNo = newspaperNo;
        System.out.println(name + ",发布了最新的报刊，刊号是:" + newspaperNo);
        notifyObservers();
    }
}
