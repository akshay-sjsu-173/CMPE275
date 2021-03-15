package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable
     */
	public static int retryCount = 0; //start with zero and increment for each retry. reset at the end of operation.
	public static int maxRetries = 3;
	
	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public int retryTweetAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the method %s\n", joinPoint.getSignature().getName());
		Integer result = null;
		while(this.retryCount < this.maxRetries+1) {
			try {
				result = (Integer) joinPoint.proceed();
				System.out.printf("Finished the execution of the method %s with result %s\n", joinPoint.getSignature().getName(), result);
			} catch (Throwable e) {
				this.retryCount += 1;
				System.out.println("Proceeding to retry attempt " + this.retryCount);
				if(this.retryCount > this.maxRetries) {
					System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName());
					e.printStackTrace();
					//reset retry counter for next operation
					this.retryCount = 0;
					throw new IOException();
				}
			}
		}
		return result.intValue();
	}
	
	@Around("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void retryAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object result = null;
		boolean flag = false;
		while(this.retryCount < this.maxRetries+1 && !flag) {
			try {
				result = joinPoint.proceed();
				System.out.printf("Finished the execution of the method %s with result %s\n", joinPoint.getSignature().getName(), result);
				flag = true;
				//System.out.println("Retry Count : " + this.retryCount + " , Execution Status : " + flag);
				this.retryCount = 0;
			} catch (Throwable e) {
				this.retryCount += 1;
				//System.out.println("Proceeding to retry attempt " + this.retryCount);
				System.out.println("Retry Count : " + this.retryCount + " , Execution Status : " + flag);
				if(this.retryCount > this.maxRetries) {
					System.out.printf("Aborted the execution of the method %s\n", joinPoint.getSignature().getName());
					e.printStackTrace();
					//reset retry counter for next operation
					this.retryCount = 0;
					throw new IOException();
				}
			}
		}
		//return result.intValue();
	}
}
