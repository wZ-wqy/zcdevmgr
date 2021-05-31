package com.dt.core.cache;

import com.dt.core.tool.util.ToolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 缓存拦截，用于注册方法信息
 */
@Aspect
@Component
public class CachingAnnotationsAspect {

    @Autowired
    private InvocationRegistry cacheRefreshSupport;

    private <T extends Annotation> List<T> getMethodAnnotations(AnnotatedElement ae, Class<T> annotationType) {
        List<T> anns = new ArrayList<T>(2);
        // look for raw annotation
        T ann = ae.getAnnotation(annotationType);
        if (ann != null) {
            anns.add(ann);
        }
        // look for meta-annotations
        for (Annotation metaAnn : ae.getAnnotations()) {
            ann = metaAnn.annotationType().getAnnotation(annotationType);
            if (ann != null) {
                anns.add(ann);
            }
        }
        return (anns.isEmpty() ? null : anns);
    }

    private Method getSpecificmethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // The method may be on an interface, but we need attributes from the
        // target class. If the target class is null, the method will be
        // unchanged.
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(pjp.getTarget());
        if (targetClass == null && pjp.getTarget() != null) {
            targetClass = pjp.getTarget().getClass();
        }
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the
        // original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }

    @Pointcut("@annotation(org.springframework.cache.annotation.Cacheable)")
    public void pointcut() {
    }

    // 只支持字符串+参数
    private String parseKey(String key, Method method, Object[] args) {
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    @Around("pointcut()")
    public Object registerInvocation(ProceedingJoinPoint joinPoint) throws Throwable {

        // 添加主动刷新的条件，有设置主动刷新功能
        Method method = this.getSpecificmethod(joinPoint);
        List<Cacheable> annotations = this.getMethodAnnotations(method, Cacheable.class);
        StringBuilder sb = new StringBuilder();
        for (Object obj : joinPoint.getArgs()) {
            if (obj != null) {
                sb.append(obj.toString());
            }
        }
        // 这里只有一个cacheable
        String pkey = sb.toString();
        for (Cacheable cacheables : annotations) {
            String key = (cacheables.key() == null ? "" : cacheables.key());
            List<String> values = Arrays.asList(cacheables.value());
            if (values.size() > 0) {
                String value = values.get(0);
                // 如果key中存在#root则不缓存，value
                String rkey = "";
                String[] vsp = value.split("#");
                if (vsp.length == 3) {
                    //获取realy key
                    if (ToolUtil.isEmpty(key)) {
                        // key从arg中获取
                        rkey = pkey;
                    } else {
                        rkey = parseKey(key, method, joinPoint.getArgs());
                    }
                    if (ToolUtil.isNotEmpty(rkey)) {
                        CacheableEntity ce = new CacheableEntity(vsp[0], rkey);
                        ce.setExpiredtime(ToolUtil.toInt(vsp[1], -1));
                        ce.setRefreshtime(ToolUtil.toInt(vsp[2], -1));
                        CachedInvocation invocation = new CachedInvocation(rkey, joinPoint.getTarget(), method,
                                joinPoint.getArgs(), ce);
                        cacheRefreshSupport.registerInvocation(invocation);
                    }
                }
            }
        }
        return joinPoint.proceed();

    }

}