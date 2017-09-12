package com.candao.trace.log.aop;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.candao.trace.log.logger.InnerApiLogger;
import com.candao.trace.log.logger.MethodLogger;


@Component
@Aspect
public class LogAdviseDefine {

    // 定义一个 Pointcut, 使用 切点表达式函数 来描述对哪些 Join point 使用 advise.
    @Pointcut("@within(com.candao.trace.log.aop.ControllerLog)")
    public void log() {
    }

    @Pointcut("@annotation(com.candao.trace.log.aop.MethodLog)")
    public void methodLog(){}
    
    @Around("methodLog()")
    public Object methodInvokeExpiredTime2(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        long start = System.currentTimeMillis();
        stopWatch.start();
        // 开始
        Object retVal = pjp.proceed();
        // 结束
        stopWatch.stop();
        long end = System.currentTimeMillis();
        MethodLogger.info(pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()), end - start);
        return retVal;
    }
    
    // 定义 advise
	@Around("log()")
    public Object methodInvokeExpiredTime(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        long start = System.currentTimeMillis();
        stopWatch.start();
        // 开始
        Object retVal = pjp.proceed();
        // 结束
        stopWatch.stop();
        long end = System.currentTimeMillis();
        InnerApiLogger.info(pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()), end - start);
        return retVal;
    }
}