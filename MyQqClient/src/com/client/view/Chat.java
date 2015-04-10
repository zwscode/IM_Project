package com.client.view;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.client.function.ClientThreadManager;
import com.client.function.ConnectToServer;
import com.common.QqMessage;

import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Chat extends JFrame implements ActionListener{

	Color fontColor= new Color(0xff000000);
	int fontSize = 12;
	QqMessage info;
	JTextPane textPane;
	JTextArea textArea;
	JButton send;
	JButton cancle, selectFile, selectPicture, selectColor;
	String[] sizeString = {"10","11","12","13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"};
	JComboBox textSizeBox;
	JPanel jp;
	private String friend;
	private String ownerId;

	public Chat(String ownerId, String friend){
		info = new QqMessage();
		this.friend = friend;
		this.ownerId = ownerId;
		jp = new JPanel();
		JPanel btnjp = new JPanel();
		textPane = new JTextPane();
		textPane.setEditable(false);
		textArea = new JTextArea(4, 25);
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		cancle = new JButton("取消");
		send = new JButton("发送");
		jp.setLayout(new BorderLayout(0, 0));
		cancle.addActionListener(this);
		send.addActionListener(this);
		JPanel bundle = new JPanel();
		bundle.setLayout(new GridLayout(0, 5, 0, 0));

		textSizeBox = new JComboBox(sizeString);

		textSizeBox.addActionListener(sizeActionListener);

		bundle.add(textSizeBox);
		selectColor = new JButton("字体颜色");
		bundle.add(selectColor);
		selectPicture = new JButton("发送图片");
		bundle.add(selectPicture);
		selectPicture.addActionListener(sendPhoto);
		selectFile = new JButton("发送邮件");
		bundle.add(selectFile);
		selectFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new mailUI();
				
			}
		});
		selectColor.addActionListener(colorActionListener);

		jp.add(textScrollPane, BorderLayout.CENTER);
		jp.add(btnjp, BorderLayout.SOUTH);
		jp.add(bundle, BorderLayout.NORTH);
		GroupLayout gl_btnjp = new GroupLayout(btnjp);

		gl_btnjp.setHorizontalGroup(gl_btnjp.createParallelGroup(
				Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_btnjp.createSequentialGroup()
						.addContainerGap(328, Short.MAX_VALUE)
						.addComponent(cancle, GroupLayout.PREFERRED_SIZE, 69,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(send, GroupLayout.PREFERRED_SIZE, 69,
								GroupLayout.PREFERRED_SIZE).addGap(12)));
		gl_btnjp.setVerticalGroup(gl_btnjp.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_btnjp.createParallelGroup(Alignment.BASELINE)
						.addComponent(cancle).addComponent(send)));

		btnjp.setLayout(gl_btnjp);
		getContentPane().add(new JScrollPane(textPane), "Center");
		getContentPane().add(jp, "South");
		this.setTitle(ownerId + " -> " + friend);
		this.setSize(500, 350);
		this.setVisible(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	//向textPane添加文字的方法
	 public void insertDocument(String text , Color textColor, boolean bold, int size){
	  SimpleAttributeSet set = new SimpleAttributeSet();
	  StyleConstants.setForeground(set, textColor);//设置文字颜色
	  StyleConstants.setFontSize(set, size);//设置字体大小
	  StyleConstants.setBold(set, bold);//设置粗体与否
	  Document doc = textPane.getStyledDocument();
	  try {
	   doc.insertString(doc.getLength(), text, set);//插入文字
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	 }
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==cancle) {
			this.setVisible(false);
		}
		if(e.getSource()==send) {
			
			sendMessage();
		}
	}
	private String getFriend() {
		return friend;
	}
	private String getOwnerId() {
		return ownerId;
	}
	//在textPane中显示图片的方法！！！！！！
	//
	
	public void showMessage(QqMessage m) {
		insertDocument(m.getSender()+" "+m.getTime()+"\n",Color.BLUE,true,13);
		insertDocument("  "+m.getMessage()+"\n",m.getFontColor(),false,m.getFontSize());
		if(m.getIconMessage()!=null) {
			
			System.out.println("我tmd要显示图片了！！");
			Document doc = textPane.getStyledDocument();
			textPane.setCaretPosition(doc.getLength());
			textPane.insertIcon(m.getIconMessage());
			insertDocument("\n",Color.black,false,12);
		}
	}
	
	ActionListener colorActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			fontColor = JColorChooser.showDialog(null, "Choose a Color",
					textArea.getForeground());
			if (fontColor != null) {
				textArea.setForeground(fontColor);
				info.setFontColor(fontColor);
			}
				
		}
	};

	ActionListener sizeActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String si = (String) textSizeBox.getSelectedItem();
			fontSize = Integer.valueOf(si).intValue();
			textArea.setFont(new Font("宋体", Font.PLAIN, fontSize));
			info.setFontSize(fontSize);
		}
	};

	ActionListener sendPhoto = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				String name = chooser.getSelectedFile().getPath();
				File url = new File(name);
				try {
					BufferedImage bfImage = ImageIO.read(url);
					ImageIcon icon = new ImageIcon(bfImage);
					info.setIconMessage(icon);
					sendMessage();
					//发送一次之后将IconMessage置为空
					info.setIconMessage(null);
				} catch (IOException e1) {

				}

			}
		}
	};
	
	ActionListener sendFile = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				String name = chooser.getSelectedFile().getPath();
				File url = new File(name);
			}
		}
	};
	
	public void sendMessage(){
		info.setMessageType("Message");
		info.setSender(this.getOwnerId());
		info.setReceiver(this.getFriend());
		info.setMessage(textArea.getText());
		//获取正确的日期并显示出来
		Calendar cal1 = Calendar.getInstance();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00")); 
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.text.SimpleDateFormat sdf2 = new SimpleDateFormat("kk:mm:ss");
		//String time = new java.util.Date().toString(); 
		String time = sdf.format(cal1.getTime());
		switch(cal1.get(java.util.Calendar.DAY_OF_WEEK)) {
		case java.util.Calendar.SUNDAY:
			time+=" 星期日";
			break;
		case java.util.Calendar.MONDAY:
			time+=" 星期一";
			break;
		case java.util.Calendar.TUESDAY:
			time+=" 星期二";
			break;
		case java.util.Calendar.WEDNESDAY:
			time+=" 星期三";
			break;
		case java.util.Calendar.THURSDAY:
			time+=" 星期四";
			break;
		case java.util.Calendar.FRIDAY:
			time+=" 星期五";
			break;
		case java.util.Calendar.SATURDAY:
			time+=" 星期六";
			break;
		}
		time += " ";
		time += sdf2.format(cal1.getTime());
		info.setTime(time);
		textArea.setText("");//设置好info所包含的各项属性后就可以向服务器发送了。
		try {
			//通过SocketThreadManager获取登录时创建的socket，使用该socket向服务器发送info
			ObjectOutputStream oos2 = ClientThreadManager.getThread(this.getOwnerId()).getObjectOutputStream();
			oos2.writeObject(info);
			oos2.flush();
			//不知道为什么要reset！！！不Reset不行啊！！！！！！！！！！！尼玛啊！逗我呢！！！！
			oos2.reset();
			//显示要发送的的时间和信息
			showMessage(info);
			/*insertDocument(info.getSender()+" "+info.getTime()+"\n",Color.BLUE,true,13);
			insertDocument("  "+info.getMessage()+"\n",Color.BLACK,false,12);*/
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
}
