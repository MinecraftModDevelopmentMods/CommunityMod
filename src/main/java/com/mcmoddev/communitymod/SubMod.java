package com.mcmoddev.communitymod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be added to a class for it to be loaded by the mod. This system is used
 * to allow many PRs with minimal chance of merge conflicts.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubMod {

    /**
     * Define the name of your submod here. This is required for all submods.
     */
    String name();

    /**
     * A short description of the submod. This is optional but should be provided.
     */
    String description() default "";

    /**
     * Define the attribution and authorship information for your submod here. This is required
     * for all submods.
     */
    String attribution();

    /**
     * This can be used to make your module only load on the client.
     */
    boolean clientSideOnly() default false;

    /**
     *
     */
    boolean requiresMcRestart() default true;
}
