package musicgame;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class StartOption extends JFrame{
	private double magnification = 1.0;

	public StartOption() {
		setTitle("Option");
		setPanel();
		setResizable(true);
		pack();
	}

	public void setPanel(){
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.setPreferredSize(new Dimension(300, 200));
		JRadioButton[] magnificationSelection = new JRadioButton[3];
		ButtonGroup group = new ButtonGroup();
		JLabel message = new JLabel("画面の表示倍率を選択してください。");
		JButton decision = new JButton("起動");
		decision.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				/*double magnification
				 * Exe起動処理
				 */
				System.out.println("起動したよー＾ｗ＾");
				for (int i = 0; i < magnificationSelection.length; i++) {
					if(magnificationSelection[i].isSelected()){
						System.out.println(magnificationSelection[i].getText());
						magnification = Double.parseDouble(magnificationSelection[i].getText());
					}
				}
				dispose();
				Exe game = new Exe(magnification);
				game.start();
			}
		});
		for (int i = 0; i < magnificationSelection.length; i++) {
			if(i == 0){
				magnificationSelection[i] = new JRadioButton((i+1)+".0", true);
			}else{
				magnificationSelection[i] = new JRadioButton((i+1)+".0", false);
			}
			group.add(magnificationSelection[i]);
		}
		panel1.add(message,BorderLayout.NORTH);
		panel1.add(magnificationSelection[0],BorderLayout.WEST);
		panel1.add(magnificationSelection[1],BorderLayout.CENTER);
		panel1.add(magnificationSelection[2],BorderLayout.EAST);
		panel1.add(decision,BorderLayout.SOUTH);
		add(panel1);
	}

	public static void main(String[] args) {
		StartOption startmenu = new StartOption();
		startmenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startmenu.setVisible(true);
		startmenu.setLocationRelativeTo(null);
	}

}
