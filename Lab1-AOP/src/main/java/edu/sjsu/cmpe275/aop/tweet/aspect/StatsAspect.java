package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;
	//Utility Methods
	public void createUser(String user) {
		System.out.println("Create user " + user);
		Map userDetails = stats.getUserDetails();
		Map<String, List> details = new HashMap<String, List>();
	    details.put("followers", new ArrayList());
	    details.put("blocked", new ArrayList());
	    details.put("shareList", new ArrayList());
	    userDetails.put(user, details);
	    stats.setUserDetails(userDetails);
	}
	
	//Stats Aspect Methods
	@AfterReturning(pointcut="execution(public Long edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))", returning="messageId")
	public void statsTweet(JoinPoint joinPoint, Long messageId) {
		System.out.printf("Register tweet after the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		String message = (String)args[1];
		String user = (String)args[0];
		// Add new message to message map
		Map messageMap = stats.getMessageMap();
		Map userDetails = stats.getUserDetails();
		Map<String, Object> newMsgMap = new HashMap<String, Object>();
		newMsgMap.put("message", message);
		newMsgMap.put("sharedBy", user);
		newMsgMap.put("likedBy", new ArrayList<String>());
		List sharedWith;
		if(userDetails.containsKey(user)) {
			sharedWith = new ArrayList((ArrayList)((Map)userDetails.get(user)).get("shareList"));
		} else {
			this.createUser(user);
			sharedWith = new ArrayList<String>();
		}
		newMsgMap.put("sharedWith", sharedWith);
		messageMap.put(messageId, newMsgMap);
		stats.setMessageMap(messageMap);
		//System.out.println(stats.getMessageMap());
	}
	
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void statsFollow(JoinPoint joinPoint) {
		System.out.printf("Update followers after the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		//Setup args and get data maps
		String followee = (String)args[1];
		String follower = (String)args[0];
		//Map messageMap = stats.getMessageMap();
		Map userDetails = stats.getUserDetails();
		//Execute business logic
		if(!userDetails.containsKey(followee)) {
		    this.createUser(followee);
		    //Update userDetails for further operations
		    userDetails = stats.getUserDetails();
		    //System.out.println(userDetails);
		}
		Map user = (Map)userDetails.get(followee);
		if(!((ArrayList)user.get("blocked")).contains(follower)) {
			((ArrayList)user.get("shareList")).add(follower);
		}
		((ArrayList)user.get("followers")).add(follower);
	    
		if(!userDetails.containsKey(follower))
	    	this.createUser(follower);
		stats.setUserDetails(userDetails);	
	}
	
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void statsLike(JoinPoint joinPoint) {
		System.out.printf("Update likes after the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		long messageId = (Long)args[1];
		String user = (String)args[0];
		Map messageMap = stats.getMessageMap();
		Map userDetails = stats.getUserDetails();
		//Execute business logic
		String msgTweeter = (String)((Map)messageMap.get(messageId)).get("sharedBy");
		/* If user is in shareList, he is successfully following the sender and is also not currently blocked.
		 * And if user has not already liked the tweet
		 */
		ArrayList tempShare = (ArrayList)((Map)userDetails.get(msgTweeter)).get("shareList");
		//ArrayList tempFollow = (ArrayList)userDetails.get(msgTweeter).get("followers");
		ArrayList likedBy = (ArrayList)((Map)messageMap.get(messageId)).get("likedBy");
		likedBy.add(user);
		//Ensure user exists if he tries to liek a message
		if(!userDetails.containsKey(user))
			this.createUser(user);
		stats.setMessageMap(messageMap);
		//stats.setUserDetails(userDetails);
	}
	
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void statsBlock(JoinPoint joinPoint) {
		System.out.printf("Update blocked users after the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		//Setup args and get data maps
		String user = (String)args[0];
		String follower = (String)args[1];
		//Map messageMap = stats.getMessageMap();
		Map userDetails = stats.getUserDetails();
		//Execute business logic
		// Add user to blocked list
		// Remove user from shareList
		//System.out.println("UserDetails before block : " + userDetails);
		if(userDetails.containsKey(user)) {
			//((ArrayList)((Map)userDetails.get(user)).get("blocked")).add(follower);
			((ArrayList)((Map)userDetails.get(user)).get("shareList")).remove(follower);
		} else {
//			Map<String, List> details = new HashMap<String, List>();
//			ArrayList blocked = new ArrayList();
//			//blocked.add(follower);
//			details.put("followers", new ArrayList());
//		    details.put("blocked", blocked);
//		    details.put("shareList", new ArrayList());
//		    userDetails.put(user, details);
			this.createUser(user);
			//update userDetails after creating user
			userDetails = stats.getUserDetails();
		}
		((ArrayList)((Map)userDetails.get(user)).get("blocked")).add(follower);
		stats.setUserDetails(userDetails);
		if(!userDetails.containsKey(follower))
			this.createUser(follower);		
	}
}
