package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void printStats(TweetStatsService stats) {
		System.out.println("******  STATISTICS *******");
		System.out.println("Most productive user: " + stats.getMostProductiveUser());
		System.out.println("Most popular user: " + stats.getMostFollowedUser());
		System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
		System.out.println("Most popular message: " + stats.getMostPopularMessage());
		System.out.println("Most liked message: " + stats.getMostLikedMessage());
//		System.out.println("Most most message: " + stats.getMostPopularMessage());
		System.out.println("Most unpopular follower: " + stats.getMostUnpopularFollower());
		System.out.println("****** X ******** X *******");
	}
	
	public static void main(String[] args) {
		/***
		 * Following is a dummy implementation of App to demonstrate bean creation with
		 * Application context. You may make changes to suit your need, but this file is
		 * NOT part of the submission.
		 */

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
		TweetService tweeter = (TweetService) ctx.getBean("tweetService");
		TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

		try {
			/* Default Test */
//			 tweeter.follow("bob", "alice");
//			 Long msg = tweeter.tweet("alice", "first tweet");
//			 tweeter.like("bob", msg);
//			 tweeter.block("alice", "bob");
//			 tweeter.tweet("alice", "second tweet");
			 
			/* Test 1 */
//			 Long msg;
//			 tweeter.follow("bob","alice");
//			 tweeter.follow("bob","cathy");
//			 tweeter.follow("cathy","alice");
//			 tweeter.follow("duke","alice");
//			 tweeter.follow("duke","bob");
//			 tweeter.follow("cathy","duke");
//			 msg = tweeter.tweet("alice", "first tweet");
//			 msg = tweeter.tweet("alice", "second tweet");
//			 tweeter.like("bob", msg);
//			 tweeter.like("cathy", msg);
//			 msg = tweeter.tweet("duke", "second tweet");
////			 tweeter.like("bob", msg);
////			 tweeter.like("alice", msg);
//			 printStats(stats);
////			 tweeter.like("cathy", msg);
//			 msg = tweeter.tweet("alice", "third tweet");
//			 tweeter.like("bob", msg);
//			 msg = tweeter.tweet("duke", "tweet two");
//			 tweeter.like("cathy", msg);
////			 tweeter.like("alice", msg);
////			 tweeter.like("edward", msg);
////			 tweeter.like("felix", msg);
//			 msg = tweeter.tweet("bob", "tweet 11");
//			 msg = tweeter.tweet("cathy","tweet 21");
////			 stats.resetStatsAndSystem();
//			 printStats(stats);
//			 tweeter.block("alice", "edward");
//			 tweeter.block("alice", "felix");
//			 tweeter.block("bob", "cathy");
//			 tweeter.block("edward", "cathy");
			 
			/* Test 2 */
//			tweeter.block("bob", "alice");
//			tweeter.follow("alice", "bob");
//			Long msg = tweeter.tweet("bob", "hello world");
//			tweeter.like("alice", msg);
			/* Test 3
			 * 
			 */
			/* Test 4
			 * 
			 */
			//Invalid operations
			//tweeter.block("bob", "bob");
			//tweeter.like("bob", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		printStats(stats);
		
		ctx.close();
	}
}
