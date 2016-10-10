import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCore {
	public
		String result = "";
		Twitter Twitter;
	private
		User User;
		static final String CONSUMER_KEY = "0jWz6LwCDdVsB5AjkCzn1wE5m";
		static final String CONSUMER_SECRET = "x0pIHYVa25F2S2IlU16V3DzuKkpFAz0EyVe1agNxg7fGtqSlyz";
	TwitterCore(){
		if(!this.auth()){
			System.out.println("認証に失敗しました");
			result = "認証に失敗しました。";
		}
		System.out.println("認証に成功しました。");
		result = "認証に成功しました。";
	}
	private boolean auth(){
		Twitter = TwitterFactory.getSingleton();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
		AccessToken token = getAccessToken();
		if(token == null){
			cb.setOAuthAccessToken(null);
			cb.setOAuthAccessTokenSecret(null);
			Twitter = new TwitterFactory(cb.build()).getInstance();
			//RequestToken発行
			try {
				RequestToken req = Twitter.getOAuthRequestToken();
				System.out.println(req.getAuthorizationURL());
				String pin = new Scanner(System.in).next();
				token = Twitter.getOAuthAccessToken(req,pin);
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}else{
			Twitter = new TwitterFactory(cb.build()).getInstance();
		}
		Twitter.setOAuthAccessToken(token);
	/*
		//TwitterFactory TwiFac = new TwitterFactory();
		Twitter = new TwitterFactory().getInstance();
		try {
			User = Twitter.verifyCredentials();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
	*/
		return true;
	}

	private AccessToken getAccessToken(){
		File f = new File(getTokenFileName());
		ObjectInputStream ois;
		AccessToken token = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			token = (AccessToken) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return token;
	}

	private void writeAccessToken(AccessToken token){
		File f = new File(getTokenFileName());
		File d = f.getParentFile();
		if(!f.exists())d.mkdirs();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(token);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルが見当たりません。生成に失敗した可能性があります。");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("XD");
			e.printStackTrace();
		}
	}

	private static String getTokenFileName(){
		return System.getProperty("user.home") + "./twitter/token.dat";
	}

	/*
	private boolean oauth(JFrame frame){
		try{
		Twitter = TwitterFactory.getSingleton();
		RequestToken reqToken = Twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		int option = JOptionPane.showConfirmDialog(frame, "アカウント認証","Twitter Oauth", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.YES_OPTION){
			Desktop.getDesktop().browse(new URI(reqToken.getAuthorizationURL()));
			String pin = JOptionPane.showInputDialog(frame, "PINを貼り付けてください","Twitter Oauth");
			accessToken = Twitter.getOAuthAccessToken(reqToken, pin);
			Twitter.setOAuthAccessToken(accessToken);
			return true;
		}
		}catch(IOException e){
			System.out.println(e);
		}catch(URISyntaxException e){
			System.out.println(e);
		}catch(HeadlessException e){
			System.out.println(e);
		}catch(TwitterException e){
			System.out.println(e);
		}
		return false;
	}
	*/

	//tweet
	public String tweet(String tweet){
		try {
			Twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
			return "Failed to tweet...";
		}
		return "Tweet was successful";
	}

	//reply
	public String reply(String tweet,long id) {
		try {
			StatusUpdate su = new StatusUpdate(tweet);
			su.setInReplyToStatusId(id);
			Twitter.updateStatus(su);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "Failed to tweet...";
		}
		return "Tweet was successful";
	}

	public Status retweet(long id){
		try {
			return Twitter.retweetStatus(id);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}

	public Boolean destroy(long id){
		try {
			Twitter.destroyStatus(id);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Boolean createFavorite(long id){
			try {
				Twitter.createFavorite(id);
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return false;
			}

		return true;
	}

	public Boolean destroyFavorite(long id){
		try {
			Twitter.destroyFavorite(id);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getResult(){
		return result;
	}

}