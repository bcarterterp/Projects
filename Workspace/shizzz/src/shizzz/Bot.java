package shizzz;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class Bot implements Serializable {
	private Twitter twitter;
	private AccessToken aT;
	private long aTID;
	
	public static void main(String args[]) throws Exception{
		Bot bot = new Bot();
		
		File file = new File("twitterdetails.bot");
		boolean exists = file.exists();
		if (exists) {
			bot = readObject(bot);
		}else if(!exists){
			getTwitterAccess(bot);
		}
		menu(bot);
	}

	private static void menu(Bot bot) throws TwitterException,IOException {
//		System.out.prin
		
	}

	private static Bot readObject(Bot bot) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void getTwitterAccess(Bot bot) {
		// TODO Auto-generated method stub
		
	}
	
	
}
