package com.client.view;
import javax.swing.*;
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
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class GroupChat extends JFrame implements ActionListener{

	JTextPane textPane;
	JTextArea textArea;
	JButton send;
	JButton cancle;
	JPanel jp;
	JPanel mainPanel,memberPanel;
	JLabel [] labels;
	private String group;
	private String ownerId;

	public void updateMembersList(String members) {
		if(members.equals(null)||members.trim().equals("")){
			System.out.println("该组没有成员");
			return;
		}
		String[] temp = members.split(" ");
		/*System.out.println("GroupChatClass: Updating MembersList");
		for(int i=0;i<temp.length;i++)
		{
			System.out.println("Writing member: "+ temp[i]);
			labels[i].setText(temp[i]);
		}*/
		labels[0].setText("成员信息：");
		labels[0].setEnabled(true);
		System.out.println("GroupChatClass: Updating MembersList");
		for(int i=1;i<temp.length+1;i++)
		{
			labels[i].setText(temp[i-1]);
			labels[i].setEnabled(true);
		}

		this.setVisible(true);
	}
	public GroupChat(String ownerId, String group){
		this.group = group;
		this.ownerId = ownerId;
		jp = new JPanel(new FlowLayout(FlowLayout.LEFT,15,10));
		mainPanel = new JPanel(new BorderLayout());
		textPane = new JTextPane();
		JScrollPane sp1 = new JScrollPane(textPane);
		sp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(sp1,"Center");
		memberPanel = new JPanel(new GridLayout(50,1,4,4));
		labels = new JLabel[50];
		
		for(int i = 0; i<50; i++) {
			labels[i] = new JLabel("                                   ");
			labels[i].setEnabled(true);
			memberPanel.add(labels[i]);
		}
		
		JScrollPane sp = new JScrollPane(memberPanel);
		sp.setSize(100,250);
		
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainPanel.add(sp,"East");
		textPane.setEditable(false);
		textArea = new JTextArea(4,25);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		cancle = new JButton("取消");
		send = new JButton("发送");
		cancle.addActionListener(this);
		send.addActionListener(this);
		jp.add(textArea);
		jp.add(cancle);
		jp.add(send);
		this.add(mainPanel,"Center");
		this.add(jp,"South");
		this.setTitle(ownerId+"的QQ群"+group);
		this.setSize(500,350);
		this.setVisible(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		//向服务器请求小组中的成员
	}
	
	public void requestMembers() {
		try {
			ObjectOutputStream oos = ClientThreadManager.getThread(ownerId).getObjectOutputStream();
			QqMessage m = new QqMessage("RequestGroupMembers");
			m.setSender(ownerId);
			m.setMessage(group);
			m.setGroup(group);
			oos.writeObject(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			QqMessage info = new QqMessage();
			info.setMessageType("GroupMessage");
			info.setSender(this.getOwnerId());
			info.setGroup(this.getGroup());
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
			textArea.setText("");
			//设置好info所包含的各项属性后就可以向服务器发送了。
			try {
				//通过SocketThreadManager获取登录时创建的socket，使用该socket向服务器发送info
				ObjectOutputStream oos = ClientThreadManager.getThread(this.getOwnerId()).getObjectOutputStream();
				oos.writeObject(info);
				//显示要发送的的时间和信息
				//showMessage(info);
				/*insertDocument(info.getSender()+" "+info.getTime()+"\n",Color.BLUE,true,13);
				insertDocument("  "+info.getMessage()+"\n",Color.BLACK,false,12);*/
			} catch(Exception e1) {
				e1.printStackTrace();
			}
			
		}
	}
	public String getGroup() {
		return group;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void showMessage(QqMessage m) {
		insertDocument(m.getSender()+" "+m.getTime()+"\n",Color.BLUE,true,13);
		insertDocument("  "+m.getMessage()+"\n",Color.BLACK,false,12);
	}
	/*public void run() {
		while(true) {
			//读取服务器发送来的信息
			try {
				//读取
				ObjectInputStream ois = new ObjectInputStream((ClientSocketManager.get(this.ownerId)).getSocket().getInputStream());
				Info info2 = (Info)ois.readObject();
				//显示
				if (info2.getReceiver().equals(this.ownerId)) {
					insertDocument(info2.getSender()+" "+info2.getTime()+"\n",Color.BLUE,true,13);
					insertDocument("  "+info2.getMessage()+"\n",Color.BLACK,false,12);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	
}
