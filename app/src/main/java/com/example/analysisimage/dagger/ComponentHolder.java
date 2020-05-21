package com.example.analysisimage.dagger;

public class ComponentHolder {

    private static AppComponent component;

    public static void setComponent(AppComponent component) {
        ComponentHolder.component = component;
    }

    public static AppComponent getComponent() {
        return component;
    }
}
