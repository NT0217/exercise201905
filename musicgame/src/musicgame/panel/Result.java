package musicgame.panel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import musicgame.Exe;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
public class Result extends Application{

	private Scene scene;

	private static DocumentBuilder documentBuilder = null;
	private final static String FILE_FOUND = "File found,and loaded.";
	private final static String FILE_NOT_FOUND = "File not found,and created.";

	private final static String SCORE_LABEL = "- Score -";
	private final static String PERFECT_LABEL = "Perfect";
	private final static String GREAT_LABEL = "Great";
	private final static String GOOD_LABEL = "Good";
	private final static String BAD_LABEL = "Bad";
	private final static String MISS_LABEL = "Miss";
	private final static String HITS_LABEL = "HITS";
	private final static String HISCORE_LABEL = "HiScore!";
	private final static String CLEAR_LABEL = "Clear!";
	private final static String FAILED_LABEL = "Failed";

	private static String scoreAddress;
	private static String imgAddress;
	/* Current Directory Path */
	private static String path = ".";
	private static boolean reScore = false;
	/* Dummy */
	private static String SongTitle;
	// private static String ArtistName = "2gnoS";
	private static int Perfect;
	private static int Great;
	private static int Good;
	private static int Miss;
	private static int Bad;
	private static int MaxCombo;
	private static int SongScore;

	private boolean isSongClear = false;



	private static int stageWidth = 640;
	private static int stageHeight = 480;
	private Exe frame;

	private boolean isEnterpress = false;
	private boolean isEscapepress = false;

	public Result(Exe frame ,double magnification){
		this.frame = frame;
	}
	public void delScore(){
		Perfect = 0;
		Great = 0;
		Good = 0;
		Bad = 0;
		Miss = 0;
		MaxCombo = 0;
		SongScore = 0;
	}

	public void setScore(String mt,int pe, int gre, int goo,int ba, int mis, int maxc ,int score,boolean bool) {
		SongTitle = mt;
		Perfect = pe;
		Great = gre;
		Good = goo;
		Bad = ba;
		Miss = mis;
		MaxCombo = maxc;
		SongScore = score;
		isSongClear = bool;
	}

	public void readResult(){
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		scoreAddress = getClass().getResource("DATA").getPath()+"/score.xml";
		System.out.println("SCOREADDRESS:"+scoreAddress);
		imgAddress =getClass().getResource("Song")+"/"+SongTitle+"/"+SongTitle+".jpg";
		hiScoreWriter();
		// statusWriter();

		/* Background Image */
		Image image = new Image(imgAddress);
		ImageView bg = new ImageView();
		bg.setImage(image);
		bg.setPreserveRatio(true);
		bg.setFitWidth(stageWidth * 1.15);
		bg.setFitHeight(stageWidth * 1.15);
		bg.setEffect(new GaussianBlur(35));

		final StackPane sp = new StackPane();

		sp.getChildren().add(bg);
		scene = new Scene(sp, stageWidth, stageHeight);
		scene.setOnKeyPressed(event -> keyPress(event));
		scene.setOnKeyReleased(event -> keyRelease(event));


		/* Status Panel */
		final Rectangle rect = new Rectangle(0, 0, stageWidth - 30, stageHeight - 30);
		rect.setFill(Color.WHITE);

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(6.0);
		dropShadow.setOffsetX(0.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0, 0, 0, 0.23));
		rect.setEffect(dropShadow);
		rect.setOpacity(0.0);
		/* Panel Border Radius */
		rect.setArcWidth(7);
		rect.setArcHeight(7);

		/* Panel FadeIn */
		final FadeTransition fadeRect = new FadeTransition(new Duration(700));
		fadeRect.setNode(rect);
		fadeRect.setToValue(0.82);

		/* Panel ScaleUp */
		final ScaleTransition scale = new ScaleTransition(new Duration(700));
		scale.setNode(rect);
		scale.setFromX(0.0);
		scale.setFromY(0.0);
		scale.setToX(1);
		scale.setToY(1);

		scale.setInterpolator(Interpolator.SPLINE(0.86, 0, 0.07, 1));
		scale.setCycleCount(1);

		/**************************************************************************************************
		 *                                                                                                *
		 * addEffect() value: text / img / chart, fade, translate, pointX, endY, (fontSize), (fontFamily) *
		 *                                  															  *
		 * StackPane point(0,0): Center                                                                   *
		 * 																							      *
		 *************************************************************************************************/

		/* SongTitle */
		final Text songTitle = new Text(SongTitle);
		final FadeTransition fadeSt = new FadeTransition(new Duration(1000));
		final TranslateTransition translateSt = new TranslateTransition(new Duration(1000));
		addEffect(songTitle, fadeSt, translateSt, 0, -160, 50, "poiret");

		/* Jacket Image */
		final ImageView jacket = new ImageView();
		final FadeTransition fadeJk = new FadeTransition(new Duration(1000));
		final TranslateTransition translateJk = new TranslateTransition(new Duration(1000));
		jacket.setImage(image);
		jacket.setPreserveRatio(true);
		jacket.setFitWidth(125);
		jacket.setFitHeight(125);
		addEffect(jacket, fadeJk, translateJk, -180, -20);

		/* Score Label */
		final Text socreLabel = new Text(SCORE_LABEL);
		final FadeTransition fadeSl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateSl = new TranslateTransition(new Duration(1000));
		addEffect(socreLabel, fadeSl, translateSl, -179, 70, 29, "poiret");

		/* Score Value */
		final Text scoreValue = new Text(String.valueOf(String.format("%07d", SongScore)));
		final FadeTransition fadeSv = new FadeTransition(new Duration(1000));
		final TranslateTransition translateSv = new TranslateTransition(new Duration(1000));
		addEffect(scoreValue, fadeSv, translateSv, -179, 100, 29, "code");

		/* Perfect Label */
		final Text perfectLabel = new Text(PERFECT_LABEL);
		final FadeTransition fadePl = new FadeTransition(new Duration(1000));
		final TranslateTransition translatePl = new TranslateTransition(new Duration(1000));
		addEffect(perfectLabel, fadePl, translatePl, -25, 50, 29, "poiret");

		/* Perfect Value */
		final Text perfectValue = new Text(String.valueOf(String.format("%04d", Perfect)));
		final FadeTransition fadePv = new FadeTransition(new Duration(1000));
		final TranslateTransition translatePv = new TranslateTransition(new Duration(1000));
		addEffect(perfectValue, fadePv, translatePv, 65, 50, 29, "code");

		/* Great Label */
		final Text greatLabel = new Text(GREAT_LABEL);
		final FadeTransition fadeGrl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateGrl = new TranslateTransition(new Duration(1000));
		addEffect(greatLabel, fadeGrl, translateGrl, 163, 50, 29, "poiret");

		/* Great Value */
		final Text greatValue = new Text(String.valueOf(String.format("%04d", Great)));
		final FadeTransition fadeGrv = new FadeTransition(new Duration(1000));
		final TranslateTransition translateGrv = new TranslateTransition(new Duration(1000));
		addEffect(greatValue, fadeGrv, translateGrv, 245, 50, 29, "code");

		/* Good Label */
		final Text goodLabel = new Text(GOOD_LABEL);
		final FadeTransition fadeGol = new FadeTransition(new Duration(1000));
		final TranslateTransition translateGol = new TranslateTransition(new Duration(1000));
		addEffect(goodLabel, fadeGol, translateGol, -21, 110, 29, "poiret");

		/* Good Value */
		final Text goodValue = new Text(String.valueOf(String.format("%04d", Good)));
		final FadeTransition fadeGov = new FadeTransition(new Duration(1000));
		final TranslateTransition translateGov = new TranslateTransition(new Duration(1000));
		addEffect(goodValue, fadeGov, translateGov, 65, 110, 29, "code");

		/* Bad Label */
		final Text badLabel = new Text(BAD_LABEL);
		final FadeTransition fadeBl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateBl = new TranslateTransition(new Duration(1000));
		addEffect(badLabel, fadeBl, translateBl, 174, 110, 29, "poiret");

		/* Bad Value */
		final Text badValue = new Text(String.valueOf(String.format("%04d", Bad)));
		final FadeTransition fadeBv = new FadeTransition(new Duration(1000));
		final TranslateTransition translateBv = new TranslateTransition(new Duration(1000));
		addEffect(badValue, fadeBv, translateBv, 245, 110, 29, "code");

		/* Miss Label */
		final Text missLabel = new Text(MISS_LABEL);
		final FadeTransition fadeMl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateMl = new TranslateTransition(new Duration(1000));
		addEffect(missLabel, fadeMl, translateMl, -12, 168, 29, "poiret");

		/* Miss Value */
		final Text missValue = new Text(String.valueOf(String.format("%04d", Miss)));
		final FadeTransition fadeMv = new FadeTransition(new Duration(1000));
		final TranslateTransition translateMv = new TranslateTransition(new Duration(1000));
		addEffect(missValue, fadeMv, translateMv, 65, 168, 29, "code");

		/* PieChart Notes */
		double totalNotes = Perfect + Great + Good + Bad + Miss;
		double hitNotes = Perfect + Great + Good;
		final PieChart notes = createPieChart(totalNotes, hitNotes);
		final FadeTransition fadePc = new FadeTransition(new Duration(1000));
		final TranslateTransition translatePc = new TranslateTransition(new Duration(1000));
		addEffect(notes, fadePc, translatePc, 18, -47);

		final ScaleTransition noteScale = new ScaleTransition(new Duration(700));
		noteScale.setNode(notes);
		noteScale.setFromX(0.18);
		noteScale.setFromY(0.18);
		noteScale.setToX(0.20);
		noteScale.setToY(0.20);

		/* Hits Label */
		final Text hitsLabel = new Text(HITS_LABEL + "(" + String.valueOf((int)hitNotes) + "/" + String.valueOf((int)totalNotes) + ")");
		final FadeTransition fadeHl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateHl = new TranslateTransition(new Duration(1000));
		addEffect(hitsLabel, fadeHl, translateHl, 18, 11, 17, "poiret");

		/* PieChart Chain */
		final PieChart chain = createPieChart(totalNotes, MaxCombo);
		final FadeTransition fadeCh = new FadeTransition(new Duration(1000));
		final TranslateTransition translateCh = new TranslateTransition(new Duration(1000));
		addEffect(chain, fadeCh, translateCh, 206, -47);

		/* Chain Label */
		final Text chainLabel = new Text(HITS_LABEL + "(" + String.valueOf((int)MaxCombo) + ")");
		final FadeTransition fadeCl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateCl = new TranslateTransition(new Duration(1000));
		addEffect(chainLabel, fadeCl, translateCl, 208, 11, 17, "poiret");

		final ScaleTransition chainScale = new ScaleTransition(new Duration(700));
		chainScale.setNode(chain);
		chainScale.setFromX(0.18);
		chainScale.setFromY(0.18);
		chainScale.setToX(0.20);
		chainScale.setToY(0.20);

		/* Miss Label */
		final Text hiScoreLabel = new Text(HISCORE_LABEL);
		final FadeTransition fadeHil = new FadeTransition(new Duration(1000));
		final TranslateTransition translateHil = new TranslateTransition(new Duration(1000));
		addEffect(hiScoreLabel, fadeHil, translateHil, -150, 124, 17, "poiret");
		hiScoreLabel.setFill(Color.rgb(200, 50, 50));

		/* Clear Label */
		final Text clearLabel = new Text(CLEAR_LABEL);
		final FadeTransition fadeCll = new FadeTransition(new Duration(1000));
		final TranslateTransition translateCll = new TranslateTransition(new Duration(1000));
		addEffect(clearLabel, fadeCll, translateCll, -178, 150, 35, "poiret");
		clearLabel.setFill(Color.rgb(200, 50, 50));

		/* Failed Label */
		final Text failedLabel = new Text(FAILED_LABEL);
		final FadeTransition fadeFl = new FadeTransition(new Duration(1000));
		final TranslateTransition translateFl = new TranslateTransition(new Duration(1000));
		addEffect(failedLabel, fadeFl, translateFl, -178, 150, 35, "poiret");
		failedLabel.setFill(Color.rgb(50, 50, 200));


		Timeline timeline = new Timeline(new KeyFrame(new Duration(100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(rect);
				fadeRect.play();
				scale.play();
			}
		}), new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(songTitle);
				fadeSt.play();
				translateSt.play();
			}
		}), new KeyFrame(new Duration(700), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(jacket);
				fadeJk.play();
				translateJk.play();
			}
		}), new KeyFrame(new Duration(800), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(socreLabel);
				fadeSl.play();
				translateSl.play();
			}
		}), new KeyFrame(new Duration(900), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(scoreValue);
				fadeSv.play();
				translateSv.play();
			}
		}), new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(notes);
				fadePc.play();
				translatePc.play();
				noteScale.play();
			}
		}), new KeyFrame(new Duration(1050), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(hitsLabel);
				fadeHl.play();
				translateHl.play();
			}
		}), new KeyFrame(new Duration(1150), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(chain);
				fadeCh.play();
				translateCh.play();
				chainScale.play();
			}
		}), new KeyFrame(new Duration(1200), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(chainLabel);
				fadeCl.play();
				translateCl.play();
			}
		}), new KeyFrame(new Duration(1250), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(perfectLabel);
				fadePl.play();
				translatePl.play();
			}
		}), new KeyFrame(new Duration(1300), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(perfectValue);
				fadePv.play();
				translatePv.play();
			}
		}), new KeyFrame(new Duration(1350), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(greatLabel);
				fadeGrl.play();
				translateGrl.play();
			}
		}), new KeyFrame(new Duration(1400), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(greatValue);
				fadeGrv.play();
				translateGrv.play();
			}
		}), new KeyFrame(new Duration(1450), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(goodLabel);
				fadeGol.play();
				translateGol.play();
			}
		}), new KeyFrame(new Duration(1500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(goodValue);
				fadeGov.play();
				translateGov.play();
			}
		}), new KeyFrame(new Duration(1550), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(badLabel);
				fadeBl.play();
				translateBl.play();
			}
		}), new KeyFrame(new Duration(1600), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(badValue);
				fadeBv.play();
				translateBv.play();
			}
		}), new KeyFrame(new Duration(1650), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(missLabel);
				fadeMl.play();
				translateMl.play();
			}
		}), new KeyFrame(new Duration(1700), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sp.getChildren().add(missValue);
				fadeMv.play();
				translateMv.play();
			}
		}), new KeyFrame(new Duration(2200), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(reScore){
					sp.getChildren().add(hiScoreLabel);
					fadeHil.play();
					translateHil.play();
				}
			}
		}), new KeyFrame(new Duration(2100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(isSongClear){
					sp.getChildren().add(clearLabel);
					fadeCll.play();
					translateCll.play();
				}else{
					sp.getChildren().add(failedLabel);
					fadeFl.play();
					translateFl.play();
				}
			}
		}));

		timeline.play();
	}
	public void addEffect(Text text, FadeTransition ft, TranslateTransition tl, double pointX, double pointY,
			int fs, String ff) {
		text.setOpacity(0.0);
		text.setFont(Font.loadFont(new File(getClass().getResource("webcontents").getPath()+"/" + ff + ".ttf").toURI().toString(), fs));
		text.setFill(Color.rgb(50, 50, 50));
		/* Text FadeIn */
		ft.setNode(text);
		ft.setToValue(1.0);
		/* MoveUp SongTitle */
		tl.setNode(text);
		tl.setFromX(pointX);
		tl.setFromY(pointY + 70);
		tl.setToY(pointY);
		tl.setAutoReverse(false);
		tl.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));
		tl.setCycleCount(1);
	}

	public static void addEffect(ImageView iv, FadeTransition ft, TranslateTransition tl, double pointX,
			double pointY) {
		iv.setOpacity(0.0);
		/* Text FadeIn */
		ft.setNode(iv);
		ft.setToValue(1.0);
		/* MoveUp SongTitle */
		tl.setNode(iv);
		tl.setFromX(pointX);
		tl.setFromY(pointY + 70);
		tl.setToY(pointY);
		tl.setAutoReverse(false);
		tl.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));
		tl.setCycleCount(1);
	}

	public static void addEffect(PieChart pc, FadeTransition ft, TranslateTransition tl, double pointX, double pointY) {
		pc.setOpacity(0.0);
		/* Text FadeIn */
		ft.setNode(pc);
		ft.setToValue(1.0);
		/* MoveUp SongTitle */
		tl.setNode(pc);
		tl.setFromX(pointX);
		tl.setFromY(pointY + 70);
		tl.setToY(pointY);
		tl.setAutoReverse(false);
		tl.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));
		tl.setCycleCount(1);
	}

	public static String rePlaceStr(String str) {
		str = str.replaceAll(" ", "_s_").replaceAll("\\?", "_q_").replaceAll("!", "_e_").replaceAll("'", "_a_");
		return str;
	}

	public static void hiScoreWriter() {
		System.out.println("HISCOREWRITER");
		int hiScore;
		Element list;
		Document document = null;

		try {
			System.out.println("TRY1");
			FileInputStream file = new FileInputStream(scoreAddress);
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				System.out.println("TRY2");
				builder = dbfactory.newDocumentBuilder();
				document = builder.parse(file);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
			list = document.getDocumentElement();
			System.out.println(FILE_FOUND);
			file.close();
		} catch (Exception e) {
			document = documentBuilder.newDocument();
			list = document.createElement("list");
			document.appendChild(list);
			System.out.println(FILE_NOT_FOUND);
		}

		Node node = document.getElementsByTagName(rePlaceStr(SongTitle)).item(0);

		if (node == null) {
			System.out.println("IF1");
			Element songName = document.createElement(rePlaceStr(SongTitle));
			list.appendChild(songName);
			songName.appendChild(document.createTextNode(String.valueOf(SongScore)));
			reScore = true;
		} else {
			hiScore = Integer.parseInt(node.getTextContent());
			if (SongScore > hiScore) {
				System.out.println("IF2");
				node.getFirstChild().setNodeValue(String.valueOf(SongScore));
				reScore = true;

			} else {
				reScore = false;

			}
		}
		File file = new File(scoreAddress);
		xmlGenerator(file, document);

	}

	public static boolean xmlGenerator(File file, Document document) {

		Transformer transformer = null;
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return false;
		}

		transformer.setOutputProperty("indent", "yes");
		transformer.setOutputProperty("encoding", "Shift_JIS");

		try {
			transformer.transform(new DOMSource(document), new StreamResult(file));
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public PieChart createPieChart(double parent, double child) {
		PieChart chart = new PieChart();

		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(

		new PieChart.Data(null, (child / parent) * 100), new PieChart.Data(null, 100 - (child / parent) * 100));
		chart.setData(data);
		chart.setMinWidth(10);

		chart.setStartAngle(90);

		chart.setLegendVisible(false);

		return chart;
	}

 	public void pc(String str){
		frame.panelChange();
	}

	public Scene getScene(){
		return scene;
	}

	public void keyPress(KeyEvent event) {
		if(!isEnterpress){
			isEnterpress = true;
			if(event.getCode().toString().equals("ENTER")){
				pc(frame.getPanelNames(1));
				isEnterpress = false;
			}
		}
		if(!isEscapepress){
			isEscapepress = true;
			if(event.getCode().toString().equals("ESCAPE")){
				pc(frame.getPanelNames(1));
				isEscapepress = false;
			}
		}
	}
	public void keyRelease(KeyEvent event){
		isEnterpress = false;
		isEscapepress = false;
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}
}
