package com.telappoint.admin.aspect.logger;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggerAspect {

   private static Logger logger = Logger.getLogger(LoggerAspect.class);

   @SuppressWarnings("unused")
   @Pointcut("within(com.telappoint.admin.appt..*)")
   private void publicMethods() {
   }

   @Before("within(com.telappoint.admin.appt..*)")
   public void logBefore(JoinPoint joinPoint) {
      logger.info("Execution Started: " + joinPoint.getTarget().getClass().getName() + ":"
            + joinPoint.getSignature().getName());
   }

   @After("within(com.telappoint.admin.appt..*)")
   public void logAfter(JoinPoint joinPoint) {
      logger.info("Execution Completed: " + joinPoint.getTarget().getClass().getName() + ":"
            + joinPoint.getSignature().getName());
   }

   @AfterThrowing(pointcut = "publicMethods()", throwing = "exception")
   public void logExceptions(Exception exception) {
      logger.error("Exception Occurred: " + exception.getMessage() + "\n", exception);
   }
}