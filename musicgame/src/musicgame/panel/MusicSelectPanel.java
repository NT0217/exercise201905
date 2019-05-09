package musicgame.panel;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.panel.effect.LoadScreen;
import musicgame.part.Parts;
import musicgame.score.Score;

public class MusicSelectPanel extends JPanel implements KeyListener,Runnable{

	private static final String[] GUAGE_LIST = {"EASY","NORMAL","HARD","EXHARD"};
	private static final String[] HAZARD_BOOL = {"OFF","ON"};
	private static final int WIDTH =640;
	private static final int HEIGHT =480;

	private String str;

	private int cursorX = 10;
	private int cursorY = 110;
	private int OptionY = -480;
	private int oCusrsorY = 146;
	private int oIndex = 0;
	private int optionInfoY = 183;
	private float alphaVal = 1.0f;
	private int x = 35;
	private int y = 47;
	private int y2 = 0;
	private int countScroll = 0;
	private  int x0, y0, x1, y1;
	private double waveX = 60.0;
	private int waveY = 0;
	double test = 1.0;

	private AudioClip scrollSE = Applet.newAudioClip(getClass().getResource("SE/tick2.wav"));


	private ArrayList<String> songLevel = new ArrayList<>();
	private ArrayList<String> songHiscore = new ArrayList<>();

	private int mIndex = 0;
	private double magnification;
	private Exe frame;
	private Thread thread;
	private LoadScreen loadScreen;

	StringBuilder sb = new StringBuilder();

	private Image textureBase;
	private Image textureList;
	private Image cursor;
	private Image oCursor;
	private Image jacket;
	private Image option;


	private AlphaComposite composite;
	private AlphaComposite composite2;
	private Timer timer;
	private Timer optionTimer;

	private boolean isUppress = false;
	private boolean isDownpress = false;
	private boolean isEnterpress = false;
	private boolean isESCpress = false;
	private boolean isFlashdecision = false;
	private boolean isPlayOptionVisible = false;
	private boolean isActive = false;
	private boolean isMovement = false;
	private boolean isScroll = false;
	private boolean isSwitch = false;

	public MusicSelectPanel(Exe frame, String str ,double magnification){
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		this.frame = frame;
		this.str = str;

		this.setName(str);
		this.setLayout(null);
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		composite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		loadImage();
		loadMusicJucket();
		loadHiscore();
		loadLevel();
		timer = new Timer();
		for(int i=0; i<songHiscore.size(); i++){
			System.out.println("ハイスコア"+songHiscore.get(i));
		}
	}

	public void fadeIn(){
		thread = new Thread(this);
		loadScreen = new LoadScreen(true);
		isActive = true;
		isMovement = true;
		thread.start();
	}

	public void bulldoze(){
		loadHiscore();
		thread = new Thread(this);
		isActive = true;
		thread.start();
	}

	@Override
	public void run() {
		while(isActive){
			if(!isSwitch){
				waveY+=1;
				if(waveY == 60){
					isSwitch = true;
				}
			}else{
				waveY-=1;
				if(waveY == -60){
					isSwitch = false;
				}
			}
			waveX+=test;
			if(waveX == 180.0 || waveX == 60.0){
				test = -test;
			}
			scrollMusicLabel();
			repaint();
			try{
				Thread.sleep(16);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void scrollMusicLabel(){
		if(isScroll) {
			if(isUppress) {
				y2 += 5;
				countScroll++;
			}
			if(isDownpress) {
				y2 -= 5;
				countScroll++;
			}
			if(countScroll == 10) {
				isScroll = false;
				countScroll = 0;
				y2 = 0;
				if(isUppress) {
					if(mIndex == 0) {
						mIndex = Parts.songList.length-1;
					}else {
						mIndex -=1;
					}
				}
				if(isDownpress) {
					if(mIndex == Parts.songList.length-1) {
						mIndex = 0;
					}else {
						mIndex +=1;
					}
				}
				isUppress = false;
				isDownpress = false;
				loadMusicJucket();
				System.out.println(mIndex);
				System.out.println(songHiscore.get(mIndex));
			}
		}
	}

	private void loadMusicJucket(){
		ImageIcon icon ;
		String path = "Song/"+Parts.songList[mIndex]+"/"+Parts.songList[mIndex]+".jpg";
		try{
			icon = new ImageIcon(getClass().getResource(path));
			jacket = icon.getImage();
		}catch (NullPointerException e) {
			path = "Image/cat.jpg";
			icon = new ImageIcon(getClass().getResource(path));
			jacket = icon.getImage();
		}

	}

	private void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/musicselect-base.png"));
		textureBase = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Texture/musicselect-list.png"));
		textureList = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/mCursor.png"));
		cursor = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Texture/playoption.png"));
		option = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/optionCursor.png"));
		oCursor = icon.getImage();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setFont(Parts.poiretFont);
		g2.drawImage(textureList, 0, 0, null);
		drawMusicLabel(g2);
		g2.drawImage(textureBase, 0, 0, null);
//		drawSinCurve(g2);
		drawLevel(g2);
		drawHiscore(g2);
		drawJacket(g2);
		drawCursor(g2);
		drawOption(g2);
		if(loadScreen != null){
			loadScreen.draw(g);
			if(loadScreen.getFinishDecision()){
				if(!loadScreen.getFademode()){
					if(isEnterpress){
						isEnterpress = false;
						pc(frame.getPanelNames(3));
					}
					if(isESCpress){
						isESCpress = false;
						pc(frame.getPanelNames(1));
					}
				}else{
					isMovement = false;
					loadScreen = null;
				}
			}
		}
	}

	private void drawMusicLabel(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		if(!isScroll) {
			y = -3;
			for(int i = mIndex-3; i<mIndex+4; i++){
				g2.drawString(Parts.songList[Parts.getSongList(i)], x, y);
				y += 50;
			}
		}else {
			y = -3 + y2;
			for(int i = mIndex-3; i<mIndex+4; i++){
				g2.drawString(Parts.songList[Parts.getSongList(i)], x, y);
				y += 50;
			}
		}
	}

//	private void drawSinCurve(Graphics g){
//		Graphics2D g2 = (Graphics2D)g;
//		x1 = -20; y1 = 300;
//		for (int i = 1; i <= 100; i++){
//			x0 = x1; y0 = y1;
//			x1 = -20+(int)(i/20.0*200);
//			y1 = 300-(int)(waveY*Math.sin(i/waveX*2*Math.PI));
//			g2.drawLine(x0, y0, x1, y1);
//		}
//	}

	private void drawLevel(Graphics g){
		Font font = new Font(Font.SERIF, Font.PLAIN, 40);
		g.setFont(font);
		g.setColor(Color.RED);
		try{
			g.drawString(songLevel.get(mIndex), 10, 442);
			g.setColor(Color.BLACK);
		}catch (IndexOutOfBoundsException e) {
			g.drawString("Undefined", 10, 442);
			g.setColor(Color.BLACK);
		}

	}
	private void drawHiscore(Graphics g){
		g.setFont(Parts.codeFont);
		g.drawString(String.format("%07d", Integer.parseInt(songHiscore.get(mIndex))), 441, 299);
	}

	private void drawJacket(Graphics g){
		g.drawImage(jacket,461 ,46 , null);
	}

	private void drawCursor(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(composite);
		g2.drawImage(cursor, cursorX, cursorY, this);
	}

	private void drawOption(Graphics g){
		//472
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(composite2);
		g2.drawImage(option, 0, OptionY, null);
		g2.drawImage(oCursor, 372, OptionY+oCusrsorY, null);
		String text;
		g2.setFont(Parts.poiretFont);
		FontMetrics fm = g.getFontMetrics();
		for(int i=0; i<Parts.optionList.length; i++){
			text = Parts.optionList[i];
			Rectangle rectText = fm.getStringBounds(text, g).getBounds();
			g.drawString(text, 472-rectText.width/2, OptionY+optionInfoY+50*i);
		}
	}

	public void loadLevel(){
		int level;
		//難易度読み込み
		File dif = new File(getClass().getResource("DATA/difficulty.lst").getPath());
		try{
			FileReader fileReader = new FileReader(dif);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			//行ごと
			while((line = br.readLine()) !=null){
				StringBuilder sb = new StringBuilder();
				System.out.println("LEVEL:"+line);
				// = で分割
				String[] data = line.split(" = ",0);
				level = Integer.parseInt(data[1]);
				for(int j = 0; j<level; j++){
					sb.append("★");
				}
				songLevel.add(sb.toString());
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}



	public void loadHiscore(){
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher;
		File dif = new File(getClass().getResource("DATA/score.xml").getPath());
		try{
			FileReader fileReader = new FileReader(dif);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			//行ごと
			while(songHiscore.size() <= Parts.songList.length){
				songHiscore.add("0000000");
			}
			while((line = br.readLine()) !=null){
				String str;
				line = rePlaceStr(line);
				System.out.println("LINE:"+line);
				for(int i = 0; i<Parts.songList.length; i++){
					if(line.contains(Parts.songList[i])){
						matcher = pattern.matcher(line);
						str = matcher.replaceAll("");
						songHiscore.set(i,str);
						break;
					}
				}
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static String rePlaceStr(String str) {
		System.out.println("REPLACE");
		str = str.replaceAll("_s_", " ").replaceAll("_q_", "\\?").replaceAll("_e_", "!").replaceAll("_a_", "'");
		return str;
	}

	private void savePlayOption(){
		File keyconf = new File(getClass().getResource("DATA/playoption.ini").getPath());
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(keyconf)));
			pw.println("Guage="+Parts.optionList[0]);
			pw.println("HiSpeed="+Parts.optionList[1]);
			pw.println("LaneCoverHeight="+Parts.optionList[2]);
			pw.println("Hazard="+Parts.optionList[3]);
			pw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void pc(String str){
		System.out.println("GUAGE:"+Parts.guageIndex);
		timer.cancel();
		timer = new Timer();
		loadScreen = null;
		thread = null;
		isActive = false;
		isMovement = false;
		savePlayOption();
		Score.setDifficulty(Parts.guageIndex);
		frame.PanelChange((JPanel)this, str);
	}

	public int getMusicNum(){
		return mIndex;
	}

	public int geths(){
		return Parts.hispeed;
	}
	public int getLaneCoverHeight(){
		return Parts.laneCoverHeight;
	}

	public String getMusicValue(){
		String s = Parts.songList[mIndex];
		System.out.println("SONG:"+s);
		System.out.println("INDEX:"+mIndex);
		return s;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		int mod = e.getModifiersEx();
		if(!isMovement){
			if(!isScroll){
				if(mod == KeyEvent.SHIFT_DOWN_MASK){
				}else{
					if(key == KeyEvent.VK_UP){
						if(!isUppress){
							scrollSE.stop();
							scrollSE.play();
							isUppress = true;
							isScroll = true;
						}
					}
					if(key == KeyEvent.VK_DOWN){
						if(!isDownpress){
							scrollSE.stop();
							scrollSE.play();
							isDownpress = true;
							isScroll = true;
						}
					}
					if(key == KeyEvent.VK_ENTER){
						if(!isEnterpress){
							isEnterpress = true;
							isMovement = true;
							System.out.println("enter");
							loadScreen = new LoadScreen(false);
						}
					}
					if(key == KeyEvent.VK_ESCAPE){
						if(!isESCpress){
							isESCpress = true;
							isMovement = true;
							loadScreen = new LoadScreen(false);
						}
					}
				}
				if(key == KeyEvent.VK_SHIFT){
					optionInfoY = 183;
					if(!isPlayOptionVisible && optionTimer == null){
						isPlayOptionVisible = true;
						optionTimer = new Timer();
						optionTimer.schedule(new OptionVisible(),0,16);
					}
				}

				if(mod == KeyEvent.SHIFT_DOWN_MASK && key == KeyEvent.VK_UP){
					System.out.println("↑");
					if(oIndex != 0){
						oIndex -= 1;
						oCusrsorY -= 50;
					}
				}
				if(mod == KeyEvent.SHIFT_DOWN_MASK && key == KeyEvent.VK_DOWN){
					System.out.println("下");
					if(oIndex != 3){
						oIndex += 1;
						oCusrsorY += 50;
					}
				}
				if(mod == KeyEvent.SHIFT_DOWN_MASK && key == KeyEvent.VK_LEFT){
					switch (oIndex) {
					case 0:
						if(Parts.guageIndex == 0){
							Parts.guageIndex = GUAGE_LIST.length-1;
							Parts.optionList[oIndex]= GUAGE_LIST[Parts.guageIndex];
						}else{
							Parts.guageIndex -=1;
							Parts.optionList[oIndex]= GUAGE_LIST[Parts.guageIndex];
						}
						break;
					case 1:
						Parts.hispeed -= 1;
						Parts.optionList[oIndex]= String.valueOf(Parts.hispeed);
						break;
					case 2:
						Parts.laneCoverHeight -= 10;
						Parts.optionList[oIndex]= String.valueOf(Parts.laneCoverHeight);
						break;
					case 3:
						if(Parts.hazardIndex == 0){
							Parts.hazardIndex = HAZARD_BOOL.length-1;
							Parts.optionList[oIndex]= HAZARD_BOOL[Parts.hazardIndex];
						}else{
							Parts.hazardIndex -=1;
							Parts.optionList[oIndex]= HAZARD_BOOL[Parts.hazardIndex];
						}
						break;
					default:
						break;
					}
				}
				if(mod == KeyEvent.SHIFT_DOWN_MASK && key == KeyEvent.VK_RIGHT){
					switch (oIndex) {
					case 0:
						if(Parts.guageIndex == GUAGE_LIST.length-1){
							Parts.guageIndex = 0;
							Parts.optionList[oIndex]= GUAGE_LIST[Parts.guageIndex];
						}else{
							Parts.guageIndex +=1;
							Parts.optionList[oIndex]= GUAGE_LIST[Parts.guageIndex];
						}
						break;
					case 1:
						Parts.hispeed += 1;
						Parts.optionList[oIndex]= String.valueOf(Parts.hispeed);
						break;
					case 2:
						Parts.laneCoverHeight += 10;
						Parts.optionList[oIndex]= String.valueOf(Parts.laneCoverHeight);
						break;
					case 3:
						if(Parts.hazardIndex ==  HAZARD_BOOL.length-1){
							Parts.hazardIndex = 0;
							Parts.optionList[oIndex]= HAZARD_BOOL[Parts.hazardIndex];
						}else{
							Parts.hazardIndex +=1;
							Parts.optionList[oIndex]= HAZARD_BOOL[Parts.hazardIndex];
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_SHIFT){
			while(isPlayOptionVisible){
				isPlayOptionVisible = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		sb.append(e.getKeyChar());
		if(sb.length() >= 4){
			if(sb.equals("auto")){
				System.out.println("OK");
			}
			System.out.println(sb);
			sb.delete(0, 4);
		}
		System.out.println(sb);
	}

	class OptionVisible extends TimerTask{
		@Override
		public void run() {
			if(isPlayOptionVisible  && OptionY != 0){
				OptionY += 20;
			}else if(!isPlayOptionVisible ){
				OptionY -= 20;
				if(OptionY == -480){
					optionTimer.cancel();
					optionTimer = null;
				}
			}
		}
	}

	public void startCursorTimer(){
		timer.schedule(new FlashingCursor(), 0, 100);
	}
	public class FlashingCursor extends TimerTask{
		@Override
		public void run() {
			if(!isFlashdecision){
				alphaVal -=0.10f;
				if(alphaVal <=0.1f){
					isFlashdecision = true;
				}
			}else{
				alphaVal +=0.10f;
				if(alphaVal >=0.9f){
					isFlashdecision = false;
				}
			}
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		}
	}
}
