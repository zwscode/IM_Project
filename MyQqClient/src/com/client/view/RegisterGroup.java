package com.client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;

import com.client.function.ClientThreadManager;
import com.client.function.ConnectToServer;
import com.common.QqMessage;
import com.common.User;

public class RegisterGroup extends JFrame implements ActionListener{
	JButton send;
	JButton cancle;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	JPanel jp4;
	JPanel jp5;
	private String username;
	private String groupname;
	JTextField jtf_group;
	JPasswordField jpf_password;

	public RegisterGroup(String username) {
		this.username = username;
		jtf_group = new JTextField(15);
		jp3 = new JPanel();
		jp5 = new JPanel();
		jp5.add(new JLabel("请输入QQ群注册信息:                                             "));
		jp1 = new JPanel();
		
		
		jp3.add(new JLabel("群名称："));
		jp3.add(jtf_group);


		BoxLayout lo = new BoxLayout(jp1, BoxLayout.Y_AXIS);
		jp1.setLayout(lo);
		jp1.add(jp5);
		jp1.add(jp3);
		
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
		this.setTitle("QQ群创建");
		this.setLocation(900,250);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==cancle) {
			this.setVisible(false);
		} else {
			groupname = jtf_group.getText();
			System.out.println("向ServerClientThread发送QQ群注册信息");
			QqMessage m = new QqMessage("RegisterGroup");
			m.setSender(username);
			m.setGroup(groupname);
			ObjectOutputStream oos = ClientThreadManager.getThread(username).getObjectOutputStream();
			try {
				oos.writeObject(m);
				oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.setVisible(false);
			/*if(registerCts.sendRegisterInfo(u.getName(),u.getPassword())) {
				JOptionPane.showMessageDialog(null, "注册成功！", "注册信息", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "注册失败！", "注册信息", JOptionPane.ERROR_MESSAGE);
			}*/
		}
	}
	
	
}
