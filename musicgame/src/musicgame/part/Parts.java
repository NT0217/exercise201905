package musicgame.part;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import musicgame.music.MusicDisplay;

public class Parts {

	/*
	 * staticごり押しクラス
	 * 絶対これまずいけど気にしない
	 */
	private static final int OPTION_LEN = 4;
	private static final String[] GUAGE_LIST = {"EASY","NORMAL","HARD","EXHARD"};
	private static final String[] HAZARD_BOOL = {"OFF","ON"};

	public static Font poiretFont;
	public static Font poiretFont2;
	public static Font codeFont;
	public static Font RictyFont;
	public static String[] songList;
	public static String[] optionList =new String[OPTION_LEN];
	public static ArrayList<String> levelList = new ArrayList<>();
	public static ArrayList<Integer> clearList = new ArrayList<>();

	public static int hispeed;
	public static int laneCoverHeight;
	public static int guageIndex;
	public static int hazardIndex;

	private MusicDisplay md;
	//使用フォント読み込み
	public Parts(){
		loadFont();
		loadPlayOption();
		loadSong();
		loadClearData();
	}
	public void loadSong(){
		md = new MusicDisplay();
		md.setFiles();
		songList = md.getMusiclist();
	}

	public void loadFont(){
		poiretFont = null;
		InputStream is = null;
		try{
			is = getClass().getResourceAsStream("webcontents/Poiret.ttf");
			poiretFont = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (FontFormatException e) {
			e.printStackTrace();
		}
		poiretFont = poiretFont.deriveFont(30.0f);

		poiretFont2 = null;
		is = null;
		try{
			is = getClass().getResourceAsStream("webcontents/Poiret.ttf");
			poiretFont2 = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (FontFormatException e) {
			e.printStackTrace();
		}
		poiretFont2 = poiretFont2.deriveFont(20.0f);

		codeFont = null;
		is = null;
		try{
			is = getClass().getResourceAsStream("webcontents/code.ttf");
			codeFont = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (FontFormatException e) {
			e.printStackTrace();
		}
		codeFont = codeFont.deriveFont(40.0f);

		RictyFont = null;
		is = null;
		try{
			is = getClass().getResourceAsStream("webcontents/RictyDiminished-Regular.ttf");
			RictyFont = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (FontFormatException e) {
			e.printStackTrace();
		}
		RictyFont = RictyFont.deriveFont(30.0f);
	}

	public void loadPlayOption(){
		File file = new File(getClass().getResource("DATA/playoption.ini").getPath());
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			int i =0;
			//行ごと
			while((line = br.readLine()) !=null){
				String[] data = line.split("=",0);
				optionList[i] = data[1];
				System.out.println("LINE"+line);
				if(line.contains("Gu")){
					for(int j= 0; j<GUAGE_LIST.length; j++){
						if(data[1].equals(GUAGE_LIST[j])){
							guageIndex = j;
							System.out.println("GUAGEINDEX:"+guageIndex);
							break;
						}
					}
				}
				if(line.contains("Hi")){
					hispeed = Integer.parseInt(data[1]);
				}
				if(line.contains("Lane")){
					laneCoverHeight = Integer.parseInt(data[1]);
				}
				if(line.contains("Ha")){
					for(int j= 0; j<HAZARD_BOOL.length; j++){
						if(line.contains(HAZARD_BOOL[j])){
							hazardIndex= j;
							break;
						}
					}
				}
				i++;
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void loadClearData(){
		File cdata = new File(getClass().getResource("DATA/ClearData.lst").getPath());
		try{
			FileReader fileReader = new FileReader(cdata);
			BufferedReader br =new BufferedReader(fileReader);
			String line;
			while((line = br.readLine()) != null){
				if(line.contains("<")){
					levelList.add(line);
					continue;
				}else{
					String[] data = line.split(" = ",0);
					clearList.add(Integer.parseInt(data[1]));
				}
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveClearData(){
		File cdata = new File(getClass().getResource("DATA/ClearData.lst").getPath());
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(cdata)));
			for(int i=0; i<levelList.size(); i++ ){
				pw.println(levelList.get(i));
				pw.println("Easy = "+clearList.get(i*5+0));
				pw.println("Normal = "+clearList.get(i*5+1));
				pw.println("Hard = "+clearList.get(i*5+2));
				pw.println("Exhard = "+clearList.get(i*5+3));
				pw.println("Hazard = "+clearList.get(i*5+4));
			}
			pw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static int getSongList(int i){
		while(i < 0) {
			i += Parts.songList.length;
		}
		while(i > Parts.songList.length - 1) {
			i -= Parts.songList.length;
		}

		return i;
	}

}
