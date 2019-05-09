package musicgame.music;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MusicDisplay implements ListSelectionListener{

	private File folder = new File(getClass().getResource("Song").getPath());
	private File files[] = folder.listFiles();
	private ArrayList<String> folderName = new ArrayList<>();
	private static ArrayList<File> musicList = new ArrayList<>();

	private static PlayMusic playMusic;


	public MusicDisplay(){
		for(int i=0; i<files.length; i++){
			try{
				//フォルダ名取得
				folderName.add(files[i].getName());
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		playMusic = new PlayMusic();
	}

	public void setFiles(){
		for(int i=0; i<folderName.size(); i++){
			try{
				String filename = folderName.get(i);
				FilenameFilter filter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if(name.equals(filename+".wav")){
							System.out.println("OK");
							System.out.println(name);
							return true;
						}else{
							System.out.println("FALSE");
							return false;
						}
					}
				};
				//.wavファイルのパス取得
				File[] file = new File(getClass().getResource("Song").getPath()+"/"+filename).listFiles(filter);
				if(file.length == 0){

				}else{
					musicList.add(file[0]);
				}
			}catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getMusiclist(){
		String[] str = folderName.toArray(new String[0]);
		return str;
	}


	public void play(String str){
		System.out.println("STR:"+str);
		int num = 0;
		System.out.println("SIZE:"+musicList.size());
		for(int i=0; i<musicList.size(); i++){
			System.out.println("TESt");
			if(str.equals(folderName.get(i))){
				num = i;
			}
		}
		playMusic.read(musicList.get(num));
	}

	public void stop(){
		playMusic.stop();
	}
	public int songNum(){
		return musicList.size() ;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	}
}
