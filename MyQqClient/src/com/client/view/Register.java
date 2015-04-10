package com.client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.client.function.ConnectToServer;
import com.common.User;

public class Register extends JFrame implements ActionListener{
	JButton send;
	JButton cancle;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	JPanel jp4;
	JPanel jp5;
	private String username;
	private String password;
	JTextField jtf_username;
	JPasswordField jpf_password;
	public static void main(String[] args){
		new Register();
	}
	public Register() {
		jtf_username = new JTextField(15);
		jpf_password = new JPasswordField(15);
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
		jp5.add(new JLabel("请输入注册信息:                                             "));
		jp1 = new JPanel();
		
		
		jp3.add(new JLabel("用户名："));
		jp3.add(jtf_username);

		
		jp4.add(new JLabel("密码：    "));
		jp4.add(jpf_password);

		BoxLayout lo = new BoxLayout(jp1, BoxLayout.Y_AXIS);
		jp1.setLayout(lo);
		jp1.add(jp5);
		jp1.add(jp3);
		jp1.add(jp4);
		
		jp2 = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
		cancle = new JButton("取消");
		send = new JButton("确定");
		cancle.addActionListener(this);
		send.addActionListener(this);
		jp2.add(cancle);
		jp2.add(send);

		this.setLayout(new BorderLayout());
		this.add(new JLabel(new ImageIcon("image/headLabel.jpg")),"North");
		this.add(jp1,"Center");
		this.add(jp2,"South");
		this.setSize(430,330);
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("QQ注册");
		this.setLocation(900,250);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==cancle) {
			this.setVisible(false);
		} else {
			System.out.println("向ConnectToServer发送注册信息");
			ConnectToServer registerCts = new ConnectToServer(); 
			User u = new User();
			u.setName(jtf_username.getText());
			u.setPassword(String.valueOf(jpf_password.getPassword()));
			if(registerCts.sendRegisterInfo(u.getName(),u.getPassword())) {
				JOptionPane.showMessageDialog(null, "注册成功！", "注册信息", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "注册失败！", "注册信息", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
}
