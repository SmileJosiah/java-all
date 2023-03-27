package com.zhuxingyi.decorated;

/**
 * @author zhuxingyi
 * @date 2023/3/27 21:51
 */
public class HouseBlend extends Beverage {

    public HouseBlend() {
        super.description = "HouseBlend Coffee";
    }

    @Override
    public double cost() {
        return 0.89;
    }
}
