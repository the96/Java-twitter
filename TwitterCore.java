import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterCore {
	public
		String result = "";
		Twitter Twitter;
	private
		User User;
	TwitterCore(){
		if(!this.auth()){
			System.out.println("認証に失敗しました");
			result = "認証に失敗しました。";
		}
		System.out.println("認証に成功しました。");
		result = "認証に成功しました。";
	}
	private boolean auth(){
		//TwitterFactory TwiFac = new TwitterFactory();
		Twitter = new TwitterFactory().getInstance();
		try {
			User = Twitter.verifyCredentials();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
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