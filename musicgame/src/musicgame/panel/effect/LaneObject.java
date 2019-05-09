package musicgame.panel.effect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class LaneObject {
	private Image[] note = new Image[3];
	private Color[] color = new Color[3];
	private int[] obj = new int[0];
	private long[] objtime2 ;
	private int noteCol;
	private int x;
	private int hs;
	private double magnification;

	public LaneObject(int laneNum,int noteSum,int hs) {
		this.hs = hs;
		switch(laneNum){
			case 0:
				noteCol = 0;
				break;
			case 1:
				noteCol = 1;
				break;
			case 2:
				noteCol = 1;
				break;
			case 3:
				noteCol = 0;
				break;
			default :
				break;
		}
		objtime2 = new long[noteSum];
		setColor();
		//loadImage();
	}

	public void addObj(int num){
		objtime2[num] = System.currentTimeMillis();
		int[] o = new int[obj.length+1];
		for(int i=0; i<obj.length; i++){
			o[i] = obj[i];
		}
		obj = o;

	}

	public void fall(int fallCount){
		for(int i=fallCount; i<obj.length; i++){
			obj[i] +=hs;
		}

	}
	public void draw(Graphics g){
		g.setColor(color[noteCol]);
		for(int y: obj ){
//			g.drawRect(x, y, 47, 8);
			g.fillRect(x, y, 47, 6);
		}
	}

	public void setXpoint(int x){
		this.x = x;
	}

	public void setYpoint(int i, int y){
		obj[i] += y;
	}

	public long getObjTime(int i){
		return objtime2[i];
	}

	public int getYpoint(int i){
		if(i >= obj.length){
			return -1;
		}else{
			return obj[i];
		}
	}

	private void setColor(){
		color[0] = Color.RED;
		color[1] = Color.BLUE;
	}

private void loadImage(){

		ImageIcon icon = new ImageIcon(getClass().getResource("Texture/note.gif"));
		note[0] = icon.getImage();

		icon = new ImageIcon(getClass().getResource("Texture/note2.gif"));
		note[1]= icon.getImage();

		icon = new ImageIcon(getClass().getResource("Texture/note3.gif"));
		note[2] = icon.getImage();
	}

}
