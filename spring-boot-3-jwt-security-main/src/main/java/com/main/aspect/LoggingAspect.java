package com.main.aspect;

import com.main.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("(@within(org.springframework.web.bind.annotation.RestController) ||" +
            " @within(org.springframework.stereotype.Controller)) &&" +
            " !@annotation(com.main.aspect.NoLogging)")
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
        
        targetLogger.info("User: {} (ID: {}) | API: {}.{} ",
                userName, userId, className, methodName);
    }
}