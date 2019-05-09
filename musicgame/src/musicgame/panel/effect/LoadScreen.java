package musicgame.panel.effect;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.math.BigDecimal;

import javax.swing.ImageIcon;

public class LoadScreen extends Thread{


	private static Image loadImage;
	private AlphaComposite composite;

	private BigDecimal bd;

	private float alphaVal;

	private boolean isFadeIn;
	private boolean isFinishied;

	public LoadScreen(boolean bool){
		int picNum = -1;
		isFadeIn = bool;
		isFinishied = false;
		if(isFadeIn){
			alphaVal = 1.0f;
		}else{
			alphaVal = 0.0f;
			double ranval = Math.random();
			picNum = (int)(ranval * 10.0);
			loadImage(picNum);
		}
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		start();
	}

	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(composite);
		g2.drawImage(loadImage, 0, 0, null);
	}

	private void loadImage(int num){
		ImageIcon icon;
		if(num == -1 || num >=8){
			icon = new ImageIcon(getClass().getResource("Texture/loadtest.png"));
		}else{
			icon = new ImageIcon(getClass().getResource("Image/Load/loadpic"+num+".png"));
		}

		loadImage = icon.getImage();
	}

	@Override
	public void run(){
		while(true){
			try{
				if(isFadeIn){
					alphaVal -= 0.02f;
				}else{
					alphaVal += 0.02f;
				}
				bd = new BigDecimal(alphaVal);
				bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
				alphaVal = bd.floatValue();
				composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
				if(alphaVal == 0.0f || alphaVal == 1.0f){
					isFinishied = true;
					return ;
				}
				Thread.sleep(16);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean getFademode(){
		return isFadeIn;
	}

	public boolean getFinishDecision(){
		return isFinishied;
	}

}
