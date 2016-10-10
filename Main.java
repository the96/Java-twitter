import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Main implements ActionListener{
	TwitterCore tw;
	GUI gui;
	TweetInfo twInfo;
	Main(){
		gui = new GUI();
		tw = new TwitterCore();
		twInfo = new TweetInfo();
		gui.twButton.addActionListener(this);
		for(int i = 0;i < gui.arrayTweet.length;i++){
			gui.arrayTweet[i].bReply.addActionListener(this);
			gui.arrayTweet[i].bRetweet.addActionListener(this);
			gui.arrayTweet[i].bLike.addActionListener(this);
		}
		TwitterStream stream = new TwitterStreamFactory().getInstance();
		StatusListener listener = new StatusListener(){
			@Override
			public void onException(Exception arg0) {
				// TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void onStatus(Status status) {
				// TODO 自動生成されたメソッド・スタブ
				gui.receiveTweet(status);
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO 自動生成されたメソッド・スタブ
				System.out.println("onTrackLimitationNotice.(" + arg0 + ")");
			}
        };
        readTweet();
        stream.addListener(listener);
        stream.user();
	}

	public void readTweet(){
	    Paging paging = new Paging(1, 50);
	    ResponseList<Status> statuses;
		try {
			statuses = tw.Twitter.getHomeTimeline(paging);
			for(Status status : statuses){
				gui.receiveTweet(status);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		Object obj = e.getSource();
		if(obj == gui.twButton){
			switch(twInfo.tweetMode){
				case 0://tweet
					String result = tw.tweet(gui.twArea.getText());
					gui.setStatus(result);
					gui.twSetText("");
					break;
				case 1://reply
					String result1 = tw.reply(gui.getTwArea(),twInfo.id);
					gui.setStatus(result1);
					gui.twSetText("");
					twInfo.tweetMode = 0;
					break;
			}
		}
		for(int i = 0;i < gui.arrayTweet.length;i++){
			//リプライ
			if(obj == gui.arrayTweet[i].bReply){
				twInfo.dearSN = gui.arrayTweet[i].getScreenName();
				twInfo.id = gui.arrayTweet[i].getId();
				gui.twSetText("@" + twInfo.dearSN + " ");
				gui.setStatus("reply to:" + gui.arrayTweet[i].getText());
				twInfo.tweetMode = 1;
			}
			//リツイート
			if(obj == gui.arrayTweet[i].bRetweet){
				twInfo.id = new Long(gui.arrayTweet[i].getId());
				if(!gui.arrayTweet[i].chkRTed()){//RT前の場合
					int flag = JOptionPane.showConfirmDialog(gui,gui.arrayTweet[i].getTwPanel(),"Are you sure you want to RT?",
							JOptionPane.YES_NO_OPTION);
					if(flag == JOptionPane.YES_OPTION){
						Status s = tw.retweet(twInfo.id);
						if(s != null){
							gui.setStatus("Retweet was sucessfull");
							gui.arrayTweet[i++].rted(s.getId());
						}else{
							gui.setStatus("Failed to retweet...");
						}
					}
				}else{//RT済みの場合
					int flag = JOptionPane.showConfirmDialog(gui,gui.arrayTweet[i].getTwPanel(),"Are you sure you want to destory RT?",
							JOptionPane.YES_NO_OPTION);
					if(flag == JOptionPane.YES_OPTION){
						boolean f = tw.destroy(gui.arrayTweet[i].getRTedID());//成否を格納
						if(f){
							gui.setStatus("Tweet was destroied");
							gui.arrayTweet[i].destroyRT();
						}else{
							gui.setStatus("Failed to destroy tweet...");
						}
					}
				}
			}
			//ふぁぼ
			if(obj == gui.arrayTweet[i].bLike){
				if(!gui.arrayTweet[i].chkFav()){//未ふぁぼ
					Boolean f = tw.createFavorite(gui.arrayTweet[i].getId());
					if(f){
						gui.setStatus("Liked");
						gui.arrayTweet[i].favorite();
					}else{
						gui.setStatus("Failed to like tweet...");
					}
				}else{//ふぁぼ済
					Boolean f = tw.destroyFavorite(gui.arrayTweet[i].getId());
					if(f){
						gui.setStatus("Unliked");
						gui.arrayTweet[i].destoryFav();
					}else{
						gui.setStatus("Failed to unlike tweet...");
					}
				}
			}
		}
	}
	public static void main(String[] args) throws TwitterException{
		new Main();
	}
}
