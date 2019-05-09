package musicgame.music;

import java.io.File;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class PlayMusic extends TimerTask{
	/*
	 * これMusicDisplayに書けばよくね？
	 * そのうち移植手術する
	 */

	private static AudioFormat format = null;
	private static DataLine.Info info =null;
	private static Clip line = null;
	private static File audioFile = null;
	private static FloatControl control = null;
	private Timer timer;

	private double vol = 0.5;

	public void read(File f){
		try{
			audioFile = f;
			format = AudioSystem.getAudioFileFormat(audioFile).getFormat();
			info = new DataLine.Info(Clip.class,format);
			line = (Clip)AudioSystem.getLine(info);
			line.open(AudioSystem.getAudioInputStream(audioFile));
			control =  (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try{
			controlByLinearScalar(control, vol);
			line.start();
			System.out.println("START!!!!!!!!!!");
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void stop(){
		timer = new Timer();
		timer.schedule(new FadeOut(), 0,50);
	}

	private void controlByLinearScalar(FloatControl control, double linearScalar) {
		control.setValue((float)Math.log10(linearScalar) * 20);
	}


	class FadeOut extends TimerTask{

		BigDecimal bd;
		@Override
		public void run() {
			vol -= 0.010;
			bd = new BigDecimal(vol);
			bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
			controlByLinearScalar(control, bd.doubleValue());
			System.out.println(bd.doubleValue());
			if(bd.doubleValue() <= 0.0f){
				line.stop();
				line.close();
				timer.cancel();
			}
		}
	}
}
