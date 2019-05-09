package musicgame.score;

import musicgame.part.Parts;

public class Score {
	/*
	 * スコア、コンボ計算用のクラス
	 * まぁここは特にこのままでいいんじゃね？
	 * あと付け足すとしたら、各判定ごとのカウント
	 */
	//素点
	private static final int RAWSCORE = 1000;
	private static final double P_BOUNUS = 1.5;
	private static final double G_BOUNUS = 1.2;
	private static final int MAX_HP = 350;
	private static final double[] EASY = {0.1,0.02,0.02,0.05};
	private static final double[] NORMAL = {0.01,0.006,0.02,0.05};
	private static final double[] HARD = {0.006,0.003,0.1,0.12};
	private static final double[] EXHARD = {0.0,0.0,0.1,0.12};


	private static final int PERFECT = 0;
	private static final int GREAT = 1;
	private static final int GOOD = 2;
	private static final int BAD = 3;
	private static final int MISS = 4;

	private static int combo;
	private static int score;
	private static int ExScore;
	private static int MaxCombo;
	private static float ComboBounus;
	private static double[] magnification;
	private static int hp = MAX_HP;

	private static int[] cScoreList = new int[4];
	private static double[] cAchievementRate = new double[4];
	private static String cLevel = new String();

	private static int perfect;
	private static int great;
	private static int good;
	private static int bad;
	private static int miss;
	private static int allNotesCount;
	private static boolean isComboCut = false;
	private static boolean isDeath = false;
	private static boolean isClear = true;
	private static boolean isChallenge = false;
	private static boolean isStart = false;

	/*
	 * HP計算は総ノーツ数から割合で加減を計算しないと楽しいことになる
	 * 凄く面倒くさい
	 * 100%から始めて最後までゲージが残ってたら勝ちでいいんじゃない？
	 *
	 */

	public Score(int allNotes){
		combo		  = 0;
		MaxCombo	  = 0;
		ComboBounus	  = 1.0f;
		score		  = 0;
		ExScore		  = 0;
		perfect		  = 0;
		great		  = 0;
		good		  = 0;
		bad			  = 0;
		miss		  = 0;
		allNotesCount = allNotes;
		System.out.println("NOTESCOUNT:"+allNotesCount);
		if(!isChallenge || !isStart){
			hp 		= MAX_HP;
			for(int i=0; i<4; i++){
				cScoreList[i] = 0;
				cAchievementRate[i] = 0;
			}
			isStart = true;
			System.out.println("ＡＡＡＡＡＡＡＡＡＡＡＡ一回だけだよ");
		}
		isComboCut 	= false;
		isDeath = false;
		isClear = true;
	}

	public  static void setDifficulty(int dif){
		switch (dif) {
		case 0:
			magnification = EASY;
			break;
		case 1:
			magnification = NORMAL;
			break;
		case 2:
			magnification = HARD;
			break;
		case 3:
			magnification = EXHARD;
			break;
		default:
			break;
		}
	}
	public void calcScore(int judge){
		if(isComboCut){
			ComboBounus = 1.0f;
		}else{
			if(combo % 50 == 0){
				//50コンボごとにスコアボーナス
				ComboBounus += 0.1f;
			}
		}
		if(!isDeath){
			switch (judge) {
			case PERFECT:
				score 	+= RAWSCORE*P_BOUNUS*ComboBounus;
				ExScore += 2;
				combo 	+= 1;
				perfect += 1;
				hp 		+= (int)(MAX_HP*magnification[0]);
				isComboCut = false;
				break;
			case GREAT:
				score 	+= RAWSCORE*G_BOUNUS*ComboBounus;
				ExScore += 1;
				combo 	+= 1;
				great 	+= 1;
				hp 		+= (int)(MAX_HP*magnification[1]);
				isComboCut = false;
				break;
			case GOOD:
				score 	+= RAWSCORE*ComboBounus;
				combo 	+= 1;
				good	+= 1;
				isComboCut = false;
				break;
			case BAD:
				score 	+= RAWSCORE/2;
				bad 	+= 1;
				hp 		-= (int)(MAX_HP*magnification[2]);
				isComboCut = true;
				break;
			case MISS:
				miss 	+= 1;
				hp 		-= (int)(MAX_HP*magnification[3]);
				isComboCut = true;
				break;
			default:
				break;
			}
			if(MaxCombo <combo){
				MaxCombo = combo;
				//↓なにこれ？
				getMaxCombo();
			}
			if(isComboCut){
				combo = 0;
			}
			if(hp >= MAX_HP){
				hp = MAX_HP;
			}
			if(hp <=0){
				hp = 0;
				isDeath = true;
				isClear = false;
			}
		}
	}
	public static void setCMode(int level){
		isChallenge = true;
		hp 		= MAX_HP;
		cLevel = Parts.levelList.get(level);
	}
	public static void setFMode(){
		isChallenge = false;
	}
	public static void setEnd(){
		isStart = false;
	}

	public static void setcScore(int musicNum){
		cScoreList[musicNum] = score;
		cScoreList[0] = cScoreList[0] + cScoreList[musicNum];
	}

	public static void setcAchievmentRate(int musicNum){
		if(isDeath){
			cAchievementRate[musicNum] += 10;
		}else{
			cAchievementRate[musicNum] += 30;
		}
		//GOOD以上の判定を出した割合
		cAchievementRate[musicNum] += ((double)(perfect+great+good)/allNotesCount*0.2)*100;
		System.out.println(((double)(perfect+great+good)/allNotesCount*0.2)*100);
		//パフェ率
		cAchievementRate[musicNum] += (double)perfect/allNotesCount*0.3*100;
		System.out.println((double)perfect/allNotesCount*0.3*100);
		//残ってたhp
		cAchievementRate[musicNum] += (double)hp/MAX_HP*0.3*100;
		System.out.println((double)hp/MAX_HP*0.3*100);

		cAchievementRate[0] = cAchievementRate[0] + cAchievementRate[musicNum];
	}

	//Escape用
	public static void setHpDeath(){
		hp = 0;
		isDeath = true;
		isClear = false;
	}

	public static int getcScore(int Num){
		return cScoreList[Num];
	}

	public static double getcAchievmentRate(int Num){
		return cAchievementRate[Num];
	}

	public static String getLevel(){
		return cLevel;
	}

	public static int getCombo(){
		return combo;
	}
	public static int getMaxCombo(){
		return MaxCombo;
	}
	public static int getScore(){
		return score;
	}
	public static boolean getDeathDecision(){
		return isDeath;
	}
	public int getHpgauge(){
		return hp;
	}
	public int getEXscore(){
		return ExScore;
	}
	public static int getPerfect() {
		return perfect;
	}
	public static int getGreat() {
		return great;
	}
	public static int getGood() {
		return good;
	}
	public static int getBad() {
		return bad;
	}
	public static int getMiss() {
		return miss;
	}
	public static boolean getClear(){
		return isClear;
	}
	public boolean getComboCut(){
		return isComboCut;
	}
}