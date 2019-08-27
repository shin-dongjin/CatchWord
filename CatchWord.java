import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class CatchWord extends Thread{
	JFrame f;
	Container cp;
	JTextField question;	
	JTextField tfAnswer;
	JTextArea tp;
	JButton bPass;
	JTextField tfTimer;
	Font f1, f2, f3;
	CWClient cwc = new CWClient(this);	

	void init(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(Exception e){}
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		setUI();
		start(); //ip port 받아오기
	}
	public void run(){
		cwc.init();
	}
	void setUI(){
		//(1) f 셋팅
		f = new JFrame();
		cp = f.getContentPane();
		cp.setLayout(new BorderLayout());
		CWHandler h = new CWHandler(this);		
		
		//(2) pNorth 셋팅(문제 단어와 제한시간 필드)
		Color c1 = new Color(184, 207, 229);
		Color c2 = new Color(99, 130, 191);

		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY)));

		JPanel pNorthIn = new JPanel(new GridLayout(1,1));

		JPanel pNorthInq = new JPanel();
		pNorthInq.setBackground(new Color(135,206,235));
		pNorthInq.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "문제 단어"));
		question = new JTextField(8);
		pNorthInq.add(question);
		question.setEditable(false);

		JPanel pNorthInStart = new JPanel();
		pNorthInStart.setBackground(new Color(135,206,235));
		pNorthInStart.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "제한 시간"));
		tfTimer = new JTextField(10);
		tfTimer.setEditable(false);
		pNorthInStart.add(tfTimer);

		f1 = new Font("궁서", Font.BOLD, 30);
		f2 = new Font("돋움", Font.BOLD, 15);
		question.setFont(f1);
		tfTimer.setFont(f2);

		pNorthInq.add(pNorthInStart);
		pNorthIn.add(pNorthInq);
		pNorth.add(pNorthIn);

		//(3) pCenter 셋팅(채팅창 필드)
		JPanel pCenter = new JPanel(new BorderLayout());
		pCenter.setBackground(new Color(135,206,235));
        pCenter.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "채팅창"));
		tp = new JTextArea();
		pCenter.setBackground(new Color(135,206,235));
		JScrollPane sp = new JScrollPane(tp);
		pCenter.add(sp, BorderLayout.CENTER);
		tp.setEditable(false); //채팅창은 편집하지 못하도록

		//(4) pSouth 셋팅(채팅입력칸 & 패스버튼 필드)
		JPanel pSouth = new JPanel(new BorderLayout());
		pSouth.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY)));
		JPanel pSouthInAnswer = new JPanel();
		pSouthInAnswer.setBackground(new Color(135,206,235));
		pSouthInAnswer.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2)));
		tfAnswer = new JTextField(30);
		tfAnswer.addActionListener(h);
		tfAnswer.setEditable(false);

		bPass = new JButton("패스!");
		f3 = new Font("바탕", Font.BOLD, 15);
		bPass.setFont(f3);
		bPass.setBackground(new Color(199,021,133));
		bPass.setForeground(Color.WHITE);

		pSouthInAnswer.add(tfAnswer);
		pSouthInAnswer.add(bPass);
		bPass.setEnabled(false);
		pSouth.add(pSouthInAnswer);

		bPass.addActionListener(h);
		
		cp.add(pNorth, BorderLayout.NORTH);
		cp.add(pCenter, BorderLayout.CENTER);
		cp.add(pSouth, BorderLayout.SOUTH);
		always();
		tfAnswer.requestFocus();
	}
	void always(){
		f.setTitle("Catch Word Game");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(600, 500);
		f.setLocation(550, 250);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int answer = JOptionPane.showConfirmDialog(f, "종료할까요?", "선택", JOptionPane.OK_CANCEL_OPTION);
				if(answer == JOptionPane.YES_OPTION){
					System.exit(-1);
				}else{
					f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}

	public static void main(String[] args) {
		CatchWord c = new CatchWord();
		c.init();
	}
}

