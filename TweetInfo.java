public class TweetInfo{//リプライ等の特殊なツイートをするときに使用する情報を持つ
	String dearSN;//送り先のScreenName
	long id;//リプライ宛のツイートID
	int tweetMode;//Default is 0:plain tweet,1:reply to tweet
	TweetInfo(){
		tweetMode = 0;
		id = 0;
		dearSN = "";
	}
}