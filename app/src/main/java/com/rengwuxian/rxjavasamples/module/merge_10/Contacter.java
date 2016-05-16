package com.rengwuxian.rxjavasamples.module.merge_10;

/**
 * Created by ljb on 2016/3/17.
 */
public class Contacter {

    private String name;

    public Contacter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contacter{" +
                "name='" + name + '\'' +
                '}';
    }
}
