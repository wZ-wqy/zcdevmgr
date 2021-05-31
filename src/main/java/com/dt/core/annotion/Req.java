package com.dt.core.annotion;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Req {
    boolean required() default true;
}
