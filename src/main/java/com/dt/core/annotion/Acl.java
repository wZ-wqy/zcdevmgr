package com.dt.core.annotion;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Acl {
    /**
     * ACL_ALLOW:无限制
     * ACL_USER:登录后就可以访问
     * ACL_DENY:登录后并且需要赋权才能访问
     * TYPE_API:指定接口
     * TYPE_VIEW:视图
     */
    String ACL_ALLOW = "allow";
    String ACL_USER = "user";
    String ACL_DENY = "deny";
    String TYPE_API = "api";
    String TYPE_VIEW = "view";
    String type() default TYPE_API;
    String value() default ACL_DENY;
    String info() default "";

}
