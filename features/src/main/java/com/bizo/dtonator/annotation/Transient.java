package com.bizo.dtonator.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Transient {
  /**
   * Returns whether or not the {@code Introspector} should
   * construct artifacts for the annotated method.
   * @return whether or not the {@code Introspector} should
   * construct artifacts for the annotated method
   */
  boolean value() default true;
}
