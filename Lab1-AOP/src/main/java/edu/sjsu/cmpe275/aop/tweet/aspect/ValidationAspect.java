package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.security.AccessControlException;
import java.util.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(0)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	@Autowired TweetStatsServiceImpl stats;
	
	@Before("execution(public Long edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void validateTweet(JoinPoint joinPoint) {
		System.out.printf("Permission check before the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		//Check if any parameter is null
		for(Object x: args) {
			if(x == null) {
				//System.out.println("Throw Illegal Argument");
				throw new IllegalArgumentException("Parameter cannot be NULL.");
			}
		}
		//Check if tweet length is within permissible limit
		if(((String)args[1]).length() > 140){
			//System.out.println("Throw Illegal Argument");
			throw new IllegalArgumentException("Tweet length cannot exceed 140 characters.");
		}
		
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void validateFollow(JoinPoint joinPoint) {
		System.out.printf("Input validation before the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		//Check if any parameter is null
		for(Object x: args) {
			if(x == null) {
				//System.out.println("Throw Illegal Argument");
				throw new IllegalArgumentException("Parameter cannot be NULL.");
			}
		}
		//Check if user is trying to follow himself
		if(((String)args[0]).equals((String)args[1])){
			//System.out.println("Throw Illegal Argument");
			throw new IllegalArgumentException("User cannot follow himself.");
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void validateLike(JoinPoint joinPoint) {
		System.out.printf("Input validation before the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		//Check if any parameter is null
		for(Object x: args) {
			//System.out.println(x);
			if(x == null) {
				//System.out.println("Throw Illegal Argument");
				throw new IllegalArgumentException("Parameter cannot be NULL.");
			}
		}
		//Setup args and get data maps
		long messageId = (Long)args[1];
		String user = (String)args[0];
		Map messageMap = stats.getMessageMap();
		Map userDetails = stats.getUserDetails();
		
		if(!messageMap.containsKey(messageId))
			throw new IllegalArgumentException("Messade with ID " + messageId + " does not exist.");
		String msgTweeter = (String)((Map)messageMap.get(messageId)).get("sharedBy");
		//System.out.println(userDetails);
		ArrayList tempShare = (ArrayList)((Map)userDetails.get(msgTweeter)).get("shareList");
		ArrayList likedBy = (ArrayList)((Map)messageMap.get(messageId)).get("likedBy");
		if(!tempShare.contains(user) || likedBy.contains(user)) {
			//System.out.println("Unable to like the message. Throw valid exception");
			if(!tempShare.contains(user))
				throw new AccessControlException(user + " does not have permission to LIKE the message with ID " + messageId + ".");
			if(likedBy.contains(user))
				throw new AccessControlException(user + " cannot LIKE the same message again.");
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void validateBlock(JoinPoint joinPoint) {
		System.out.printf("Input validation before the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String user = (String)args[0];
		String follower = (String)args[1];
		//Check if any parameter is null
		for(Object x: args) {
			if(x == null) {
				//System.out.println("Throw Illegal Argument");
				throw new IllegalArgumentException("Parameter cannot be NULL.");
			}
		}
		//Check if user is trying to block himself
		if(((String)args[0]).equals((String)args[1])){
			//System.out.println("Throw Illegal Argument");
			throw new IllegalArgumentException(user + " cannot BLOCK himself.");
		}
	}

}
