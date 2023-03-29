package com.zhuxingyi.factory.simplefactory;

import java.util.Objects;

/**
 * @author zhuxingyi
 * @date 2023/3/29 21:43
 */
public class SimplePizzaFactory {
    public Pizza createPizza(String type) {
        if (Objects.equals(type, "cheese")) {
            return new CheesePizza();
        } else if (Objects.equals(type, "veggie")) {
            return new VeggiePizza();
        } else if (Objects.equals(type, "clam")) {
            return new ClamPizza();
        }
        return null;
    }
}
