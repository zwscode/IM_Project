package com.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import javax.swing.*;

import com.client.function.ConnectToServer;
import com.client.function.ClientThreadManager;
import com.client.function.FriendListManager;
import com.client.function.UserManager;
import com.common.User;
import com.common.QqMessage;


public class Login extends JFrame {
	JLabel headLabel,lid,lpw,lrg,lfg,loginlabel;
	JPanel panel1,panel2,panel;
	JPanel innerPanel;
	JPanel southPanel;
	JButton btn;
	JTextField id;
	JPasswordField pw;
	JCheckBox remember, autologin;
	public static void main(String[] args){
		Login QqLogin = new Login();
	}
	public void closeFrame(){
		this.dispose();
	}
	public Login(){
		this.setTitle("QQ登陆");
		this.setLocation(600,300);
		//头部
		headLabel = new JLabel(new ImageIcon("image/headLabel.jpg"));
		//中部
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		panel1 = new JPanel();
		panel2 = new JPanel();
		lid = new JLabel("账号：",JLabel.CENTER);
		
		lpw = new JLabel("密码：",JLabel.CENTER);
		lrg = new JLabel("注册账号",JLabel.CENTER);
		
		lrg.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){//点击鼠标
				new Register();
			}
			public void mouseEntered(MouseEvent e){//鼠标进入  
			     lrg.setForeground(Color.decode("0x87CEFA"));
	
			 }  
			 public  void mouseExited(MouseEvent e){//鼠标移除  
				   lrg.setForeground(Color.blue);
			 }
		});
		
		lfg = new JLabel("找回密码",JLabel.CENTER);
		lfg.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){//鼠标进入  
			     lfg.setForeground(Color.decode("0x87CEFA"));
	
			 }  
			 public  void mouseExited(MouseEvent e){//鼠标移除  
				   lfg.setForeground(Color.blue);
			 }
		});
		lrg.setForeground(Color.blue);
		lfg.setForeground(Color.blue);
		
		id = new JTextField(15);
		pw = new JPasswordField(15);
		
		panel1.add(lid);
		panel1.add(id);
		panel1.add(lrg);
		panel2.add(lpw);
		panel2.add(pw);
		panel2.add(lfg);
		
		//南部
		southPanel = new JPanel();
		loginlabel = new JLabel(new ImageIcon("Image/login2.jpg"));
		
		//处理登陆 按钮的点击
		loginlabel.addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e){//点击鼠标
				 User u = new User();
				 u.setName((id.getText()).trim());
				 u.setPassword(String.valueOf(pw.getPassword()).trim());
				//将user添加到userManager进行管理
				 UserManager.add(u.getName(), u);
				 /*//如果该用户当前已经在线则不给他再登陆的机会
				 if(ClientThreadManager.getThread(u.getName()) != null) {
					 JOptionPane.showMessageDialog(null, "您已经登陆，请不要重复登录！", "alert", JOptionPane.ERROR_MESSAGE); 
					 return;
				 }*/
				 ConnectToServer clientToServer = new ConnectToServer();
				 if(clientToServer.sendLoginInfo(u.getName(),u.getPassword())) {
					 System.out.println("服务器返回信息 验证成功 :  "+u.getName()+"欢迎登陆！");
					 //请求好友列表
					 FriendList qq = new FriendList(u);
					 qq.setVisible(false);
					 FriendListManager.add(u.getName(), qq);
					 try {
						ObjectOutputStream oos = ClientThreadManager.getThread(u.getName()).getObjectOutputStream();
						QqMessage m = new QqMessage();
						m.setMessageType("RequestFriendList");
						m.setSender(u.getName());
						oos.writeObject(m);
					 } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					 }

					try {
						ObjectOutputStream oos3 = ClientThreadManager.getThread(u.getName()).getObjectOutputStream();
						QqMessage m3 = new QqMessage("RequestGroupList");
						m3.setSender(u.getName());
						oos3.writeObject(m3);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 try {
						 ObjectOutputStream oos2 = ClientThreadManager.getThread(u.getName()).getObjectOutputStream();
						 QqMessage m2 = new QqMessage("RequestOnline");
						 m2.setSender(u.getName());
						 oos2.writeObject(m2);
					 } catch (IOException e2){
						 e2.printStackTrace();
					 }
					 try {
						 ObjectOutputStream oos4 =  ClientThreadManager.getThread(u.getName()).getObjectOutputStream();
						 QqMessage m3 = new QqMessage("RequestUnreadMessages");
						 m3.setSender(u.getName());
						 oos4.writeObject(m3);
					 } catch (IOException e3){
						 e3.printStackTrace();
					 }
					 //关闭掉登陆界面
					 closeFrame();
				 } else {
					 System.out.println("服务器返回信息 验证失败");
					 JOptionPane.showMessageDialog(null, "账号或密码不正确", "alert", JOptionPane.ERROR_MESSAGE); 
				 }
			 }
			 public void mouseEntered(MouseEvent e){//鼠标进入  
			     loginlabel.setIcon(new ImageIcon("Image/login1.jpg"));  
			 }  
			 public  void mouseExited(MouseEvent e){//鼠标移除  
				   loginlabel.setIcon(new ImageIcon("Image/login2.jpg"));
			 }
			 
		});
		southPanel.add(loginlabel);
		panel.add(panel1);
		panel.add(panel2);
		panel.setBackground(Color.decode("0xebf2f9"));

		this.setLayout(new BorderLayout());
		this.add(southPanel,"South");
		this.add(headLabel, "North");
		this.add(panel,"Center");
		
		this.setSize(430,330);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
