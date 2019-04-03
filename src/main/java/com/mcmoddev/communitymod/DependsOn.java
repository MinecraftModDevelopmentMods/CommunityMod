package com.mcmoddev.communitymod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DependsOn {
    /**
     * Define the modids of the submods you are depending on. Equal to {@link SubMod#modid()}.
     */
    String[] depend();
    
    /**
     * Define your sub-modid, equal to {@link SubMod#modid()}
     */
    String modid();
}
