package com.seai.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StopWatch;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Slf4j
public class AopConfig {

    @Around("@annotation(TrackExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);
        Object var5;
        try {
            var5 = joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("Execution of {} took {} ms", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
        }
        return var5;
    }
}