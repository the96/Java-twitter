import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import twitter4j.Status;

//１つのツイートをこのようなオブジェクト単位で扱う。
public class Tweet{
	public JButton bReply;
	public JButton bRetweet;
	public JButton bLike;
	public JPanel Panel;
	public static int width;
	private	JLabel nameLabel;
	private JTextArea tweetArea;
	private JPanel bPanel;
	private String screenName;
	private String name;
	private String text;
	private long id;
	private boolean rted;
	private long rtedid;//rtで投稿した自分のツイートのid
	private boolean favo;//favoriteのチェック
	final private ImageIcon ICON_REPLY = new ImageIcon(getClass().getResource("./reply.png"));
	final private ImageIcon ICON_RETWEET = new ImageIcon(getClass().getResource("./retweet.png"));
	final private ImageIcon ICON_RETWEETED = new ImageIcon(getClass().getResource("./retweeted.png"));
	final private ImageIcon ICON_LIKE = new ImageIcon(getClass().getResource("./like.png"));
	final private ImageIcon ICON_LIKED = new ImageIcon(getClass().getResource("./liked.png"));
	Tweet(){
		nameLabel = new JLabel();
		tweetArea = new JTextArea();
		Panel = new JPanel();
		bPanel = new JPanel();
		bReply = new JButton(ICON_REPLY);
		bRetweet = new JButton(ICON_RETWEET);
		bLike = new JButton(ICON_LIKE);
		id = -1;
		rted = false;
		favo = false;
		//左詰め
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		//ツイート本文を表示するエリアの設定
		tweetArea.setLineWrap(true);
		tweetArea.setEditable(false);

		//各ボタンの設定
		Dimension dReply = new Dimension(ICON_REPLY.getIconWidth(),ICON_REPLY.getIconHeight());
		bReply.setContentAreaFilled(false);
		bReply.setBorderPainted(false);
		bReply.setPreferredSize(dReply);
		bReply.setFocusPainted(false);

		Dimension dRetweet = new Dimension(ICON_RETWEET.getIconWidth(),ICON_RETWEET.getIconHeight());
		bRetweet.setContentAreaFilled(false);
		bRetweet.setBorderPainted(false);
		bRetweet.setPreferredSize(dRetweet);
		bRetweet.setFocusPainted(false);

		Dimension dLike = new Dimension(ICON_LIKE.getIconWidth(),ICON_LIKE.getIconHeight());
		bLike.setContentAreaFilled(false);
		bLike.setBorderPainted(false);
		bLike.setPreferredSize(dLike);
		bLike.setFocusPainted(false);

		bPanel.setLayout(new FlowLayout());
		bPanel.add(bReply);
		bPanel.add(bRetweet);
		bPanel.add(bLike);

		//Layoutの設定
		GridBagLayout pLayout = new GridBagLayout();
		GridBagConstraints pCons = new GridBagConstraints();
		Panel.setLayout(pLayout);
		pCons.gridx = 0;
		pCons.anchor = GridBagConstraints.NORTHWEST;
		pCons.gridy = 0;
		pLayout.setConstraints(nameLabel,pCons);
		Panel.add(nameLabel);

		pCons.gridx = 0;
		pCons.gridy++;
		pLayout.setConstraints(tweetArea,pCons);
		Panel.add(tweetArea);

		pCons.gridx = 0;
		pCons.gridy++;
		pLayout.setConstraints(bPanel,pCons);
		Panel.add(bPanel);
		resize();
		screenName = "";
		name = "";
		text = "";
	}

	static void setWidth(int width){
		Tweet.width = width;
	}

	void setTweet(Status status){
		name = status.getUser().getName();
		screenName = status.getUser().getScreenName();
		text = status.getText();
		id = status.getId();
		rted = false;
		favo = false;
		resetRTIcon(rted);
		resetFavIcon(favo);
		setText();
	}

	//受け取ったツイートオブジェクトに変更する
	void setTweet(Tweet tweet){
		name = tweet.name;
		screenName = tweet.screenName;
		text = tweet.text;
		id = tweet.id;
		rted = tweet.rted;
		rtedid = tweet.rtedid;
		favo = tweet.favo;
		resetRTIcon(rted);
		resetFavIcon(favo);
		setText();
	}

	//RTしたときに呼び出される。
	void rted(long rtid){
		rted = true;
		resetRTIcon(rted);
		rtedid = rtid;
	}

	//RTを取り消した際に呼び出される。
	void destroyRT(){
		rtedid = -1;
		rted = false;
		resetRTIcon(rted);
	}

	//RTボタンのアイコンをrtedに合わせて変更する。
	void resetRTIcon(boolean rted){
		if(rted){
			bRetweet.setIcon(ICON_RETWEETED);
		}else{
			bRetweet.setIcon(ICON_RETWEET);
		}
	}

	void favorite(){
		favo = true;
		resetFavIcon(favo);
	}

	void destoryFav(){
		favo = false;
		resetFavIcon(favo);
	}

	void resetFavIcon(boolean favo){
		if(favo){
			bLike.setIcon(ICON_LIKED);
		}else{
			bLike.setIcon(ICON_LIKE);
		}
	}

	//リサイズする際に呼び出される。
	void resize(){
		nameLabel.setSize(new Dimension(Tweet.width,10));
		tweetArea.setSize(new Dimension(Tweet.width,20));
		bPanel.setSize(new Dimension(Tweet.width,10));
		Panel.setSize(new Dimension(Tweet.width,40));
	}

	//ツイートをセットする
	void setText(){
		nameLabel.setText(name + "(" + screenName + ")");
		tweetArea.setText(text);
		resize();
	}

	//ツイートが格納されているかを確認する
	boolean chkTweet(){
		return text.isEmpty() || "".equals(text);
	}

	//以下ゲッターメソッド
	String getScreenName(){
		return screenName;
	}

	long getId(){
		return id;
	}

	boolean chkRTed(){
		return rted;
	}

	long getRTedID(){
		return rtedid;
	}

	boolean chkFav(){
		return favo;
	}

	JPanel getTwPanel(){
		JPanel jp = new JPanel();
		jp.setSize(new Dimension(Tweet.width,30));
		jp.add(new JLabel(nameLabel.getText()));
		jp.add(new JTextArea(tweetArea.getText()));
		return jp;
	}

	String getText(){
		return this.text;
	}
}

