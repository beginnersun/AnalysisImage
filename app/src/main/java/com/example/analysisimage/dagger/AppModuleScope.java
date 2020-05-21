package com.example.analysisimage.dagger;

import javax.inject.Scope;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope  //声明这是一个自定义@Scope注解
@Retention(RUNTIME)
public @interface AppModuleScope {

}