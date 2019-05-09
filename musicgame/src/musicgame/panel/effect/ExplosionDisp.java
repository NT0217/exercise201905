package musicgame.panel.effect;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ExplosionDisp extends Thread{
	private static final int SIZE = 60;

	private Image explosionImage;

	private int count;

	private int x;
	private int y;

	public ExplosionDisp(int x, int y){
		this.x = x;
		this.y = y;
		count = 0;
		ImageIcon icon = new ImageIcon(getClass().getResource("Image/test.png"));
		explosionImage = icon.getImage();
		start();
	}

	public void draw(Graphics g){
		g.drawImage(explosionImage, x, y, x+SIZE,y+SIZE,count*SIZE, 0, count * SIZE + SIZE, SIZE, null);
	}

	@Override
	public void run(){
		while(true){
			count++;
			if(count == 15){
				return;
			}

			try{
				Thread.sleep(16);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
