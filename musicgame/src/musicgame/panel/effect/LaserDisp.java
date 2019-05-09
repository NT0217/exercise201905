package musicgame.panel.effect;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

public class LaserDisp {

	//画面外に最初から書いておく
	private static final Point STORAGE = new Point(-400, -400);
	private int x;
	private int y;
	private double magnification;
	
	private Image laser;


	public LaserDisp(){
		x = STORAGE.x;
		y = STORAGE.y;
		loadImage();
	}

	public void store(){
		x = STORAGE.x;
		y = STORAGE.y;
	}

	public Point getPos() {
		return new Point(x, y);
	}

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	//202/32
	public void draw(Graphics g){
		g.drawImage(laser, x, y, null);
	}

	private void loadImage(){
		ImageIcon icon ;
		icon = new ImageIcon(getClass().getResource("Texture/laser.png"));
		laser = icon.getImage();
	}

}
