package com.zhuxingyi.decorated;

/**
 * 浓缩咖啡
 *
 * @author zhuxingyi
 * @date 2023/3/27 21:49
 */
public class Espresso extends Beverage {


    public Espresso() {
        super.description = "Espresso";
    }

    @Override
    public double cost() {
        return 1.99;
    }
}
