package ru.practicum.util.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllersLoggingAspect {

    @Pointcut("within(ru.practicum.user.UserController) || " +
            "within(ru.practicum.item.ItemController) || " +
            "within(ru.practicum.booking.BookingController) || " +
            "within(ru.practicum.request.ItemRequestController)")
    public void controllersLogging() {
    }

    @Before("controllersLogging()")
    public void logBeforeItemControllerMethod(JoinPoint joinPoint) {
        String msg = createMessageBefore(joinPoint);
        log.info(msg);
    }

    @AfterReturning(pointcut = "controllersLogging()", returning = "result")
    public void logAfterReturningBookingControllerMethod(JoinPoint joinPoint, Object result) {
        String fullClassname = joinPoint.getSignature().getDeclaringTypeName();
        String classname = fullClassname.substring(fullClassname.lastIndexOf('.') + 1);
        log.info("{} Result: {}", classname, result);
    }

    @AfterThrowing(pointcut = "controllersLogging()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.error("Exception in method: {} with cause: {}", methodName, exception.getMessage());
    }

    private String createMessageBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();

        String fullClassName = joinPoint.getTarget().getClass().getName();

        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

        String[] parameterNames = methodSignature.getParameterNames();

        Object[] args = joinPoint.getArgs();

        StringBuilder msg = new StringBuilder();
        msg.append(className)
                .append(".")
                .append(methodName)
                .append("(");

        if (parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length - 1; i++) {
                msg.append(parameterNames[i])
                        .append(": ")
                        .append(args[i] != null ? args[i] : "null")
                        .append(", ");
            }
            msg.append(parameterNames[parameterNames.length - 1])
                    .append(": ")
                    .append(args[parameterNames.length - 1] != null ? args[parameterNames.length - 1] : "null");
        }

        msg.append(")");
        return msg.toString();
    }
}