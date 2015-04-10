/**
 * 登陆成功后将各个QQ的Socket保存起来，
 * 给以后聊天的时候来使用该Socket与服务器进行通信
 */
package com.client.function;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import com.client.view.Chat;
import com.client.view.FriendList;
import com.client.view.GroupChat;
import com.client.view.Search;
import com.common.QqMessage;
import com.common.User;



public class ClientThread extends Thread{
	private Socket s;
	private String username;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	public boolean isRunning = true;
	public void setIsRunning(boolean b) {
		// TODO Auto-generated method stub
		isRunning = b;
	}
	public ClientThread(Socket s2, String username) throws IOException {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.s = s2;
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
	}
	public Socket getSocket() {
		return s;
	}
	public void setSocket(Socket s2) {
		this.s =  s2;
	}
	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}
	public ObjectInputStream getObjectInputStream() {
		return ois;
	}
	public void run()
	{
		
		//不停的读取从服务器端发来的消息
		try {
			while(isRunning) {
				QqMessage m=(QqMessage)this.getObjectInputStream().readObject();
				System.out.println("ClientThread读取到从服务发来的消息:"+ m.getSender() +" 对 "+m.getReceiver()+" 说 "+
									m.getMessage()+" MessageType:"+m.getMessageType());
				//如果从服务器得到的qqMessage类型是普通聊天信息MessageType是Message类型
				if (m.getMessageType().equals("ServerClientThreadClosing")) {
					if (s.isConnected()) {
						try{
							s.shutdownInput();
							s.shutdownOutput();
							s.close();
							isRunning = false;
							System.out.println("ClientThread Socket成功关闭");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}else if(m.getMessageType().equals("Message"))
				{
					if(ChatManager.contains(m.getReceiver()+" "+m.getSender())) {
						//把从服务器获得消息，显示到该显示的聊天界面
						Chat qqChat=ChatManager.getChat(m.getReceiver()+" "+m.getSender());
						qqChat.setVisible(true);
						//显示
						qqChat.showMessage(m);
					} else {
						Chat qqChat = new Chat(m.getReceiver(), m.getSender());
						ChatManager.addChat(m.getReceiver()+" "+m.getSender(), qqChat);
						qqChat.setVisible(true);
						qqChat.showMessage(m);
					}
					
				} else if(m.getMessageType().equals("ReturnOnline")) {
					String receiver=m.getReceiver();
					//修改相应的好友列表.
					FriendList friendList=FriendListManager.get(receiver);
					if(m.getMessage().trim().equals("")||m.getMessage().equals(null)) {
						//do nothing
					} else {
						if(friendList!=null)
						{
							friendList.upateOnline(m);
						
						}
					}
					//if(qqFriendList)
					//更新在线好友.
					
					friendList.setVisible(true);

				} else if (m.getMessageType().equals("ReturnFriendList")) {	//处理FriendList好友列表
					
					String receiver=m.getReceiver();
					User u=UserManager.get(receiver);
					u.updateFriendsList(m);
				} else if (m.getMessageType().equals("ReturnGroupMembers")) {
					System.out.println("返回的成员列表信息： 接受者"+m.getReceiver()+"QQ群："+m.getGroup()+"群成员："+m.getMessage());
					if(GroupChatManager.contains(m.getReceiver()+" "+m.getGroup())) {
						GroupChat gc = GroupChatManager.getChat(m.getReceiver()+" "+m.getGroup());
						gc.updateMembersList(m.getMessage());
					} else {
						GroupChat gc = new GroupChat(m.getReceiver(),m.getGroup());
						GroupChatManager.addChat(m.getReceiver()+" "+m.getGroup(), gc);
						System.out.println("新建一个GroupChat！"+m.getGroup());
					}
				} else if (m.getMessageType().equals("ReturnGroupList")) {	//处理FriendList群列表
					
					FriendList fl = FriendListManager.get(m.getReceiver());
					String[] temp = null;
					if(m.getMessage().equals(null)||m.getMessage().trim().equals("")){
						System.out.println("ClientThread:该用户没有QQ群");
						
					} else {
						temp = m.getMessage().split("\\s+");
						fl.updateGroupsList(temp);
					}

				} else if (m.getMessageType().equals("GroupMessage")) {	//处理群消息
					System.out.println("ClientThread读取到从服务发来的GroupMessage消息:"+ m.getSender() +" 对 "+m.getReceiver()+"在QQ群中"+m.getGroup()+" 说 "+
							m.getMessage()+" MessageType:"+m.getMessageType());
					if(GroupChatManager.contains(m.getReceiver()+" "+m.getGroup())) {
						//把从服务器获得消息，显示到该显示的聊天界面
						GroupChat qqChat=GroupChatManager.getChat(m.getReceiver()+" "+m.getGroup());
						qqChat.setVisible(true);
						//显示
						qqChat.showMessage(m);
					} else {
						GroupChat qqChat = new GroupChat(m.getReceiver(), m.getGroup());
						GroupChatManager.addChat(m.getReceiver()+" "+m.getGroup(), qqChat);
						qqChat.requestMembers();
						qqChat.setVisible(true);
						qqChat.showMessage(m);
					}
					
				} else if(m.getMessageType().equals("ReturnSearchForFriend")) {
					if(SearchManager.contains(m.getReceiver())) {
						Search s = SearchManager.get(m.getReceiver());
						System.out.println("ClientThread:获得好友查询结果："+m.getMessage());
						s.updateResults(m.getMessage());
					}
				} else if (m.getMessageType().equals("ReturnSearchForGroup")) {
					if(SearchManager.contains(m.getReceiver())) {
						Search s = SearchManager.get(m.getReceiver());
						System.out.println("ClientThread:获得QQ群查询结果："+m.getMessage());
						s.updateResults(m.getMessage());
					}
				} else if(m.getMessageType().equals("ReturnRegisterGroup")) {
					if(m.getMessage().equals("RegisterGroupSuccessful")) {
						JOptionPane.showMessageDialog(FriendListManager.get(m.getReceiver()), "注册成功！", "QQ群注册信息", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(FriendListManager.get(m.getReceiver()), "注册失败，已经存在该群！", "QQ群注册信息", JOptionPane.INFORMATION_MESSAGE);

					}
				}
			}
		} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
		}finally {
			if(s.isConnected()) {
				try {
					ClientThreadManager.remove(username);
					s.close();
					s = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
			
}

