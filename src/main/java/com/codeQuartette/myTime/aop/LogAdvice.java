package com.codeQuartette.myTime.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAdvice {

    // 패스, 바디, 유저 id, 메서드 이름, 실행 시간
    @Around("execution(* com.codeQuartette.myTime.controller.*.*(..))")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("=========================");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("Request Path = {}", request.getServletPath());

        log.info("signature = {}", joinPoint.getSignature());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            log.info("parameter = {}: {}", method.getParameters()[i].getName(), joinPoint.getArgs()[i]);
        }

        log.info("signature = {}", joinPoint.getSignature());

        long before = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long after = System.currentTimeMillis();

        log.info("running time = {}", after - before);

        log.info("=========================");

        return result;
    }
}
