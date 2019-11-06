package com.example.analysisimage.util;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

public class Test {
    @NotNull
    public Test getValue(@NotNull Example example, @NotNull KProperty<?> property) {
                return new Test();

    }

    public void setValue(@NotNull Example example, @NotNull KProperty<?> property, @NotNull Test test) {
    }
}
