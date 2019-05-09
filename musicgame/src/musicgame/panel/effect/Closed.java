package musicgame.panel.effect;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Closed extends Thread{
	private Image shutter1;
	private Image shutter2;
	private Image failed;

	private int x = 0;
	private int failedy = 190;
	private int shutter1y = -480;
	private int shutter2y = 480;
	private AlphaComposite composite;
	private float alphaVal = 0.001f;
	private double magnification;
	
	public Closed(){
		ImageIcon icon = new ImageIcon(getClass().getResource("Texture/shutter.png"));
		shutter1 = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Texture/shutter2.png"));
		shutter2 = icon.getImage();
		icon = new ImageIcon(getClass().getResource("Image/failed.png"));
		failed = icon.getImage();
		start();
	}
	public void run(){
		while(true){
			try{
				if(shutter1y != 0){
					shutter1y += 10;
					shutter2y -= 10;
				}else{
					if(alphaVal < 0.99f){
						alphaVal += 0.01f;
					}else{
						return;
					}
				}
				Thread.sleep(16);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics g){
		g.drawImage(shutter1, x, shutter1y, null);
		g.drawImage(shutter2, x, shutter2y, null);
		Graphics2D g2 = (Graphics2D)g;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaVal);
		g2.setComposite(composite);
		g2.drawImage(failed, x, failedy, null);
	}
}
