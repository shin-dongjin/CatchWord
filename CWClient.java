import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class CWClient extends Thread {
	BufferedReader brK = new BufferedReader(new InputStreamReader(System.in));
	Socket s;
	String ip = "127.0.0.1"; 
	String port = "6000";
	String id = "홍길동";
	OutputStream os;
	InputStream is; 
	DataInputStream dis;
	DataOutputStream dos;
	String msg;
	String rQuestion; //받아온 문제
	static String code;
	int corAnsNum; //정답갯수

	CatchWordQuestion cwq;
	CatchWordQuestion cwq2 = new CatchWordQuestion(this);

	CatchWord cw;
	CWClient(CatchWord cw){
		this.cw = cw;
	}
    void init(){
		try{
			ip = IPnPortHandler.sIp; 
			port = IPnPortHandler.sPort; 
			id = IPnPortHandler.sId; 
			int iPort = Integer.parseInt(port);
			s = new Socket(ip, iPort);
			cw.tp.append("서버와 연결 성공!"+"\n");
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			start();
		}catch(IllegalArgumentException iae){
			JOptionPane.showConfirmDialog(null, "포트를 찾을 수 없습니다.", "경고", JOptionPane.CLOSED_OPTION);
			System.exit(-1);
		}catch(UnknownHostException ue){
			JOptionPane.showConfirmDialog(null, "IP를 찾을 수 없습니다!", "경고", JOptionPane.CLOSED_OPTION);
			System.exit(-1); 
		}catch(IOException ie){
			JOptionPane.showConfirmDialog(null, "이미 게임이 시작되었습니다.", "경고", JOptionPane.CLOSED_OPTION);
			System.exit(-1);
		}
	}
	void correct(String str){ //정답, 오답, PASS를 판별
		try{
			str = str.trim();
			int idx2 = str.indexOf(">>"); //추가
			if(str.substring(idx2+2).equals(rQuestion)){
				cwq = new CatchWordQuestion();
				cw.tp.append("--------------------------"+"\n");
				cw.tp.append("정답: "+rQuestion+"\n");
				String cName = str.substring(0, idx2);
				cw.tp.append(cName+"님이 정답을 맞추셨습니다!\n");
				cw.tp.append("--------------------------"+"\n");
				corAnsNum++;
				String q = cwq.show().trim();
				dos.writeUTF(q+"#1");
			}else if(str.equals("pass")){
				cwq = new CatchWordQuestion();
				dos.writeUTF("--------------------------"+"#5");
				dos.writeUTF("정답: "+rQuestion+"#5");
				dos.writeUTF("Pass 하셨습니다!"+"#5");
				dos.writeUTF("--------------------------"+"#5");
				String q = cwq.show().trim();
				dos.writeUTF(q+"#1");
			}else{
				cw.tp.append(str+"\n");
			}
		}catch(IOException ie){}
	}
	/*
		CODE
		#1 : 문제단어
		#2 : 채팅
		#3 : 출제자
		#4 : 정답자
		#5 : PASS
	*/
	public void run(){ //socket -> monitor 
		int i = 1; //단 한번 타이머가 실행되도록 임의로 세워준 변수
		try{
			code = dis.readUTF();
			dos.writeUTF(code);
			while(true){
				String msg = dis.readUTF();
				msg = msg.trim();
				int idx = msg.lastIndexOf("#");
				int idx2 = msg.indexOf(">>"); //추가
				if(idx != -1){
					String check = msg.substring(idx+1);
					switch(check)
					{
						case "1": if(code.equals("#3")) cw.question.setText(msg.substring(0,idx)+"\n");
								  if(i==1){ //단 한번만 실행되도록 제어
									  cw.tfAnswer.setEditable(true);
									  cw.bPass.setEnabled(true);
									  cwq2.start();
									  i++;
								  }
								  rQuestion = msg.substring(0,idx);
								break;
						case "2": cw.tp.append(msg.substring(0,idx)+"\n");
								  break;
						case "3": int cen = msg.substring(0,idx).indexOf(rQuestion);
								  if(cen!=-1){	
									cw.tp.append("출제자가 정답이 포함된 말을 하셨습니다."+"\n");
									break;
								  }else{
									cw.tp.append("(출제자)"+msg.substring(0,idx)+"\n");
									break;
								  }
						case "4": correct(msg.substring(0,idx));
								break;
						case "5": cw.tp.append(msg.substring(0,idx)+"\n");
								break;
					}
				}
				cw.tp.setCaretPosition(cw.tp.getDocument().getLength());
			}
		}catch(IOException ie){
			cw.tp.append("상대방이 게임을 종료하여 서버를 종료합니다. \n5초 후에 게임을 종료합니다."); //추가
			try{
				Thread.sleep(5000);
			}catch(Exception e){}
		}finally{
			closeAll();
			System.exit(-1);
		}
	}
	public void speak(){ 
		try{
			dos.writeUTF(id+">>"+msg+code);
			dos.flush();
		}catch(IOException ie){}
	}
	void closeAll(){
		try{
			if(dis != null) dis.close();
			if(dos != null) dos.close();
			if(is != null) is.close();
			if(os != null) os.close();
			if(s != null) s.close();
		}catch(IOException ie){}
	}
}

