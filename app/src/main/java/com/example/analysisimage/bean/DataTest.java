package com.example.analysisimage.bean;

public class DataTest {

    private String name;


    public void set(String name) {
        this.name = name + "555";
    }

    public String get() {
        System.out.println(this.name);
        return name;
    }
}
