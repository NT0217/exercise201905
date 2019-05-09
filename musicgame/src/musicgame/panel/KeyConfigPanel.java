package musicgame.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import musicgame.Exe;
import musicgame.panel.effect.LoadScreen;

public class KeyConfigPanel extends JPanel implements KeyListener,Runnable{

	private static final int WIDTH =640;
	private static final int HEIGHT =480;
	private static final int KEY_NUM = 4;

	private String str;
	private JLabel[] keyLabel = new JLabel[KEY_NUM];
	private JPanel[] jpanel = new JPanel[KEY_NUM];
	private JLabel savemessage = new JLabel("セーブしました。");
	private static String[] keyName = new String[KEY_NUM];
	private Font font;
	private Image keyconfig;

	private Exe frame;
	private boolean isLeftpress = false;
	private boolean isRightpress = false;
	private boolean isEnterpress = false;
	private boolean isEscapepress = false;
	private boolean isActive = false;
	private boolean isMovement = false;
	private Thread thread;
	private LoadScreen loadScreen;

	private int selectNum = 0;
	private double magnification;

	public KeyConfigPanel(Exe frame, String str ,double magnification){
		this.frame = frame;
		this.str = str;
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setName(str);
		this.setLayout(null);
		font = new Font("ＭＳ ゴシック",Font.PLAIN,80);
		selectNum = 0;
		savemessage.setBounds(-100, -100, 200, 30);
		savemessage.setForeground(Color.WHITE);
		add(savemessage);
		loadImage();
	}
	public void initKeyConf(){
		int strX = 75;
		int strY = 190;
		for(int i=0; i<KEY_NUM; i++){
			keyLabel[i] = new JLabel("CENTER");
			keyLabel[i].setFont(font);
			jpanel[i] = new JPanel();
			jpanel[i].setOpaque(false);
			jpanel[i].setBounds(strX, strY, 100, 100);
			jpanel[i].setLayout(new FlowLayout());
			jpanel[i].add(keyLabel[i]);
			add(jpanel[i]);
			strX += 130;
		}
		setKeyname();
		keyLabel[0].setForeground(Color.RED);
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
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(keyconfig, 0, 0, null);
		if(loadScreen != null){
			loadScreen.draw(g);
			if(loadScreen.getFinishDecision()){
				if(!loadScreen.getFademode()){
					pc(frame.getPanelNames(1));
				}else{
					loadScreen = null;
					isMovement = false;
					initKeyConf();
				}
			}
		}
	}

	private void setKeyname(){
		File keyconf = new File(getClass().getResource("DATA/keyconfig.ini").getPath());
		try{
			FileReader fileReader = new FileReader(keyconf);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			int i = 0;
			while((line = br.readLine()) !=null){
				String[] data = line.split("=",0);
				keyName[i] = data[1];
				keyLabel[i].setText(keyName[i]);
				i++;
			}
			repaint();
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void saveKeyconfig(){
		File keyconf = new File(getClass().getResource("DATA/keyconfig.ini").getPath());
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(keyconf)));
			pw.println("FIRST="+keyName[0]);
			pw.println("SECOND="+keyName[1]);
			pw.println("THIRD="+keyName[2]);
			pw.println("FOURTH="+keyName[3]);
			pw.close();
			savemessage.setBounds(10, 10, 200, 30);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/Keyconfig.png"));
		keyconfig = icon.getImage();
	}

	public void pc(String str){
		//画面外に排除
		savemessage.setBounds(-100, -100, 200, 30);
		loadScreen = null;
		thread = null;
		isActive = false;
		isMovement = false;
		isEscapepress =false;
		isEnterpress = false;
		frame.PanelChange((JPanel)this, str);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(!isMovement){
			if(key == KeyEvent.VK_LEFT){
				if(selectNum != 0){
					if(!isLeftpress){
						isLeftpress = true;
						keyLabel[selectNum].setForeground(Color.BLACK);
						selectNum--;
						keyLabel[selectNum].setForeground(Color.RED);
					}
				}
			}else if(key == KeyEvent.VK_RIGHT){
				if(selectNum != 3){
					if(!isRightpress){
						isRightpress = true;
						keyLabel[selectNum].setForeground(Color.BLACK);
						selectNum++;
						keyLabel[selectNum].setForeground(Color.RED);
					}
				}
			}else if(key == KeyEvent.VK_ENTER){
				if(!isEnterpress){
					isEnterpress = true;
					isMovement = true;
					saveKeyconfig();
					for(int i=0; i<KEY_NUM; i++){
						remove(jpanel[i]);
						keyLabel[i] = null;
						jpanel[i] = null;
					}
					loadScreen = new LoadScreen(false);
				}
			}else if(key == KeyEvent.VK_ESCAPE){
				if(!isEscapepress){
					isEscapepress = true;
					isMovement = true;
					saveKeyconfig();
					for(int i=0; i<KEY_NUM; i++){
						remove(jpanel[i]);
						keyLabel[i] = null;
						jpanel[i] = null;
					}
					loadScreen = new LoadScreen(false);
				}
			}else{
				//選択されているレーンのキー変更入力処理
				keyName[selectNum] = String.valueOf(e.getKeyChar());
				keyLabel[selectNum].setText(keyName[selectNum]);
				repaint();
			}
		}

	}
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT){
			isLeftpress = false;
		}
		if(key == KeyEvent.VK_RIGHT){
			isRightpress = false;
		}
		if(key == KeyEvent.VK_ENTER){
			isEnterpress = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}
