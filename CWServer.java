import java.io.*;
import java.util.*;
import java.net.*;

class CWServer{
	static ServerSocket ss;
	int port = 6000;
	CWModul cm;
	static Vector<CWModul> v = new Vector<CWModul>();

	CWServer(){
		try{
			ss = new ServerSocket(port);
			InClient t = new InClient();
			System.out.println("��Ʈ��ȣ "+port+"������ ���� �����...");
			Socket s = ss.accept();
			cm = new CWModul(s,v,"#3");
			cm.start();
			v.add(cm);
			t.start();
		}catch(IOException ie){
			System.out.println("�̹� ��� ���� ��Ʈ�Դϴ�.");
		}
	}
	public static void main(String []args){
		new CWServer();
	}
}

class CWModul extends Thread{
	CatchWordQuestion cwq;
	Socket s;
	static Vector<CWModul> v;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	String chatId;
	String gameCode; 
	String code;

	CWModul(Socket s, Vector<CWModul> v, String code){
		this.s = s;
		this.v = v;
		this.code = code;
		try{
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException ie){}
	}
	public void run(){
		try{
			dos.writeUTF(code);
			System.out.println("PLAYER�� ������ (�ο�:"+v.size()+")!!");
			broadcast("PLAYER�� �����ϼ̽��ϴ�. (�ο�:"+v.size()+")!!#2");			
			gameCode = dis.readUTF();	
			if(gameCode.equals("#4 ")){
				broadcast("��� �����ϼ̽��ϴ�. 5�� �Ŀ� ������ ���۵˴ϴ�!#2");
				sleep(5000);
				broadcast("���� ����#2");
				cwq = new CatchWordQuestion();
				String q = cwq.show().trim();
				broadcast(q+"#1");
			}		
			while(true){
				String msg = dis.readUTF();
				int idx = msg.lastIndexOf("#");
				if(idx != -1){
					String line = msg.substring(idx+1);
					line = line.trim();
					broadcast(msg);
				}
			}
		}catch(IOException ie){
			v.remove(this);
			System.out.println("PLAYER�� ������ (�ο�:"+v.size()+")!!");
			broadcast("������ �����Ͽ����ϴ�. (�ο�:"+v.size()+")!!#2");
			if(v.size() == 1) System.exit(-1);
            closeAll();
		}catch(InterruptedException ie){}
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
	void broadcast(String msg){
		for(CWModul cm : v){
			try{
				cm.dos.writeUTF(msg);
				cm.dos.flush();
			}catch(IOException ie){}
		}
	}
}
class InClient extends Thread{
	CWModul cm;
	int port = 6000;
	int count = 1;
	void init(){
		try{
			while(count < 1){ //PLAYER�� 2����� �޵���
				System.out.println("���� PLAYER ���� �����...");
				Socket s = CWServer.ss.accept();
				cm = new CWModul(s,CWServer.v,"#4");
				cm.start();
				CWServer.v.add(cm);
				count++;
			}
			Socket s = CWServer.ss.accept();
			cm = new CWModul(s,CWServer.v,"#4 ");
			cm.start(); //���� ��ŸƮ
			CWServer.v.add(cm);
			System.out.println("�����Ϸ�Ǿ����ϴ�.");
			CWServer.ss.close();
		}catch(IOException ie){
			System.out.println("Sever Socket �����߻�");
		}
	}
	public void run(){
		init();
	}
}