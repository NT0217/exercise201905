package musicgame.panel;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.music.MusicDisplay;
import musicgame.music.PlayMusic;
import musicgame.panel.effect.Closed;
import musicgame.panel.effect.ExplosionDisp;
import musicgame.panel.effect.LaneObject;
import musicgame.panel.effect.LaserDisp;
import musicgame.panel.effect.LoadScreen;
import musicgame.part.Parts;
import musicgame.score.Judge;
import musicgame.score.Score;

public class BemaniPanel extends JPanel implements Runnable,KeyListener{


	private static final int LANE_NUM = 4;
	private static final int WIDTH =640;
	private static final int HEIGHT =480;
	private static final int[] BOMB_POS = {193,258,323,388};
	private static final int LANE_COVER_X = 199;
	/* 燃え上がるやつ
	private static final int CH_NUM = 119;
	private static final int PC_NUM = 100;
	private static final int NOTE_NUM = 85;
	*/
	/*　ハイハット
	private static final int CH_NUM = 119;
	private static final int PC_NUM = 36;
	private static final int NOTE_NUM = 65;
	*/

	//リフレク
	private static final int CH_NUM = 10;	//楽器
	private static final int PC_NUM = 36;
	private static final int NOTE_NUM = 42;


	protected LaneObject[] laneobj = new LaneObject[LANE_NUM];

	protected ArrayList<Long> NoteCreateTimeimg;
	protected ArrayList<Long> lane1Timinglist;
	protected ArrayList<Long> lane2Timinglist;
	protected ArrayList<Long> lane3Timinglist;
	protected ArrayList<Long> lane4Timinglist;

	private long pressTime[] = {0,0,0,0};
	private long endPointTime = 0;
	private int timingAdjust = 0;


	private int hantei;
	private static int[] noteCount = new int[LANE_NUM];
	private int[] throughCount = {0,0,0,0};
	private int[] num = {0,0,0,0};
	protected int cntNotes = 0;
	private int hs;
	private double magnification;

	private String humen;
	private Timer timer;
	private Timer startTimer;

	private Image gameFrame;
	private Image laneCover;
	private Image ready;

	private int laneCoverY = 0;

	private String[] keyconfChar = new String[LANE_NUM];

	private boolean is1Lanepress = false;
	private boolean is2Lanepress = false;
	private boolean is3Lanepress = false;
	private boolean is4Lanepress = false;
	private boolean isEscapepress = false;
	private boolean isUppress = false;
	private boolean isDownpress = false;

	private boolean isActive = false;
	private boolean isStart = false;
	private int GORIOSHI = 0;

	private Exe frame;
	private String str;
	private Judge judge;
	private LaserDisp[] ld = new LaserDisp[LANE_NUM];
	private ExplosionDisp[] ed = new ExplosionDisp[LANE_NUM];
	private Closed closed;
	private MusicDisplay md;

	private LoadScreen loadScreen;

	private AlphaComposite composite;

	private float alphaVal;

	private static Thread gameloop;

	public BemaniPanel(Exe frame,String str ,double magnification) {
		this.frame = frame;
		this.str = str;
	}

	public void initGame(String humen, int hs,int laneheight ){
		//譜面のパス取得
		this.humen = humen;
		//レーンカバーとハイスピの設定読み込み
		this.laneCoverY = laneheight;
		this.hs = hs;
		this.setName(str);
		//もろもろ初期化
		for(int i=0; i<noteCount.length; i++){
			noteCount[i] = 0;
			throughCount[i] = 0;
			num[i] = 0;
			cntNotes = 0;
		}
		//譜面関連のリスト初期化
		NoteCreateTimeimg = new ArrayList<Long>();
		lane1Timinglist = new ArrayList<Long>();
		lane2Timinglist = new ArrayList<Long>();
		lane3Timinglist = new ArrayList<Long>();
		lane4Timinglist = new ArrayList<Long>();
		GORIOSHI = 0;
		//譜面読み込み
		LoadNotes();
		//判定クラスにハイスピ情報とプレイする曲の総ノーツ数を渡す
		judge = new Judge(hs,(noteCount[0]+noteCount[1]+noteCount[2]+noteCount[3]));
		window();
		loadImage();
		loadKeyConfig();
		//y座標0から判定ライン到達までの時間を取得
		endPointTime = (long)judge.getJtime();
		//レーザー初期化
		for(int i=0; i<LANE_NUM; i++){
			ld[i] = new LaserDisp();
		}
		md = new MusicDisplay();
		//play(大嘘)音源のパス渡してるだけ
		md.play(humen);

		isActive = true;
		gameloop = new Thread(this);
		gameloop.start();

		alphaVal = 1.0f;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		startTimer = new Timer();
		startTimer.schedule(new GameStartTimer(),0,100);
	}

	public void window(){
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		addKeyListener(this);
		setLayout(null);
		//各レーンごとのノーツ数渡す
		for(int i=0; i<laneobj.length; i++){
			laneobj[i] = new LaneObject(i,noteCount[i],hs);
			laneobj[i].setXpoint(199+65*i);
		}
		//初期化
		for(int i=0; i<noteCount.length; i++){
			noteCount[i] =0;
		}
	}

	public void fadeIn(){
		loadScreen = new LoadScreen(true);
	}

	class GameStartTimer extends TimerTask{
		BigDecimal bd;
		@Override
		public void run() {
			alphaVal -= 0.02f;
			bd = new BigDecimal(alphaVal);
			bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
			alphaVal = bd.floatValue();
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
			if(alphaVal == 0.0f){
				isStart = true;
				startTimer.cancel();
				//曲再生タイマー
				new Timer().schedule(new PlayMusic(),endPointTime-timingAdjust);
				timer = new Timer();
				for(int i=0; i<NoteCreateTimeimg.size(); i++){
					//ノーツ生成タイマー
					timer.schedule(new AddNotes(),NoteCreateTimeimg.get(i));
				}
			}
		}
	}
	@Override
	public void run() {
		while(isActive){
			//ノーツ動かす
			for(int i=0; i<laneobj.length; i++){
				laneobj[i].fall(noteCount[i]);
			}
			//スルー判定
			for(int i=0; i<LANE_NUM; i++){
				if(noteCount[i] == throughCount[i]){
					if(laneobj[i].getYpoint(throughCount[i])> HEIGHT+10*5){
						judge.loadImage(4);
						judge.calcScore(4);
						judge.setPos(199, 275);
						noteCount[i]++;
						throughCount[i]++;
					}
				}else{
					throughCount[i]++;
				}
			}
			//死亡判定
			if(Score.getDeathDecision() || (Parts.hazardIndex == 1 && judge.getComboCut())){
				if(GORIOSHI != -1){
					GORIOSHI = -1;
					closed = new Closed();
					md.stop();
					new Timer().schedule(new End(), 5000);
				}
			}
			repaint();
			try{
				Thread.sleep(16);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//背景黒く
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 199, 480);
		g.fillRect(442, 0, 640, 480);
		//HPバー
		g.setColor(Color.WHITE);
		g.fillRect(470, 356-judge.getHpgauge(), 30, judge.getHpgauge());
		//フレーム
		g.drawImage(gameFrame, 0, 0, null);
		for(int i=0; i<laneobj.length; i++){
			laneobj[i].draw(g);
		}
		//レーザー描画
		judge.draw(g);
		for(int i =0; i<LANE_NUM; i++){
			ld[i].draw(g);
		}
		//ボム描画
		for(int i=0; i<LANE_NUM; i++){
			if(ed[i] != null){
				ed[i].draw(g);;
			}
		}
		drawScoreInfo(g);
		drawCombo(g);
		drawLaneCover(g);

		//Ready
		if(!isStart){
			drawReady(g);
		}
		//閉店描画処理
		if(closed != null){
			closed.draw(g);
		}
		//画面遷移
		if(loadScreen != null){
			loadScreen.draw(g);
			if(loadScreen.getFinishDecision()){
				loadScreen = null;
			}
		}
	}

	public void drawReady(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(composite);
		g2.drawImage(ready, 199, 190, null);
	}

	//MAXCOMBOとSCORE描画
	public void drawScoreInfo(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(Parts.poiretFont2);
		String text =String.format("%07d", Score.getScore());
		FontMetrics fm = g.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g).getBounds();
		g.drawString(text, 91-rectText.width/2, 96);
		text =String.format("%04d", Score.getMaxCombo());
		rectText = fm.getStringBounds(text, g).getBounds();
		g.drawString(text, 91-rectText.width/2, 180);
	}
	//判定の↓にあるやつ
	public void drawCombo(Graphics g ){
		g.setColor(Color.BLACK);
		String text =String.format("%04d", Score.getCombo());
		FontMetrics fm = g.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g).getBounds();
		g.drawString(text, WIDTH/2-rectText.width/2, 350);
	}

	public void drawLaneCover(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(Parts.RictyFont);
		g.drawImage(laneCover, LANE_COVER_X, -480+laneCoverY, null);
		g.drawString(String.format("%03d", laneCoverY), 298, laneCoverY-4);
	}

	//判定
	public void timingCheck(int i){
		pressTime[i] = System.currentTimeMillis();
		if(pressTime[i]-laneobj[i].getObjTime(noteCount[i]) == pressTime[i]){
		}else{
			hantei = judge.decision(pressTime[i]-laneobj[i].getObjTime(noteCount[i]));
			switch(hantei){
				case 0:
					judge.loadImage(hantei);
					judge.setPos(199, 275);
					laneobj[i].setYpoint(noteCount[i], 500);
					noteCount[i]++;
					ed[i] = new ExplosionDisp(BOMB_POS[i], 420);
					break;
				case 1:
					judge.loadImage(hantei);
					judge.setPos(199, 275);
					laneobj[i].setYpoint(noteCount[i], 500);
					noteCount[i]++;
					ed[i] = new ExplosionDisp(BOMB_POS[i], 420);
					break;
				case 2:
					judge.loadImage(hantei);
					judge.setPos(199, 275);
					laneobj[i].setYpoint(noteCount[i], 500);
					noteCount[i]++;
					ed[i] = new ExplosionDisp(BOMB_POS[i], 420);
					break;
				case 3:
					judge.loadImage(hantei);
					judge.setPos(199, 275);
					laneobj[i].setYpoint(noteCount[i], 500);
					noteCount[i]++;
					break;
				default:
					break;
			}
		}
	}

	private void loadKeyConfig(){
		File keyconf = new File(getClass().getResource("DATA/keyconfig.ini").getPath());
		try{
			FileReader fileReader = new FileReader(keyconf);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			int i = 0;
			while((line = br.readLine()) !=null){
				String[] data = line.split("=",0);
				keyconfChar[i] = data[1];
				i++;
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void loadImage(){
		System.out.println("IMAGE");
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/frame.png"));
		gameFrame = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Texture/lanecover.png"));
		laneCover = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/ready.png"));
		ready = icon.getImage();
	}

	public void pc(String str){
		isActive = false;
		isStart =false;
		gameloop = null;
		timer.cancel();
		md = null;
		closed = null;
		loadScreen = null;
		frame.PanelChange((JPanel)this, str);
	}

	//ノーツ生成処理
	class AddNotes extends TimerTask{
		public void run(){
			if(lane1Timinglist.get(cntNotes) == -1){
				if(GORIOSHI != -1){
					md.stop();
					//なんだこの二度手間
					GORIOSHI = -1;
					new Timer().schedule(new End(), 5000);
				}
			}else{
				if(lane1Timinglist.get(cntNotes) == 1){
					laneobj[0].addObj(num[0]);
					num[0]++;
				}
				if(lane2Timinglist.get(cntNotes) == 1){
					laneobj[1].addObj(num[1]);
					num[1]++;
				}
				if(lane3Timinglist.get(cntNotes) == 1){
					laneobj[2].addObj(num[2]);
					num[2]++;
				}
				if(lane4Timinglist.get(cntNotes) == 1){
					laneobj[3].addObj(num[3]);
					num[3]++;
				}
			}
			cntNotes++;
		}
	}
	//閉店
	class End extends TimerTask{
		public void run(){
			pc(frame.getPanelNames(4));
		}
	}
	//ノーツ生成時間、位置読み込み
	public void LoadNotes(){
		File noteFile;
		noteFile = new File(getClass().getResource("Song").getPath()+"/"+humen+"/"+humen+".csv");
		try{
			FileReader fileReader = new FileReader(noteFile);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			while((line = br.readLine()) !=null){
				String[] data = line.split(",",0);
				for(int i=0; i<data.length; i++){
					switch (i) {
						case 0:
							NoteCreateTimeimg.add(Long.parseLong(data[i]));
							break;
						case 1:
							lane1Timinglist.add(Long.parseLong(data[i]));
							if(Integer.parseInt(data[i]) == 1){
								noteCount[i-1]++;
							}
							break;
						case 2:
							lane2Timinglist.add(Long.parseLong(data[i]));
							if(Integer.parseInt(data[i]) == 1){
								noteCount[i-1]++;
							}
							break;
						case 3:
							lane3Timinglist.add(Long.parseLong(data[i]));
							if(Integer.parseInt(data[i]) == 1){
								noteCount[i-1]++;
							}
							break;
						case 4:
							lane4Timinglist.add(Long.parseLong(data[i]));
							if(Integer.parseInt(data[i]) == 1){
								noteCount[i-1]++;
							}
							break;
						default:
							break;
					}
				}
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	public void keyPressed(KeyEvent e) {
		int ekey = e.getKeyCode();
		String key = String.valueOf(e.getKeyChar());
		if(keyconfChar[0].equals(key)){
			if(!is1Lanepress){
				is1Lanepress = true;
				ld[0].setPos(199, 192);
				timingCheck(0);
			}
		}
		if(keyconfChar[1].equals(key)){
			if(!is2Lanepress){
				is2Lanepress = true;
				ld[1].setPos(264, 192);
				timingCheck(1);
			}
		}
		if(keyconfChar[2].equals(key)){
			if(!is3Lanepress){
				is3Lanepress = true;
				ld[2].setPos(329, 192);
				timingCheck(2);
			}
		}
		if(keyconfChar[3].equals(key)){
			if(!is4Lanepress){
				is4Lanepress = true;
				ld[3].setPos(394, 192);
				timingCheck(3);
			}
		}
		//強制脱出用
		if(ekey == KeyEvent.VK_ESCAPE){
			if(!isEscapepress && GORIOSHI != -1 && isStart){
				isEscapepress = true;
//				closed = new Closed();
//				md.stop();
//				new Timer().schedule(new End(), 5000);
//				GORIOSHI = -1;
				Score.setHpDeath();
				isEscapepress = false;
			}
		}
		//レーンカバー移動用
		if(ekey == KeyEvent.VK_UP){
			if(!isUppress){
				isUppress = true;
				System.out.println("up");
				if(laneCoverY != 0){
					System.out.println("UP");
					laneCoverY -= 10;
				}
			}
		}
		if(ekey == KeyEvent.VK_DOWN){
			if(!isDownpress){
				isDownpress = true;
				System.out.println("down");
				if(laneCoverY != 480){
					System.out.println("DOWN");
					laneCoverY += 10;
				}
			}
		}
		System.out.println(e.getKeyCode());
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int ekey = e.getKeyCode();
		String key = String.valueOf(e.getKeyChar());
		if(keyconfChar[0].equals(key)){
			is1Lanepress = false;
			ld[0].store();
		}
		if(keyconfChar[1].equals(key)){
			is2Lanepress = false;
			ld[1].store();
		}
		if(keyconfChar[2].equals(key)){
			is3Lanepress = false;
			ld[2].store();
		}
		if(keyconfChar[3].equals(key)){
			is4Lanepress = false;
			ld[3].store();
		}
		if(ekey == KeyEvent.VK_UP){
			if(isUppress){
				isUppress = false;
			}
		}
		if(ekey == KeyEvent.VK_DOWN){
			if(isDownpress){
				isDownpress = false;
			}
		}
	}
}