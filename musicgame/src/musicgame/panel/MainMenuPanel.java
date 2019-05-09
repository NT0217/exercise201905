package musicgame.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.panel.effect.LoadScreen;

public class MainMenuPanel extends JPanel implements KeyListener,Runnable{

	private static final int WIDTH =640;
	private static final int HEIGHT =480;
	private static final int CURSOR_WIDTH = 40;
	private static final int CURSOR_HEIGHT = 40;

	private int cursorX = 140;
	private int cursorY = 55;
	private int selectNum;
	private double magnification;


	private Image texture;
	private ImageIcon cursor;

	private Exe frame;
	private String str;

	private JLabel cursorPos;
	private Thread thread;
	private LoadScreen loadScreen;

	private boolean isUppress = false;
	private boolean isDownpress = false;
	private boolean isEnterpress = false;
	private boolean isActive = false;
	private boolean isMovement = false;

	public MainMenuPanel(Exe frame, String str ,double magnification) {
		this.frame = frame;
		this.str = str;
		this.magnification = magnification;
		this.setName(str);
		loadImage();
		window();
		selectNum = 0;
	}

	public void window(){
		addKeyListener(this);
		setPreferredSize(new Dimension((int)(WIDTH*magnification), (int)(HEIGHT*magnification)));
		setLayout(null);
	}
	public void setCursor(){
		cursorPos = new JLabel(cursor);
		cursorPos.setBounds(cursorX,cursorY, CURSOR_WIDTH, CURSOR_HEIGHT);
		add(cursorPos);
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


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform at = new AffineTransform();
		at.setToScale(magnification, magnification);
		g2.setTransform(at);
		g2.drawImage(texture, 0, 0, null);
		if(loadScreen != null){
			loadScreen.draw(g2);
			if(loadScreen.getFinishDecision()){
				if(!loadScreen.getFademode()){
					isEnterpress = false;
					if(selectNum == 0){
						pc(frame.getPanelNames(2));
					}else if(selectNum == 1){
						pc(frame.getPanelNames(6));
					}else if(selectNum == 2){
						pc(frame.getPanelNames(5));
					}else if(selectNum == 3){
						System.out.println("TITLE");
						pc(frame.getPanelNames(0));
					}
				}else{
					loadScreen = null;
					isMovement = false;
					setCursor();
				}
			}
		}
	}

	private void loadImage(){
		ImageIcon icon = new ImageIcon(getClass().getResource("Texture/modeselect.png"));
		texture = icon.getImage();
		cursor = new ImageIcon(getClass().getResource("Image/cursor.gif"));
	}

	public void pc(String str){
		selectNum = 0;
		cursorY = 55;
		loadScreen = null;
		thread = null;
		isActive =false;
		isMovement = false;
		frame.PanelChange((JPanel)this, str);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(!isMovement){
			if(key == KeyEvent.VK_UP){
				if(selectNum != 0){
					if(!isUppress){
						isUppress = true;
						selectNum--;
						cursorY -= 110;
						cursorPos.setBounds(cursorX, cursorY, CURSOR_WIDTH, CURSOR_HEIGHT);
					}
				}
			}
			if(key == KeyEvent.VK_DOWN){
				if(selectNum != 3){
					System.out.println("test");
					if(!isDownpress){
						isDownpress = true;
						selectNum++;
						cursorY += 110;
						cursorPos.setBounds(cursorX, cursorY, CURSOR_WIDTH, CURSOR_HEIGHT);
					}
				}
			}
			if(key == KeyEvent.VK_ENTER){
				if(!isEnterpress){
					isMovement =true;
					isEnterpress = true;
					remove(cursorPos);
					cursorPos = null;
					loadScreen = new LoadScreen(false);
				}
			}
		}
	}



	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_UP){
			isUppress = false;
		}
		if(key == KeyEvent.VK_DOWN){
			isDownpress = false;
		}
	}
}
