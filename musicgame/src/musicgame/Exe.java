package musicgame;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import musicgame.panel.BemaniPanel;
import musicgame.panel.ChallengeModePanel;
import musicgame.panel.ChallengeResult;
import musicgame.panel.KeyConfigPanel;
import musicgame.panel.MainMenuPanel;
import musicgame.panel.MusicSelectPanel;
import musicgame.panel.Result;
import musicgame.panel.TitlePanel;
import musicgame.part.Parts;
import musicgame.score.Score;


public class Exe extends JFrame{
	//実行、パネル切り替えクラス
	private static final String[] PanelNames = {"title","menu","mslect","bemani",
												"result","conf","challenge","load","cresult"};
	private Parts parts;
	private TitlePanel title;
	private MainMenuPanel menu;
	private KeyConfigPanel conf;
	private MusicSelectPanel mselect;
	private ChallengeModePanel cselect;
	private BemaniPanel bemani;
	private JFXPanel jfxPanel;
	private Result result;
	private ChallengeResult cresult;


	private int songNum,hs,laneCoverHeight,challengeLevel;
	private int cntChallenge;
	private String songTitle;
	private ArrayList<String> songList = new ArrayList<>();
	private boolean isChallengemode = false;

	String name,str;

	public Exe(double magnification) {
		parts = new Parts();
		title = new TitlePanel(this, PanelNames[0] ,magnification);
		menu = new MainMenuPanel(this, PanelNames[1],magnification);
		conf = new KeyConfigPanel(this, PanelNames[5],magnification);
		mselect = new MusicSelectPanel(this, PanelNames[2],magnification);
		cselect = new ChallengeModePanel(this, PanelNames[6],magnification);
		bemani = new BemaniPanel(this,PanelNames[3],magnification);
		jfxPanel = new JFXPanel();
		result = new Result(this,magnification);
		cresult = new ChallengeResult(this,PanelNames[8],magnification);
	}

	public void start(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				setResizable(false);
				//setMinimumSize(new Dimension(640,480));
				setTitle("Sound VolTekkun");
				add(title);
				title.startFlashTimer();
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
				pack();
				setLocationRelativeTo(null);
				System.out.println(getSize());
			}
		});
	}


//	public static void main(String[] args) {
//		Platform.setImplicitExit(false);
//		Exe frame = new Exe();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}
	//パネル切り替え処理
	//サンプルから拾ってきたはずなんだけど魔改造してるせいでもうわけわからないので触ったらやばいところ（一番駄目な奴な）
	public void PanelChange(JPanel jp , String str){
		name = jp.getName();
		this.str = str;
		if(str.equals(PanelNames[0])){
			add(title);
			title.fadeIn();
			title.startFlashTimer();
			title.setVisible(true);
			title.requestFocusInWindow();
		}else if(str.equals(PanelNames[1])){
			add(menu);
			menu.fadeIn();
			menu.setVisible(true);
			menu.requestFocusInWindow();
		}else if(str.equals(PanelNames[2])){
			add(mselect);
			mselect.fadeIn();
			mselect.startCursorTimer();
			mselect.setVisible(true);
			mselect.requestFocusInWindow();
		}else if(str.equals(PanelNames[6])){
			add(cselect);
			cselect.fadeIn();
			cselect.initChallengeMode();
			cselect.startCursorTimer();
			cselect.setVisible(true);
			cselect.requestFocusInWindow();
		}else if(str.equals(PanelNames[3])){
			songTitle = mselect.getMusicValue();
			hs = mselect.geths();
			laneCoverHeight = mselect.getLaneCoverHeight();
			isChallengemode = false;
			Score.setFMode();
			add(bemani);
			bemani.initGame(songTitle, hs, laneCoverHeight);
			bemani.fadeIn();
			bemani.setVisible(true);
			bemani.requestFocusInWindow();
		}else if(str.equals(PanelNames[4])){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					System.out.println("platform");
					result.delScore();
					result.setScore(songTitle,Score.getPerfect(),Score.getGreat(),Score.getGood(),Score.getBad(),Score.getMiss(),Score.getMaxCombo(),Score.getScore(),Score.getClear());
					result.readResult();
					jfxPanel.setScene(result.getScene());
				}
			});
			add(jfxPanel);
			jfxPanel.setVisible(true);
			jfxPanel.requestFocusInWindow();
			//チャレンジモードの場合楽曲のスコアを一時保存
			if(isChallengemode){
				Score.setcScore(cntChallenge);
				Score.setcAchievmentRate(cntChallenge);
			}
		}else if(str.equals(PanelNames[5])){
			add(conf);
			conf.setVisible(true);
			conf.fadeIn();
			conf.requestFocusInWindow();
		}

		if(name.equals(PanelNames[0])){
			System.out.println("TITLEREMOVE");
			title.setVisible(false);
			remove(title);
		}else if(name.equals(PanelNames[1])){
			menu.setVisible(false);
			remove(menu);
		}else if(name.equals(PanelNames[2])){
			mselect.setVisible(false);
			remove(mselect);
		}else if(name.equals(PanelNames[6])){
			cselect.setVisible(false);
			remove(cselect);
		}else if(name.equals(PanelNames[3])){
			System.out.println("bemaniremove");
			bemani.setVisible(false);
			remove(bemani);
		}else if(name.equals(PanelNames[5])){
			System.out.println("confremove");
			conf.setVisible(false);
			remove(conf);
		}else if(name.equals(PanelNames[8])){
			cresult.setVisible(false);
			remove(cresult);
		}

	}
	//チャレンジ開始用
	public void PanelChange(JPanel jp , String str,ArrayList<String> songList,int cindex){
		cntChallenge = 0;
		songNum = cindex*3;
		challengeLevel = cindex;
		hs = cselect.geths();
		laneCoverHeight = cselect.getLaneCoverHeight();
		songTitle = songList.get(songNum);
		this.songList = songList;
		isChallengemode = true;
		Score.setCMode(challengeLevel);
		add(bemani);
		bemani.initGame(songTitle, hs, laneCoverHeight);
		bemani.fadeIn();
		bemani.setVisible(true);
		cselect.setVisible(false);
		remove(cselect);
		bemani.requestFocusInWindow();
		System.out.println("NUM:"+songNum);
		songTitle = songList.get(songNum);
		cntChallenge++;
	}

	//scene用
	public void panelChange(){
		//チャレンジモードかつ死亡していないかつ三曲プレイしていないかどうか
		if(isChallengemode && !Score.getDeathDecision() && cntChallenge < 3){
			jfxPanel.setScene(null);
			jfxPanel.setVisible(false);
			remove(jfxPanel);
			songNum++;
			System.out.println("NUM:"+songNum);
			songTitle = songList.get(songNum);
			System.out.println(songTitle);
			add(bemani);
			bemani.initGame(songTitle, hs, laneCoverHeight);
			bemani.setVisible(true);
			bemani.requestFocusInWindow();
			cntChallenge++;
		}else if(isChallengemode){
			if(!Score.getDeathDecision() && Parts.hazardIndex == 1){
				Parts.clearList.set(challengeLevel*5+4, 1);
				parts.saveClearData();
			}else if(!Score.getDeathDecision() && Parts.hazardIndex == 0){
				Parts.clearList.set(challengeLevel*5+Parts.guageIndex, 1);
				parts.saveClearData();
			}
			//なんかこの辺だけリザルトリムーブすると壊れるのでリムーブ無しで
			Score.setEnd();
			add(cresult);
			cresult.initCresult();
			cresult.setVisible(true);
			cresult.requestFocusInWindow();
			jfxPanel.setScene(null);
			jfxPanel.setVisible(false);
			//応急処置何してんのかは知らん
			invalidate();
			validate();
		}else{
			jfxPanel.setVisible(false);
			add(mselect);
			mselect.bulldoze();
			mselect.setVisible(true);
			mselect.startCursorTimer();
			mselect.requestFocusInWindow();
		}
	}

	public String getPanelNames(int i){
		return PanelNames[i];
	}
}
