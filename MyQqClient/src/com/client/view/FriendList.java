/**
 * 我的好友列表,(也包括陌生人，添加好友/QQ群)
 */
package com.client.view;

import com.client.function.*;
import com.common.QqMessage;
import com.common.User;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
public class FriendList extends JFrame implements ActionListener,MouseListener{

	private User owner;
	//处理第一张卡片.
	private boolean flag = false;
	JPanel friendCardPanel,friendPanel,jphy3;
	JButton friendButton,groupButton,SearchButton;
	JScrollPane friendScrollPane;
	JLabel []friendsLabelArray;
	String ownerId;
	
	//处理第二张卡片(QQ群).
	JPanel groupCardPanel,groupPanel,northButtonsPanel;
	JButton friendButton2,groupButton2,SearchButton2;
	JScrollPane groupScrollPane;
	
	public JLabel [] groupLabels;
	//把整个JFrame设置成CardLayout
	CardLayout cl;
	
	public User getQqOwner(){
		return owner;
	}
	//朋友QQ下线的时候修改相应的JLabel状态为不在线状态
	public void updateOffline(QqMessage m) {
		System.out.println("Updating Offline Friends");
		String offlineFriend = m.getSender();
		System.out.println("Changing online state" + offlineFriend);
		for(int i=0; i<owner.getFriends().size();i++) {
			if(friendsLabelArray[i].getText().equals(offlineFriend)) {
				friendsLabelArray[i].setEnabled(false);
				break;
			}
		}
		return;
	}
	//添加好友
	public void searchFriend(String username) {
		Search s = new Search(username);
		SearchManager.add(username, s);
	}
	//删除好友
	public void deleteFriend(String name) {
		
	}
	//更新在线的好友情况
	public void upateOnline(QqMessage m)
	{
		System.out.println("Updating Online Friends");
		if(m.getMessage().equals(null)||m.getMessage().trim().equals("")){
			System.out.println("FriendList:该用户没有在线好友");
			return;
		}
		String onlineFriends[]=m.getMessage().split("\\s+");
		
		for(int i=0;i<onlineFriends.length;i++)
		{
			System.out.println("Changing online state" + onlineFriends[i]);
			/*friendsLabelArray[Integer.parseInt(onlineFriend[i])-1].setEnabled(true);*/
			if(owner.getFriends().contains(onlineFriends[i])){
				for(int i1=0; i1<owner.getFriends().size();i1++) {
					if(friendsLabelArray[i1].getText().equals(onlineFriends[i])) {
						//如果返回String与改Label Text一致，则该好友是上线状态的
						friendsLabelArray[i1].setEnabled(true);
					}
				}
			}
		}
		return;
	}
	public void updateGroupsList(String[] groups) {
		System.out.println("FriendListClass: Updating GroupsList");
		if(groups.length == 0){
			System.out.println("FriendList:该用户没有QQ群");
			return;
		}
		int j=0;
		for(int i=0;i<groups.length;i++)
		{
			if(groups[i].equals("")||groups[i].equals(null)||groups[i].equals(" ")) {
				j++;
			}
			groupLabels[i-j].setText(groups[i]);
			
		}
	}
	
	//更新好友列表 这个方法在User的updateFriends中被调用
	 
	public void updateFriendsList(String[] friends) {
		System.out.println("FriendListClass: Updating FriendsList");
		if(friends.length == 0){
			System.out.println("FriendList:该用户没有好友");
			return;
		}
		int j=0;
		for(int i=0;i<friends.length;i++)
		{
			
			if(friends[i].equals("")||friends[i].equals(null)||friends[i].equals(" ")) {
				j++;
			}
			friendsLabelArray[i-j].setText(friends[i]);
			
		}
	}
	
	public FriendList(User user)
	{
		this.addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent e){
				if(ClientThreadManager.getThread(ownerId).getSocket().isConnected())
				try {
					ObjectOutputStream oos = ClientThreadManager.getThread(ownerId).getObjectOutputStream();
					QqMessage endInfo = new QqMessage("ClientThreadClosing");
					endInfo.setSender(ownerId);
					oos.writeObject(endInfo);
					oos.flush();
					System.out.println("Friendlist发送QqMessage: endInfo Type:"+endInfo.getMessageType());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			 }
		});
		this.owner = new User(user);
		this.ownerId=owner.getName();
		//处理第一张卡片(显示好友列表)
		friendButton=new JButton("我的好友");
		groupButton=new JButton("QQ群");
		groupButton.addActionListener(this);
		
		SearchButton=new JButton("添加好友/QQ群");
		SearchButton.addActionListener(this);
		friendCardPanel=new JPanel(new BorderLayout());
		//假定有50个好友
		friendPanel=new JPanel(new GridLayout(50,1,4,4));
		
		friendsLabelArray =new JLabel[50];
		int i = 0;
		while(i<50) {
			friendsLabelArray[i] = new JLabel();
			friendsLabelArray[i].setEnabled(false);
			friendsLabelArray[i].addMouseListener(this);
			friendPanel.add(friendsLabelArray[i]);
			i++;
		}
		
		
		jphy3=new JPanel(new GridLayout(2,1));
		//把两个按钮加入到jphy3
		jphy3.add(groupButton);
		jphy3.add(SearchButton);
		
		
		friendScrollPane=new JScrollPane(friendPanel);
		
		
		
		friendCardPanel.add(friendButton,"North");
		friendCardPanel.add(friendScrollPane,"Center");
		friendCardPanel.add(jphy3,"South");
		
		
		/************************************
		 * 处理陌生人选项卡界面
		 * 
		 * *************
		 **/
		
		
		friendButton2 = new JButton("我的好友");
		friendButton2.addActionListener(this);
		groupButton2 = new JButton("QQ群");
		SearchButton2 = new JButton("添加好友/QQ群");
		SearchButton2.addActionListener(this);
		groupCardPanel=new JPanel(new BorderLayout());
		//假定有20个QQ群
		groupPanel=new JPanel(new GridLayout(20,1,4,4));
		
		//给jphy2，初始化20个QQ群.
		groupLabels=new JLabel[20];
		int j = 0;
		while(j<20) {
			groupLabels[j] = new JLabel();
			//groupLabels[j].setEnabled(false);
			groupLabels[j].addMouseListener(this);
			groupPanel.add(groupLabels[j]);
			j++;
		}
		
		/*for(int i=0;i<jb1s2.length;i++)
		{
			jb1s2[i]=new JLabel(i+1+"",new ImageIcon("image/mm.jpg"),JLabel.LEFT);
			strangerPanel.add(jb1s2[i]);
		}*/
		
		northButtonsPanel=new JPanel(new GridLayout(2,1));
		//把两个按钮加入到jphy3
		northButtonsPanel.add(friendButton2);
		northButtonsPanel.add(groupButton2);
		
		
		groupScrollPane=new JScrollPane(groupPanel);
		
		
		//对jphy1,初始化
		groupCardPanel.add(northButtonsPanel,"North");
		groupCardPanel.add(groupScrollPane,"Center");
		groupCardPanel.add(SearchButton2,"South");
		
		
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(friendCardPanel,"1");
		this.add(groupCardPanel,"2");
		//在窗口显示自己的编号.
		this.setTitle(ownerId);
		this.setSize(200, 400);
		this.setLocation(1200, 50);
		this.setVisible(true);	
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//如果点击了陌生人按钮，就显示第二张卡片
		
		if(arg0.getSource()==groupButton)
		{
			//当进入QQ群选项卡的时候flag变量为true
			flag = true;
			cl.show(this.getContentPane(), "2");
		}else if(arg0.getSource()==friendButton2){
			flag= false;
			cl.show(this.getContentPane(), "1");
		} else if(arg0.getSource() == SearchButton||arg0.getSource() == SearchButton2) {
			searchFriend(ownerId);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//响应用户双击的事件，并得到好友的ID.
		//System.out.println("The Source I clicked is :"+arg0.getSource().toString());
		if(arg0.getClickCount()==2 && flag == false)
		{
			//得到该好友的编号
			String friendName=((JLabel)arg0.getSource()).getText();
			//System.out.println("你希望和 "+friendName+" 聊天");
			//如果chat已经添加到hashmap中，就直接setVisable,否则就创建Chat类并且添加到ChatManager的hashmap
			if(ChatManager.contains(this.ownerId+" "+friendName)) {
				Chat qqChat = ChatManager.getChat(this.ownerId+" "+friendName);
				qqChat.setVisible(true);
			} else {
				Chat qqChat=new Chat(this.ownerId,friendName);
				qqChat.setVisible(true);
				//把聊天界面加入到管理类
				ChatManager.addChat(this.ownerId+" "+friendName, qqChat);
			}
		} else if (arg0.getClickCount()==2 && flag == true) {
			String groupName = ((JLabel)arg0.getSource()).getText();
			if(GroupChatManager.contains(this.ownerId+" "+groupName)) {
				System.out.println(this.ownerId+" "+groupName);
				GroupChat qqChat = GroupChatManager.getChat(this.ownerId+" "+groupName);
				System.out.println("以后点击QQ群");
				qqChat.setVisible(true);
				qqChat.requestMembers();
			} else {
				GroupChat qqChat=new GroupChat(this.ownerId,groupName);
				qqChat.setVisible(true);
				//把聊天界面加入到管理类
				GroupChatManager.addChat(this.ownerId+" "+groupName, qqChat);
				System.out.println("第一次点击QQ群！");
				qqChat.requestMembers();
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.red);
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
