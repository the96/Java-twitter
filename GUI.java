import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import twitter4j.Status;
public class GUI  extends JFrame implements ComponentListener{
	public
		JButton twButton;
		Tweet[] arrayTweet;
	private
		JPanel twPanel;
		JPanel timeLinePanel;
		JPanel statusPanel;
		JPanel northPanel;
		JTextArea twArea;
		JLabel statusLabel;
		JScrollPane timeLine;
		JScrollPane twScrollPane;
		final static int twHEIGHT = 80;
		final static int AREAWIDTH = twHEIGHT + 30;//フレーム幅から引いた値がtwAreaの幅になる
		final static int BARWIDTH = 20;//フレーム幅から引いた値がstatusBarの幅になる
		final static int BARHEIGHT = 20;
		final static int TLHEIGHT = twHEIGHT + BARHEIGHT + 60;//フレーム高から引いた値がTLの表示領域
		final static int TLPWIDTH = 25;
	GUI(){
		//GUI周りの初期化
		super("TwitterTestClient");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640,480);
		setMinimumSize(new Dimension(320,320));
		addComponentListener(this);

		//上部のツイート入力欄とツイートボタン
		twPanel = new JPanel();
		twArea = new JTextArea();
		twArea.setLineWrap(true);
		twScrollPane = new JScrollPane(twArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		twButton = new JButton("tweet");
		twButton.setPreferredSize(new Dimension(twHEIGHT,twHEIGHT));
		twPanel.setLayout(new FlowLayout());
		twPanel.add(twScrollPane);
		twPanel.add(twButton);

		//中部のステータスラベル
		statusPanel = new JPanel();
		statusLabel = new JLabel("statusBar");
		statusLabel.setPreferredSize(new Dimension(this.getWidth()-BARWIDTH,BARHEIGHT));
		statusLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED,Color.BLACK,Color.GRAY));
		statusLabel.setHorizontalAlignment(JLabel.LEFT);
		statusPanel.add(statusLabel);

		//それらを纏めるnorthPanel
		northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(twPanel,BorderLayout.NORTH);
		northPanel.add(statusPanel,BorderLayout.SOUTH);

		//下段のタイムライン部分
		timeLinePanel = new JPanel();
		timeLine = new JScrollPane(timeLinePanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		timeLine.setPreferredSize(new Dimension(this.getWidth(),this.getHeight() - TLHEIGHT));
		timeLinePanel.setSize(new Dimension(timeLine.getWidth()-TLPWIDTH,20));

		//ぺたり
		getContentPane().add(northPanel,BorderLayout.NORTH);
		getContentPane().add(timeLine,BorderLayout.SOUTH);

		//以下ツイート関連の初期化
		//ツイートのレイアウト
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTH;
		cons.gridx = 0;
		cons.weightx = 1;
		cons.weighty = 0;
		cons.insets = new Insets(1,0,1,0);
		timeLinePanel.setLayout(layout);
		//ツイートの配列を全て初期化
		arrayTweet = new Tweet[50];
		Tweet.width = timeLinePanel.getWidth();
		for(int i = 0;i < arrayTweet.length;i++){
			arrayTweet[i] = new Tweet();
			cons.gridy = i;
			arrayTweet[i].Panel.setBorder(new LineBorder(Color.GRAY, 1, true));
			layout.setConstraints(arrayTweet[i].Panel,cons);
			timeLinePanel.add(arrayTweet[i].Panel);
		}
		allResize();
		setVisible(true);
	}

	public void receiveTweet(Status status){
		for(int i = arrayTweet.length - 2;i >= 0;i--){
			if(arrayTweet[i].chkTweet())continue;
			arrayTweet[i+1].setTweet(arrayTweet[i]);
		}
		arrayTweet[0].setTweet(status);
	}

	public void twSetText(String str){
		twArea.setText(str);
	}
	public String getTwArea(){
		return twArea.getText();
	}

	public void setStatus(String str){
		statusLabel.setText(str);
	}


	//クライアントのすべてのコンポーネントを初期化。
	private void allResize(){
		JScrollBar jsb = timeLine.getVerticalScrollBar();
		int bar = jsb.getValue();
		twScrollPane.setPreferredSize(new Dimension(this.getWidth() - AREAWIDTH , twHEIGHT));
		statusLabel.setPreferredSize(new Dimension(this.getWidth() - BARWIDTH , BARHEIGHT));
		timeLine.setPreferredSize(new Dimension(this.getWidth() , this.getHeight() - TLHEIGHT));
		timeLinePanel.setSize(new Dimension(timeLine.getWidth() - TLPWIDTH , 20));

		Tweet.setWidth(timeLinePanel.getWidth());
		for(int i = 0;i < arrayTweet.length;i++){
			arrayTweet[i].resize();
		}
		jsb.setValue(bar);
	}

	//フレームのサイズが変更されたとき、それに合わせてUIのサイズを変更するためのリスナー。
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		allResize();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
}