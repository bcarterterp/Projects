package shizzz;

import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Parser {
	private static String consumerKey = "pXN0UBqtNL0DfaHrYBGbw";
	private static String consumerSecret = "GzjOozBzVlKMN2u69GxNu9BMcqwlzQGJAqq6ORVoVo8";
	private static String accessToken = "2338836362-Tuesu2a3oGK1NDxhJQcL6f2UOsvVSUpKUoWCdu4";
	private static String accessSecret = "LxKLJoOAijVcOpvv7aS8GEcRtopbDaf5fXOIyzig8JTw8";
	
	
	
	public static boolean isValid (String twitterName){
		Boolean valid = false;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		 
	     cb.setDebugEnabled(true)
	           .setOAuthConsumerKey(consumerKey)
	          .setOAuthConsumerSecret(consumerSecret)
	            .setOAuthAccessToken(accessToken)
	          .setOAuthAccessTokenSecret(accessSecret);
		try{
			TwitterFactory factory = new TwitterFactory(cb.build());
			Twitter twitter = factory.getInstance();
			twitter.showUser(twitterName);
			valid = true;
		}catch(TwitterException e){
			if(e.getStatusCode() == 404){
				valid = false;
			}
		}
		return valid;
	}
	
	
	public static ArrayList<String> tweets(String twitter_name){
		ConfigurationBuilder cb = new ConfigurationBuilder();
	     cb.setDebugEnabled(true)
	           .setOAuthConsumerKey(consumerKey)
	          .setOAuthConsumerSecret(consumerSecret)
	            .setOAuthAccessToken(accessToken)
	          .setOAuthAccessTokenSecret(accessSecret);
		
		List<Status> status = null;
		ArrayList<String> accepted = new ArrayList<String>();
		
 		try{
			TwitterFactory factory = new TwitterFactory(cb.build());
			Twitter twitter = factory.getInstance();
			status = twitter.getUserTimeline(twitter_name, new Paging(1,30));
				for (Status s : status) {
            		if(isEvent(s.getText())){
            		String curr = (s).getText();
            		accepted.add(curr);
            		}
            	}
			
		}catch(TwitterException e){
			if(e.getStatusCode() == 404){
				return null;
			}else{
			e.printStackTrace();
			System.out.println("Failed to get time line:" + e.getMessage());
			System.exit(-1);
			}
		}
		return accepted;
	}
	
	
	public static Boolean isEvent (String tweets){
		/*Find if the tweet is about an event
		 * first check if there is a date, a time, "today", "tomorrow"
		 * if there is relay the whole tweet back*/
		//String pattern = "(([0-9]+(pm|am|:))|tonight|TONIGHT|TODAY|TOMORROW|tomorrow|Nvited)";
		String pattern = "(([0-9]+(pm|am|:))|(tonight|TONIGHT|TODAY|TOMORROW|tomorrow)|Nvited|((S|s)peaker))";
		String[] result = tweets.split(pattern);
		if(result.length > 1){
			return true;
		}
		return false;
	}

	

	 
	  public static void main(String[] args){
		// System.out.println(isValid("@@@@@@@@@@@@@"));
		  for(String s: tweets("SEE_UMD")){
			  System.out.println(s);
		  }
	  }
	 
	
	

}
