package musicgame.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.panel.effect.LoadScreen;
import musicgame.part.Parts;
import musicgame.score.Score;

public class ChallengeResult extends JPanel implements KeyListener,Runnable{

	//チャレンジ用のリザルト
	//め　ん　ど　く　さ
	private static final int[] DIVIDE_NUM = {10,100,1000,10000,100000,1000000,10000000};
	private static final int[] INCREASE_NUM = {1,10,100,1000,10000,100000,1000000,10000000};

	//トータルのレートにより表示するメッセ
	//一番下は煽り、一番上は褒めてる（暴言）
	private static final String[] COMPLIMENT_MESSAGE = {"Do it seriously","Well...Good luck with that!","Good job!","Congratulations!"
														,"Excellent!","Awesome!","WTF? U are crazy!"};

	private static final int WIDTH =640;
	private static final int HEIGHT =480;

	private int number;
	private int index;
	private int compNum;
	private int modNum;
	private int count;
	private int messageIndex;
	private int[] countScore = new int[4];
	private int[] countRate = new int[4];

	private double magnification;

	private String str;

	private Exe frame;
	private Thread thread;
	private LoadScreen loadScreen;

	private Image result;

	private Timer timer;

	private boolean isEnterpress = false;
	private boolean isActive = false;
	private boolean isMovement = false;
	private boolean isIncrease = false;
	private boolean isFinishedDrawScore = false;

	public ChallengeResult(Exe frame , String str ,double magnification) {
		this.frame = frame;
		this.str = str;
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setName(str);
		this.setLayout(null);
		loadImage();
		timer = new Timer();
	}

	public void initCresult(){
		number = 1;
		index = 0;
		compNum = 0;
		modNum = 0;
		count = 0;
		messageIndex = 0;
		for(int i=0; i<4; i++){
			countScore[i] = 0;
			countRate[i] = 0;
		}
		isActive = true;
		thread = new Thread(this);
		thread.start();
	}
	public void setScore(int num){
		compNum = Score.getcScore(number);
		modNum = compNum % DIVIDE_NUM[count];
		isIncrease = true;
	}

	@Override
	public void run() {
		while(isActive){
			if(!isIncrease){
				setScore(number);
			}
			if(compNum != 0 && compNum != countScore[index]){
				if(modNum == countScore[index]){
					count++;
					modNum = compNum % DIVIDE_NUM[count];
				}else{
					countScore[index] += INCREASE_NUM[count];
				}
			}
			checkScore();
			repaint();
			try{
				Thread.sleep(16);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}


	public void checkScore(){
		switch (number) {
		case 1:
			if(countScore[index] == Score.getcScore(number)){
				countRate[index] = (int)Score.getcAchievmentRate(number);
				number +=1;
				index +=1;
				count = 0;
				isIncrease = false;
			}
			break;
		case 2:
			if(countScore[index] == Score.getcScore(number)){
				countRate[index] = (int)Score.getcAchievmentRate(number);
				number +=1;
				index +=1;
				count = 0;
				isIncrease = false;
			}
			break;
		case 3:
			if(countScore[index] == Score.getcScore(number)){
				countRate[index] = (int)Score.getcAchievmentRate(number);
				number =0;
				index +=1;
				count = 0;
				isIncrease = false;
			}
			break;
		case 0:
			if(countScore[index] == Score.getcScore(number)){
				countRate[index] = (int)Score.getcAchievmentRate(number);
				checkTotalRate();
				isFinishedDrawScore = true;
			}
			break;
		default:
			break;
		}
	}
	public void checkTotalRate(){
		if(countRate[index] <=10){
			messageIndex = 0;
		}else if(countRate[index] <=90){
			messageIndex = 1;
		}else if(countRate[index] <=150){
			messageIndex = 2;
		}else if(countRate[index] <=200){
			messageIndex = 3;
		}else if(countRate[index] <=250){
			messageIndex = 4;
		}else if(countRate[index] <=280){
			messageIndex = 5;
		}else{
			messageIndex = 6;
		}
	}

	public void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/cresult.png"));
		result = icon.getImage();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.drawImage(result, 0, 0, null);
		drawLevel(g2);
		g2.setFont(Parts.codeFont);
		if(index >=0){
			drawScore1st(g2);
		}
		if(index >=1){
			drawScore2nd(g2);
		}
		if(index >=2){
			drawScore3rd(g2);
		}
		if(index >=3){
			drawScoreTotal(g2);
		}
		if(isFinishedDrawScore){
			drawMessage(g2);
		}
		if(loadScreen != null){
			loadScreen.draw(g2);
			if(loadScreen.getFinishDecision()){
				if(!loadScreen.getFademode()){
					isEnterpress = false;
					pc(frame.getPanelNames(1));
				}else{
					loadScreen = null;
				}
			}
		}
	}

	public void drawLevel(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.setFont(Parts.poiretFont);
		String text = Score.getLevel();
		FontMetrics fm = g2.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
		g2.drawString(text, WIDTH/2-rectText.width/2, 58);
	}

	public void drawScore1st(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawString(String.format("%07d",countScore[0]), 240, 238);
		g2.drawString(String.format("%03d",countRate[0]), 490, 238);
	}
	public void drawScore2nd(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawString(String.format("%07d",countScore[1]), 240, 309);
		g2.drawString(String.format("%03d",countRate[1]), 490, 309);
	}
	public void drawScore3rd(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawString(String.format("%07d",countScore[2]), 240, 379);
		g2.drawString(String.format("%03d",countRate[2]), 490, 379);
	}
	public void drawScoreTotal(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawString(String.format("%07d",countScore[3]), 240, 449);
		g2.drawString(String.format("%03d",countRate[3]), 490, 449);
	}

	public void drawMessage(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.setFont(Parts.poiretFont);
		String text = COMPLIMENT_MESSAGE[messageIndex];
		FontMetrics fm = g2.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g2).getBounds();
		g2.drawString(text, WIDTH/2-rectText.width/2, 133);
	}

	//画面遷移のやつ
	public void pc(String str){
		timer.cancel();
		timer = new Timer();
		loadScreen = null;
		thread = null;
		isActive = false;
		isIncrease = false;
		frame.PanelChange((JPanel)this, str);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int key = e.getKeyCode();
		if(!isMovement){
			if(key == KeyEvent.VK_ENTER){
				if(!isEnterpress){
					isEnterpress = true;
					loadScreen = new LoadScreen(false);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
