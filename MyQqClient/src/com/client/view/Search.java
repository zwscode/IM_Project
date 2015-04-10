package com.client.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;

import com.client.function.ClientThreadManager;
import com.client.function.FriendListManager;
import com.client.function.UserManager;
import com.common.QqMessage;

public class Search extends JFrame implements ActionListener, MouseListener{
	FriendList fl;
	JTextField jtf;
	JButton bt_f,bt_g,bt_cg;
	JTextArea jta;
	JPanel top1,top2,top3,center;
	JLabel jl;
	JLabel[] jls;
	MenuItem menuitem1; 
	MenuItem menuitem2;
	PopupMenu pop1,pop2;
	private String chosenLabel;
	boolean flag = true;
	private String username;
	
	public Search(String username) {
		this.username = username;
		center = new JPanel(new GridLayout(50,1,4,4));
		pop1 = new PopupMenu();
		pop2 = new PopupMenu();
		this.add(pop1);
		this.add(pop2);
		menuitem1= new MenuItem();
		menuitem1.setLabel("Add as friend");
		menuitem1.addActionListener(this);
		menuitem2= new MenuItem();
		menuitem2.setLabel("Join group");
		menuitem2.addActionListener(this);
		pop2.add(menuitem2);
		pop1.add(menuitem1);
		jls = new JLabel[100];
		jl = new JLabel("输入查询信息：");
		jtf = new JTextField(20);
		bt_f = new JButton("搜索好友");
		bt_f.addActionListener(this);
		bt_g = new JButton("搜索QQ群");
		bt_g.addActionListener(this);
		bt_cg = new JButton("创建QQ群");
		bt_cg.addActionListener(this);
		top1 = new JPanel(new FlowLayout(FlowLayout.CENTER));

		top1.add(jl);
		top1.add(jtf);
		top2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		top2.add(bt_f);
		top2.add(bt_g);
		top2.add(bt_cg);
		top3 = new JPanel();
		BoxLayout box = new BoxLayout(top3,BoxLayout.Y_AXIS);
		top3.setLayout(box);
		top3.add(top1);
		top3.add(top2);
		jls = new JLabel[50];
		for(int i=0;i<50;i++) {
			jls[i]= new JLabel();
			jls[i].addMouseListener(this);
			center.add(jls[i]);
		}
		JScrollPane js = new JScrollPane(center);
		this.add(top3,"North");
		this.add(js,"Center");
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(400,500);
		this.setTitle(username+":搜索好友/QQ群");
		this.setLocation(700,300);
	}
	public void updateResults(String results) {
		if(results.trim().equals("")||results.equals(null)) {
			return;
		} else {
			String [] temp = results.split(" ");
			System.out.println("temp.length:"+temp.length);
			
			for(int i=0;i<temp.length;i++) {
				jls[i].setText(temp[i]);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == bt_f) {//如果点击搜索好友
			for(int i=0;i<50;i++) {
				jls[i].setText("");
			}
			flag = true;	//flag 为true表示结果是搜索好友的结果
			QqMessage m = new QqMessage("SearchForFriend");
			m.setSender(username);
			m.setMessage(jtf.getText());
			ObjectOutputStream oos = ClientThreadManager.getThread(username).getObjectOutputStream();
			try {
				oos.writeObject(m);
				oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(e.getSource() == bt_g) { //如果点击搜索QQ群
			flag = false;
			//刷新显示区
			for(int i=0;i<50;i++) {
				jls[i].setText("");
			}
			QqMessage m = new QqMessage("SearchForGroup");
			m.setSender(username);
			m.setMessage(jtf.getText());
			ObjectOutputStream oos = ClientThreadManager.getThread(username).getObjectOutputStream();
			try {
				oos.writeObject(m);
				oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(e.getSource() == bt_cg) { //如果点击创建QQ群
			/*******************************
			 * 创建QQ群的代码
			 * 
			 * */
			new RegisterGroup(username);
			
		} else if(e.getSource() == menuitem1) {	//单击了添加好友的弹出菜单
			if(UserManager.get(username).getFriends().contains(chosenLabel)) {
				JOptionPane.showMessageDialog(null, "该用户已经是你的好友，还加你妹！", "alert", JOptionPane.ERROR_MESSAGE); 
				return;
			} else if (UserManager.get(username).getName().equals(chosenLabel)) {
				JOptionPane.showMessageDialog(null, "不要调皮，加自己为好友干什么啊！", "alert", JOptionPane.ERROR_MESSAGE); 
				return;
			}	else {
				QqMessage m = new QqMessage("RequestAddFriend");
				m.setSender(username);
				m.setReceiver(chosenLabel);
				ObjectOutputStream oos = ClientThreadManager.getThread(username).getObjectOutputStream();
				
				try {
					oos.writeObject(m);
					oos.flush();
					oos.reset();
					QqMessage m1 = new QqMessage("RequestFriendList");
					m1.setSender(username);
					oos.writeObject(m1);
					oos.flush();
					oos.reset();
					
					QqMessage m2 = new QqMessage("RequestOnline");
					m2.setSender(username);
					oos.writeObject(m2);
					oos.flush();
					oos.reset();
					
					/*if() {	//如果他加的这个好友在线那么这个好友也要更新他的好友列表
						//在线的被加好友的家伙也要即时更新好友列表
						ObjectOutputStream oos1 = ClientThreadManager.getThread(chosenLabel).getObjectOutputStream();
						JOptionPane.showMessageDialog(this, "被加的家伙你也要更新你的好友列表啊");
						m1.setSender(chosenLabel);
						m1.setMessageType("RequestFriendList");
						oos1.writeObject(m1);
						oos1.flush();
						oos1.reset();
						
						m2.setSender(chosenLabel);
						m2.setMessageType("RequestFriendList");
						oos1.writeObject(m2);
						oos1.flush();
						oos1.reset();
					} else {
						JOptionPane.showMessageDialog(this, "这SB不在线");
					}*/
					
					
					
					JOptionPane.showMessageDialog(null, "添加好友成功！", "alert", JOptionPane.PLAIN_MESSAGE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == menuitem2) {	//单击了加入QQ群的菜单
			FriendList fl = FriendListManager.get(username);
			for(int i=0;i<fl.groupLabels.length;i++) {
				if(fl.groupLabels[i].getText().equals(chosenLabel)) {
					JOptionPane.showMessageDialog(null, "你TM已经是该群的成员，别加了！", "alert", JOptionPane.ERROR_MESSAGE); 
					return;
				}
			}
			QqMessage m = new QqMessage("RequestJoinGroup");
			m.setSender(username);
			m.setReceiver(chosenLabel);
			m.setGroup(chosenLabel);
			ObjectOutputStream oos = ClientThreadManager.getThread(username).getObjectOutputStream();
			try {
				oos.writeObject(m);
				oos.flush();
				oos.reset();
				
				QqMessage m1 = new QqMessage("RequestGroupList");
				m1.setSender(username);
				oos.writeObject(m1);
				oos.flush();
				oos.reset();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getButton()==MouseEvent.BUTTON3 && flag == true) {//添加好友
			chosenLabel = ((JLabel)e.getSource()).getText();
			pop1.show(e.getComponent(), e.getX(), e.getY());
		} else if (e.getButton()==MouseEvent.BUTTON3 && flag == false) {
			//加入QQ群
			chosenLabel = ((JLabel)e.getSource()).getText();
			pop2.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
