import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

class CWHandler implements ActionListener{
	CatchWord cw;
	CWHandler(CatchWord cw){
		this.cw = cw;
	}
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj==cw.bPass){
			cw.cwc.correct("pass");
		}
		if(obj==cw.tfAnswer){			
			String line = cw.tfAnswer.getText();
			cw.cwc.msg = line;
			cw.cwc.speak();
			cw.tfAnswer.setText("");
			cw.tp.setCaretPosition(cw.tp.getDocument().getLength());
		}
	}
}