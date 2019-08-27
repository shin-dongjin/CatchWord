import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

class CatchWordQuestion extends Thread{
	int number;
	String fName = "./문제목록.txt";
	BufferedReader br;
	Random r = new Random();
	int n = 100;
	FileReader fr;
	String answer;

	CWClient cwc;
	CatchWordQuestion(CWClient cwc){
		try{
			fr = new FileReader(fName);
			br = new BufferedReader(fr);
		}catch(FileNotFoundException fe){
		}
		this.cwc = cwc; 
	}
	CatchWordQuestion(){
		try{
			fr = new FileReader(fName);
			br = new BufferedReader(fr);
		}catch(FileNotFoundException fe){
		}
	}
	void closeAll(){
		try{
			if(br != null) br.close();
			if(fr != null) fr.close();
		}catch(IOException ie){}
	}
	String show(){
		try{
			number = r.nextInt(n);
			int i=0;
			String answer = "";
			while(( answer = br.readLine())!=null){
				if(i==number) return answer;
				i++;
			}
		}catch(Exception e){}
		return answer;
	}
	public void run(){ //start()는 CWClient 클래스 run()에서 구현
		timer(1, 0);
	}
	void timer(int min, int sec){
		int answer = 0;
		do{
			try{
				cwc.cw.tfTimer.setText(" " + min + "분 : " + sec + "초");
				Thread.sleep(1000);
				if(sec == 0){
					min--;
					sec = 60;
				} sec--;
			}catch(InterruptedException ine){
				System.out.println("오류");
			}
			if(min == 0 && sec == 0){
				cwc.cw.tfTimer.setText(" " + (min = 0) + "분 : " + (sec = 0) + "초");
				cwc.code = cwc.code.trim();
				if(cwc.code.equals("#4")){
					 answer = JOptionPane.showConfirmDialog(null, "경기가 종료되었습니다.\n"+cwc.id+"님이 "+cwc.corAnsNum+"문제를 맞추셨습니다!", "게임종료", JOptionPane.PLAIN_MESSAGE);
				}else{
					 answer = JOptionPane.showConfirmDialog(null, "경기가 종료되었습니다.\n"+cwc.id+"님, 수고하셨습니다!", "게임종료", JOptionPane.PLAIN_MESSAGE);
				}
				if(answer == JOptionPane.OK_OPTION){
					System.exit(-1);
				}
			}
		} while(min >= 0 && sec >= 0);
	}
}
