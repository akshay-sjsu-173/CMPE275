package edu.sjsu.cmpe275.aop.tweet;

import java.util.*;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */
	private static Map<Long, Map>  messageMap = new HashMap<Long, Map>();
	private static Map<String, Map>  userDetails = new HashMap<String, Map>();
	
	//Getters and Setters
	public Map<Long, Map> getMessageMap() {
		return this.messageMap;
	}

	public void setMessageMap(Map<Long, Map> messageMap) {
		this.messageMap = messageMap;
	}

	public Map<String, Map> getUserDetails() {
		return this.userDetails;
	}

	public void setUserDetails(Map<String, Map> userDetails) {
		this.userDetails = userDetails;
	}
	
	@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		this.setMessageMap(new HashMap<Long, Map>());
		this.setUserDetails(new HashMap<String, Map>());
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		
		int max = 0;
		Map temp = this.getMessageMap();
		for (Object entry : temp.keySet()) {
			String m = (String)((Map)temp.get(entry)).get("message");
			if(m.length() > max)
				max = m.length();
		}
		return max;
	}

	@Override
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		int max = 0;
		String u = null;
		Map temp = this.getUserDetails();
		for (Object entry : temp.keySet()) {
			if(u == null)
				u = (String)entry;
			
			ArrayList f = (ArrayList)((Map)temp.get(entry)).get("followers");
			//System.out.println(entry + " , " + u + " , " + f.size());
			//System.out.println(f.size());
			// Ensure alphabetical order is used in case of a tie
			if(max < f.size() || (max == f.size() && u.compareTo((String)entry) > 0) ) {
				max = f.size();
				u = (String)entry;
			}
		}
		return u;
	}

	@Override
	public String getMostPopularMessage() {
		// TODO Auto-generated method stub
		Map shareList = new HashMap<String, List>();
		Map temp = this.getMessageMap();
		for (Object entry : temp.keySet()) {
			String m = (String)((Map)temp.get(entry)).get("message");
			List s = (ArrayList)((Map)temp.get(entry)).get("sharedWith");
			if(shareList.containsKey(m)) {
				List l = (List)shareList.get(m);
				for (Object x : s){
					   if (!l.contains(x))
					      l.add(x);
				}
			} else {
				shareList.put(m, s);
			}
		}
		int max = 0;
		String popularMessage = null;
		for(Object x: shareList.keySet()) {
			if(((List)shareList.get(x)).size() > max) {
				max = ((List)shareList.get(x)).size();
				popularMessage = (String)x;
			}
		}
		return popularMessage;
	}
	
	@Override
	public String getMostProductiveUser() {
		// TODO Auto-generated method stub
		Map tweetLengths = new HashMap<String, Long>();
		Map temp = this.getMessageMap();
		for (Object entry : temp.keySet()) {
			String m = (String)((Map)temp.get(entry)).get("message");
			String u = (String)((Map)temp.get(entry)).get("sharedBy");
			if(tweetLengths.containsKey(u)) {
				System.out.println();
				tweetLengths.put(u, (Integer)tweetLengths.get(u)+m.length());
			} else {
				tweetLengths.put(u, m.length());
			}
		}
		//System.out.println(tweetLengths);
		int max = 0;
		String productiveUser = null;
		for(Object user: tweetLengths.keySet()) {
			if(productiveUser == null)
				productiveUser = (String)user;
			if((Integer)tweetLengths.get(user) > max || (max == (Integer)tweetLengths.get(user) && productiveUser.compareTo((String)user) > 0)) {
				max = (Integer)tweetLengths.get(user);
				productiveUser = (String)user;
			}
		}
		return productiveUser;
	}

	@Override
	public Long getMostLikedMessage() {
		// TODO Auto-generated method stub
		long max = 0;
		Object result = null;
		Map temp = this.getMessageMap();
		for (Object entry : temp.keySet()) {
			//System.out.println(entry);
			if(result == null)
				result = (Long)entry;
			ArrayList m = (ArrayList)((Map)temp.get(entry)).get("likedBy");
			//System.out.println(entry + " : " + m.size());
			if(m.size() > max || (m.size() == max && (Long)result > (Long)entry)) {
				max = m.size();
				result = (Long)entry;
			}	
		}
		if(max == 0)
			return null;
//		if(max > 0) {
//			System.out.println(temp);
//			return max;
//		}
		return (Long)result;
	}

	@Override
	public String getMostUnpopularFollower() {
		// TODO Auto-generated method stub
		Map temp = this.getUserDetails();
		Map blockCount = new HashMap<String, Long>();
		for (Object entry : temp.keySet()) {
			List m = (ArrayList)((Map)temp.get(entry)).get("blocked");
			for(Object user: m) {
				if(blockCount.containsKey(user)) {
					blockCount.put(user, (Integer)blockCount.get(user)+1);
				}
				else {
					blockCount.put(user, 1);
				}		
			}
		}
		int max = 0;
		String unpopularUser = null;
		for(Object user: blockCount.keySet()) {
			if((Integer)blockCount.get(user) > max) {
				max = (Integer)blockCount.get(user);
				unpopularUser = (String)user;
			}
		}
		return unpopularUser;
	}

}



