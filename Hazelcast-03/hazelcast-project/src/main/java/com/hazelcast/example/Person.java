package com.hazelcast.example;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private static final long serialVersionUID = 1L;

    private Person() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }
}

