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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sun.org.apache.bcel.internal.generic.LALOAD;

import musicgame.Exe;
import musicgame.panel.effect.LoadScreen;

public class TitlePanel extends JPanel implements KeyListener,Runnable{

	private static final int WIDTH =640;
	private static final int HEIGHT =480;
	private static final String START_LABEL = "PRESS ENTER KEY";

	private String str;
	private Image title;
	private AlphaComposite composite;

	private float alphaVal;
	private Exe frame;
	private Timer timer;
	private Thread thread;
	private LoadScreen loadScreen;

	private double magnification;

	private boolean isEnterpress = false;
	private boolean isFlashdecision = false;
	private boolean isActive = false;

	public TitlePanel(Exe frame, String str ,double magnification){
		this.frame = frame;
		this.str = str;
		this.magnification = magnification;
		alphaVal = 1.0f;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		setFocusable(true);
		addKeyListener(this);
		setPreferredSize(new Dimension((int)(WIDTH*magnification), (int)(HEIGHT*magnification)));
		this.setName(str);
		this.setLayout(null);
		loadImage();
		timer = new Timer();
		isActive = true;
		thread = new Thread(this);
		thread.start();
	}

	public void fadeIn(){
		thread = new Thread(this);
		loadScreen = new LoadScreen(true);
		isActive = true;
		thread.start();
	}
	//ごり押しやツー
	public void bulldoze(){
		thread = new Thread(this);
		isActive = true;
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


	//描画処理
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform at = new AffineTransform();
		at.setToScale(magnification, magnification);
		g2.setTransform(at);
		g2.drawImage(title, 0, 0, null);
		draw(g2);
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
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.setComposite(composite);
		Font font = new Font("ＭＳ ゴシック", Font.BOLD, 32);
		g2.setFont(font);
		String text = START_LABEL;
		FontMetrics fm = g.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g).getBounds();
		g2.drawString(text, WIDTH/2-rectText.width/2, 400);
	}
	//ここまで
	//画像読み込み
	private void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/title.png"));
		System.out.println("TITLE:"+getClass().getResource("Texture/title.png"));
		title = icon.getImage();
	}
	//画面遷移のやつ
	public void pc(String str){
		timer.cancel();
		timer = new Timer();
		loadScreen = null;
		thread = null;
		isActive = false;
		frame.PanelChange((JPanel)this, str);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_ENTER){
			if(!isEnterpress){
				isEnterpress = true;
				loadScreen = new LoadScreen(false);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void startFlashTimer(){
		timer.schedule(new FlashingString(), 100, 100);
	}
	//文字点滅処理
	public class FlashingString extends TimerTask{
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
