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
	JButton bConfirm; //Ȯ�ι�ư
	JButton bManual; //���Ӽ��� ��ư

	void init(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(Exception e){}
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		setUI();
	}
	void setUI(){
		//(1) f ����
		f = new JFrame();
		cp = f.getContentPane();
		cp.setLayout(new BorderLayout());

		IPnPortHandler h = new IPnPortHandler(this); //Handler��ü ����
		
		Color c1 = new Color(184, 207, 229);
		Color c2 = new Color(99, 130, 191);
		
		//(2) pCenter ����(ID, PORT, ID �޴� �ʵ�)
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

		//(3) pSouth ����(��������� ���Ӽ��� ��ư �ʵ� )
		JPanel pSouth = new JPanel(new BorderLayout());
		pSouth.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY)));
		JPanel pSouthInAnswer = new JPanel();
		pSouthInAnswer.setBackground(new Color(255,255,204));
		pSouthInAnswer.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, c1, c2)));
		bConfirm = new JButton("���� ����");
		bConfirm.setBackground(new Color(255,204,000));
		bManual = new JButton("���� ����");
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
				int answer = JOptionPane.showConfirmDialog(f, "�����ұ��?", "����", JOptionPane.OK_CANCEL_OPTION);
				if(answer == JOptionPane.YES_OPTION){ //����â���� Ȯ�ι�ư ���� ���
					System.exit(-1);
				}else{ //����â���� ��ҹ�ư ���� ���
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
	static String sIp;   //IPĭ�� �Է��� ��
	static String sPort; //Portĭ�� �Է��� ��
	static String sId;   //IDĭ�� �Է��� ��
	CatchWord cw;
	IPnPort ipp;
	IPnPortHandler(IPnPort ipp){
		this.ipp = ipp;
	}
	IPnPortHandler(){}
	void test(){ //IP�� PORT��ȣ�� ����� �Է��ߴ��� �׽�Ʈ
		sIp = ipp.fIP.getText().trim();     //IP�� �Է¹ޱ�
		sPort = ipp.fPort.getText().trim(); //PORT�� �Է¹ޱ�
		sId = ipp.fId.getText().trim();     //ID�� �Է¹ޱ�
		if(sIp.length() == 0){
			JOptionPane.showMessageDialog(null, "IP�� �Է����ּ���", "�˸�", JOptionPane.WARNING_MESSAGE);
			ipp.fIP.setText("");
			ipp.fIP.requestFocus();
		}else if(sPort.length() == 0){
			JOptionPane.showMessageDialog(null, "PORT�� �Է����ּ���", "�˸�", JOptionPane.WARNING_MESSAGE);
			ipp.fPort.setText("");
			ipp.fPort.requestFocus();
		}else if(sId.length() == 0){
			JOptionPane.showMessageDialog(null, "ID�� �Է����ּ���", "�˸�", JOptionPane.WARNING_MESSAGE);
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
		}else if(obj == ipp.bConfirm){ //�޼��� â�� ���̱�
			test();
		}else if(obj == ipp.bManual){
			String intro = "<���Ӽ���>\n1. IP, PORT, ID�� �Է��ϼ���.\n2. ���� ������ �Ͻ� ��, ������ ���� ������ ������ּ���.\n3. ������ ������ 5�� �Ŀ� ������ ���۵˴ϴ�.\n4. �����ڴ� ��� ������ �ܾ ���� �����ڿ��� ��Ʈ�� �ݴϴ�.\n5. �����ڴ� �Է��ϴ� ���� ������ �ƴ� ��쿡�� ��� �������� ó���մϴ�.\n6. ���ѽð� ���� �ִ��� ���� ������ �����ּ���.";
			JOptionPane.showMessageDialog(null, intro, "Introduction", JOptionPane.PLAIN_MESSAGE); //���ӼҰ��� ��� �߰�(�̹����� ����)
		}
	}
}


