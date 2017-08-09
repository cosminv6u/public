package uk.tm.cosmin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Cosmin on 7/25/2017.
 */
@Aspect
@Component
public class PalindromeAspect {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeAspect.class);

    @Around("execution(* uk.tm.cosmin..*(..))")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); //continue on the intercepted method
        long end = System.currentTimeMillis();

        LOG.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
                + " took: " + (end - start) + " millis to execute!");

        return proceed;
    }

}
