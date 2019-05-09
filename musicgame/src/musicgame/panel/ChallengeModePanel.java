package musicgame.panel;

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

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.panel.MusicSelectPanel.OptionVisible;
import musicgame.panel.effect.LoadScreen;
import musicgame.part.Parts;
import musicgame.score.Score;


public class ChallengeModePanel extends JPanel implements KeyListener,Runnable{
	private static final String[] GUAGE_LIST = {"EASY","NORMAL","HARD","EXHARD"};
	private static final String[] HAZARD_BOOL = {"OFF","ON"};
	private static final int WIDTH =640;
	private static final int HEIGHT =480;

	private String str;

	private int cursorX = 9;
	private int cursorY = 10;
	private int OptionY = -480;
	private int oCusrsorY = 146;
	private int oIndex = 0;
	private int listIndex = 0;
	private int optionInfoY = 183;
	private float alphaVal = 1.0f;

	private ArrayList<String> levelList = new ArrayList<>();
	private ArrayList<String> songList = new ArrayList<>();


	private int mIndex = 0;
	private Exe frame;
	private Thread thread;
	private LoadScreen loadScreen;

	private Image texture;
	private Image cursor;
	private Image oCursor;
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

	private int x = 40;
	private int y = 47;

	public ChallengeModePanel(Exe frame, String str ,double magnification){
		this.frame = frame;
		this.str = str;
		this.setName(str);
		this.setLayout(null);
		//点滅するやつ
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		composite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		window();
		loadCourse();
		timer = new Timer();

	}

	public void window(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	public void initChallengeMode(){
		loadImage();
	}

	public void fadeIn(){
		thread = new Thread(this);
		loadScreen = new LoadScreen(true);
		isActive = true;
		isMovement = true;
		thread.start();
	}


	@Override
	public void run() {
		while(isActive){
			repaint();
			try{
				Thread.sleep(16);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/courseselect.png"));
		texture = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/cCursor.png"));
		cursor = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Texture/playoption.png"));
		option = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/optionCursor.png"));
		oCursor = icon.getImage();
	}

	public void paintComponent(Graphics g){
		x = 40;
		y = 47;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.drawImage(texture, 0, 0, null);
		g2.setFont(Parts.poiretFont);
		for(int i=0; i<levelList.size(); i++){
			g2.drawString(levelList.get(i), x, y);
			drawClearData(g2, i);
			y += 50;
		}
		drawSongList(g2);
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
						returnpc(frame.getPanelNames(1));
					}
				}else{
					isMovement = false;
					loadScreen = null;
				}
			}
		}
	}
	//100,367
	private void drawSongList(Graphics g){
		int y = 367;
		for(int i = 0; i< 3; i++){
			g.drawString(songList.get(listIndex*3+i), 100, y+40*i);
		}

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
	private void drawClearData(Graphics g,int level){
		Font font = new Font(Font.SERIF, Font.PLAIN, 30);
		g.setFont(font);
		int starX = 250;
		for(int j=0; j<5; j++){
			if(Parts.clearList.get(level*5+j) == 1){
				switch (j) {
				case 0:
					g.setColor(Color.GREEN);
					break;
				case 1:
					g.setColor(Color.YELLOW);
					break;
				case 2:
					g.setColor(Color.ORANGE);
					break;
				case 3:
					g.setColor(Color.RED);
					break;
				case 4:
					g.setColor(Color.BLACK);
					break;
				default:
					break;
				}
				g.drawString("★", starX, y);
				starX += 40;
			}
		}
		g.setFont(Parts.poiretFont);
		g.setColor(Color.BLACK);
	}

	public void loadCourse(){
		File dif = new File(getClass().getResource("DATA/Course.lst").getPath());
		try{
			FileReader fileReader = new FileReader(dif);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			//行ごと
			while((line = br.readLine()) !=null){
				if(line.contains("<")){
					levelList.add(line);
				}else{
					songList.add(line);
				}
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
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
		timer.cancel();
		timer = new Timer();
		loadScreen = null;
		isActive = false;
		isMovement = false;
		savePlayOption();
		Score.setDifficulty(Parts.guageIndex);
		frame.PanelChange((JPanel)this, str,songList,listIndex);
	}

	public void returnpc(String str){
		timer.cancel();
		timer = new Timer();
		loadScreen = null;
		thread = null;
		isActive = false;
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


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		int mod = e.getModifiersEx();
		if(!isMovement){
			if(mod == KeyEvent.SHIFT_DOWN_MASK){
			}else{
				if(key == KeyEvent.VK_UP){
					if(listIndex != 0){
						if(!isUppress){
							isUppress = true;
							System.out.println("up");
							listIndex -=1;
							cursorY -= 50;
						}
					}
				}
				if(key == KeyEvent.VK_DOWN){
					if(listIndex != levelList.size()-1){
						if(!isDownpress){
							System.out.println("down");
							isDownpress = true;
							listIndex += 1;
							cursorY +=50;
						}
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
					System.out.println("TIMER");
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

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		System.out.println(key);
		if(key == KeyEvent.VK_UP){
			isUppress = false;
		}
		if(key == KeyEvent.VK_DOWN){
			isDownpress = false;
		}
		if(key == KeyEvent.VK_SHIFT){
			while(isPlayOptionVisible){
				isPlayOptionVisible = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	public boolean getDANNI(){
		return true;
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
				alphaVal -=0.1f;
				if(alphaVal <=0.1f){
					isFlashdecision = true;
				}
			}else{
				alphaVal +=0.1f;
				if(alphaVal >=0.9f){
					isFlashdecision = false;
				}
			}
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		}
	}
}
