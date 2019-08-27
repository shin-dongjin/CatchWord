import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class IPnPort extends Thread{
	JFrame f;
	Container cp;
	JTextField fIP;
	JTextField fPort;
	JTextField fId;
	JButton bConfirm; //확인버튼
	JButton bManual; //게임설명 버튼

	void init(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(Exception e){}
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		setUI();
	}
	void setUI(){
		//(1) f 셋팅
		f = new JFrame();
		cp = f.getContentPane();
		cp.setLayout(new BorderLayout());

		IPnPortHandler h = new IPnPortHandler(this); //Handler객체 생성
		
		Color c1 = new Color(184, 207, 229);
		Color c2 = new Color(99, 130, 191);
		
		//(2) pCenter 셋팅(ID, PORT, ID 받는 필드)
		JPanel pCenter = new JPanel(new GridLayout(3,1));

		JPanel pCenterhInIp = new JPanel();
		pCenterhInIp.setBackground(new Color(135,206,235));
		pCenterhInIp.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "IP"));
		fIP = new JTextField(10);
		fIP.setText("127.0.0.1");
		pCenterhInIp.add(fIP);
		pCenter.add(pCenterhInIp);

		JPanel pCenterInPort = new JPanel();
		pCenterInPort.setBackground(new Color(135,206,235));
		pCenterInPort.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "PORT"));
		fPort = new JTextField(10);
		fPort.setText("6000");
		pCenterInPort.add(fPort);
		pCenter.add(pCenterInPort);

		JPanel pCenterInId = new JPanel();
		pCenterInId.setBackground(new Color(135,206,235));
		pCenterInId.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2), "ID"));
		fId = new JTextField(10);
		pCenterInId.add(fId);
		pCenter.add(pCenterInId);

		//(3) pSouth 셋팅(게임입장과 게임설명 버튼 필드 )
		JPanel pSouth = new JPanel(new BorderLayout());
		pSouth.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY)));
		JPanel pSouthInAnswer = new JPanel();
		pSouthInAnswer.setBackground(new Color(255,255,204));
		pSouthInAnswer.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2)));
		bConfirm = new JButton("게임 입장");
		bConfirm.setBackground(new Color(255,204,000));
		bManual = new JButton("게임 설명");
		bManual.setBackground(new Color(255,204,000));
		pSouthInAnswer.add(bManual);
		pSouthInAnswer.add(bConfirm);
		pSouth.add(pSouthInAnswer);
		
		cp.add(pCenter, BorderLayout.CENTER);
		cp.add(pSouth, BorderLayout.SOUTH);
		always();

		fIP.addActionListener(h);
		fPort.addActionListener(h);
		fId.addActionListener(h);
		bConfirm.addActionListener(h);
		bManual.addActionListener(h);		
	}
	void always(){
		f.setTitle("Catch Word Game");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(300, 250);
		f.setLocation(600, 350);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int answer = JOptionPane.showConfirmDialog(f, "종료할까요?", "선택", JOptionPane.OK_CANCEL_OPTION);
				if(answer == JOptionPane.YES_OPTION){ //종료창에서 확인버튼 누른 경우
					System.exit(-1);
				}else{ //종료창에서 취소버튼 누른 경우
					f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}
	public static void main(String[] args) {
		IPnPort ipp = new IPnPort();
		ipp.init();
	}
}
class IPnPortHandler implements ActionListener {
	static String sIp;   //IP칸에 입력한 값
	static String sPort; //Port칸에 입력한 값
	static String sId;   //ID칸에 입력한 값
	CatchWord cw;
	IPnPort ipp;
	IPnPortHandler(IPnPort ipp){
		this.ipp = ipp;
	}
	IPnPortHandler(){}
	void test(){ //IP와 PORT번호를 제대로 입력했는지 테스트
		sIp = ipp.fIP.getText().trim();     //IP값 입력받기
		sPort = ipp.fPort.getText().trim(); //PORT값 입력받기
		sId = ipp.fId.getText().trim();     //ID값 입력받기
		if(sIp.length() == 0){
			JOptionPane.showMessageDialog(null, "IP를 입력해주세요", "알림", JOptionPane.WARNING_MESSAGE);
			ipp.fIP.setText("");
			ipp.fIP.requestFocus();
		}else if(sPort.length() == 0){
			JOptionPane.showMessageDialog(null, "PORT를 입력해주세요", "알림", JOptionPane.WARNING_MESSAGE);
			ipp.fPort.setText("");
			ipp.fPort.requestFocus();
		}else if(sId.length() == 0){
			JOptionPane.showMessageDialog(null, "ID를 입력해주세요", "알림", JOptionPane.WARNING_MESSAGE);
			ipp.fId.setText("");
			ipp.fId.requestFocus();
		}else{
			cw = new CatchWord();
			cw.init();
			ipp.f.setVisible(false);
		}
	}
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == ipp.fIP){			
			ipp.fPort.requestFocus();
			test();
		}else if(obj == ipp.fPort){			
			ipp.fId.requestFocus();
			test();
		}else if(obj == ipp.fId){
			ipp.bConfirm.requestFocus();
			test();
		}else if(obj == ipp.bConfirm){ //메세지 창에 보이기
			test();
		}else if(obj == ipp.bManual){
			String intro = "<게임설명>\n1. IP, PORT, ID를 입력하세요.\n2. 게임 입장을 하신 후, 상대방이 들어올 때까지 대기해주세요.\n3. 상대방이 들어오고 5초 후에 게임이 시작됩니다.\n4. 출제자는 상단 문제인 단어를 보고 정답자에게 힌트를 줍니다.\n5. 정답자는 입력하는 값이 정답이 아닌 경우에는 모두 오답으로 처리합니다.\n6. 제한시간 내에 최대한 많은 문제를 맞춰주세요.";
			JOptionPane.showMessageDialog(null, intro, "Introduction", JOptionPane.PLAIN_MESSAGE); //게임소개와 방법 추가(이미지도 가능)
		}
	}
}


