package com.main.aspect;

import com.main.user.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.main..*(..)) &&" +
            " !@annotation(com.main.aspect.NoLogging) &&" +
            " !execution(* com.main.config..*(..)) && " +
            " !execution(* com.main.aspect..*(..)) &&" +
            " !within(org.springframework.web.filter.OncePerRequestFilter+) &&" +
            " !within(jakarta.servlet.Filter+)")
    public void logMethodCall(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = signature.getDeclaringType();
        String className = targetClass.getName();
        String methodName = signature.getMethod().getName();
        Logger targetLogger = LoggerFactory.getLogger(targetClass);
        
        String userName = "Guest";
        String userId = "N/A";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && 
            authentication.isAuthenticated() && 
            !(authentication instanceof AnonymousAuthenticationToken) &&
            authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userId = String.valueOf(user.getId());
            if (user.getFirstname() != null && user.getLastname() != null) {
                userName = user.getFirstname() + " " + user.getLastname();
            } else if (user.getPhone() != null) {
                userName = user.getPhone();
            }
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String threadName = Thread.currentThread().getName();
        targetLogger.info("User: {} (ID: {}) | API: {}.{} | Time: {} | Thread: {}",
                userName, userId, className, methodName, timestamp, threadName);
    }

    @AfterThrowing(pointcut = "execution(* com.main..*(..)) &&" +
            " !@annotation(com.main.aspect.NoLogging) &&" +
            " !execution(* com.main.config..*(..)) && " +
            " !execution(* com.main.aspect..*(..)) &&" +
            " !within(org.springframework.web.filter.OncePerRequestFilter+) &&" +
            " !within(jakarta.servlet.Filter+)", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = signature.getDeclaringType();
        String className = targetClass.getName();
        String methodName = signature.getMethod().getName();
        Logger targetLogger = LoggerFactory.getLogger(targetClass);
        targetLogger.error("{}.{} - Exception occurred: {}", className, methodName, exception.getMessage());
    }

    @Around("execution(* com.main..*(..)) &&" +
            " !@annotation(com.main.aspect.NoLogging) &&" +
            " !execution(* com.main.config..*(..)) && " +
            " !execution(* com.main.aspect..*(..)) &&" +
            " !within(org.springframework.web.filter.OncePerRequestFilter+) &&" +
            " !within(jakarta.servlet.Filter+)")
    public Object logPerformanceMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Get the actual target class (not the proxy)
        Class<?> targetClass = signature.getDeclaringType();
        
        // Use the actual class name (not the proxy)
        String className = targetClass.getName();
        String methodName = method.getName();
        
        // Use a logger for the actual target class
        Logger targetLogger = LoggerFactory.getLogger(targetClass);
        
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        // Log after method execution with execution time
        targetLogger.info("{}.{} after {}ms", className, methodName, executionTime);
        
        return result;
    }
}