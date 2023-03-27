package com.zhuxingyi.decorated;

/**
 * @author zhuxingyi
 * @date 2023/3/27 21:54
 */
public class Soy extends CondimentDecorator {
    Beverage beverage;

    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public double cost() {
        double cost = beverage.cost();
        if (getSize() == "TALL") {
            cost += 0.1;
        } else if (getSize() == "GRANDE") {
            cost += 0.15;
        } else if (getSize() == "VENTI") {
            cost += 0.20;
        }
        return cost;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }
}
