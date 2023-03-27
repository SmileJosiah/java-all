package com.zhuxingyi.decorated;

/**
 * @author zhuxingyi
 * @date 2023/3/27 21:58
 */
public class DarkRoast extends Beverage {

    public DarkRoast() {
        super.description = "DarkRoast";
    }

    @Override
    public double cost() {
        return 0.99;
    }
}
