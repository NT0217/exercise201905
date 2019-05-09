package musicgame.score;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Judge extends Score{

	private static final Point STORAGE = new Point(-400, -400);
	private static final int JUDGE_LINE = 450;
	private static final int F = 18;//1フレーム+誤差2ms
	private int x;
	private int y;

	private double magnification;

	//落ちる速度の設定
	private int hs;

	private Image hantei;

	private double JUDGE_TIME;


	public Judge(int hs,int allNotes){
		super(allNotes);
		this.x = STORAGE.x;
		this.y = STORAGE.y;
		this.hs = hs;
		JUDGE_TIME = JUDGE_LINE/this.hs*16;
		loadImage(0);
	}



	public int decision(long typetime){

		if(typetime>=JUDGE_TIME-F*2 && typetime<= JUDGE_TIME+F*2){
			calcScore(0);
			return 0;
		}else if(typetime>=JUDGE_TIME-F*4 && typetime<= JUDGE_TIME+F*3){
			calcScore(1);
			return 1;
		}else if(typetime>=JUDGE_TIME-F*5 && typetime<= JUDGE_TIME+F*5){
			calcScore(2);
			return 2;
		}else if(typetime>=JUDGE_TIME-F*7 && typetime<= JUDGE_TIME+F*7){
			calcScore(3);
			return 3;
		}else{
			return -1;
		}
	}

	public void store(){
		x = STORAGE.x;
		y = STORAGE.y;

	}

	public double getJtime(){
		return JUDGE_TIME;
	}

	public void setPos(int x, int y ){
		this.x = x;
		this.y = y;
	}


	public void draw(Graphics g){
		g.drawImage(hantei, x, y, null);
	}

	public void loadImage(int Num){
		ImageIcon icon;
		switch (Num) {
		case 0:
			icon= new ImageIcon(getClass().getResource("Image/perfect.png"));
			hantei = icon.getImage();
			break;
		case 1:
			icon = new ImageIcon(getClass().getResource("Image/great.png"));
			hantei = icon.getImage();
			break;
		case 2:
			icon = new ImageIcon(getClass().getResource("Image/good.png"));
			hantei = icon.getImage();
			break;
		case 3:
			icon = new ImageIcon(getClass().getResource("Image/bad.png"));
			hantei = icon.getImage();
			break;
		case 4:
			icon = new ImageIcon(getClass().getResource("Image/miss.png"));
			hantei = icon.getImage();
			break;
		default:
			break;
		}
	}
}
