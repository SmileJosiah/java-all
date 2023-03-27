package com.zhuxingyi.decorated;

/**
 * @author zhuxingyi
 * @date 2023/3/27 21:47
 */
public abstract class Beverage {
    String description = "Unknown Beverage";
    String size = "Unknown Size";

    public String getDescription() {
        return description;
    }

    public String getSize() {
        return size;
    }

    public abstract double cost();


}
