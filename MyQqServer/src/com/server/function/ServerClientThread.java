/**
 * 服务器与某客户端通信线程
 * 本类在login成功登陆之后start
 */

package com.server.function;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

import com.common.QqMessage;
import com.server.db.SqlHelper;

public class ServerClientThread extends Thread {
	private Socket s;
	private String username;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public ServerClientThread(Socket s,String username) throws IOException {
		this.username = username;
		this.s = s;
		ois = new ObjectInputStream(s.getInputStream());
		oos = new ObjectOutputStream(s.getOutputStream());
	}
	public Socket getSocket() {
		return this.s;
	}
	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}
	public ObjectInputStream getObjectInputStream() {
		return ois;
	}
	public void run() {
		
			//线程接受客户端的信息
			try{
				while(true) {
					QqMessage info = (QqMessage)this.getObjectInputStream().readObject();
					System.out.println("ServerClientThread: "+info.getSender()+"对"+info.getReceiver()+"说"+info.getMessage()+";MessageType:"+info.getMessageType());
					if(info.getMessageType().equals("ClientThreadClosing")) {
						QqMessage m = new QqMessage("ServerClientThreadClosing");
						//告诉正在监听的ClientThread滚粗！
						ServerClientThread receiver = ServerThreadManager.getThread(info.getSender());
						ObjectOutputStream tempOos = receiver.getObjectOutputStream();
						tempOos.writeObject(m);
						tempOos.flush();
						break;
					} else if (info.getMessageType().equals("Message")) {
						//如果该用户是在线的就直接转发消息
						if(ServerThreadManager.isOnline(info.getReceiver())) {
						//获取接收人的通信线程,并将接收到的info信息发送给接收人Receiver
						ServerClientThread receiver = ServerThreadManager.getThread(info.getReceiver());
						ObjectOutputStream tempOos = receiver.getObjectOutputStream();
							tempOos.writeObject(info);
							tempOos.flush();
						} else {
							/**************************************
							 * 离线消息上线转发
							 * 不在线就把这条Messae存入数据库
							 */
							SqlHelper.storeMessage(info);
						}
						
					} else if (info.getMessageType().equals("GroupMessage")) {
						//处理群聊信息
						String group = info.getGroup();
						/*3.4要补全这里！！！*/
						String[] members = SqlHelper.getGroupMembers(info.getGroup()).split("\\s+");
						for (int i=0;i<members.length;i++) {
							QqMessage m = new QqMessage("GroupMessage");
							m.setSender(info.getSender());
							m.setReceiver(members[i]);
							m.setGroup(group);
							m.setTime(info.getTime());
							m.setMessage(info.getMessage());
							if(ServerThreadManager.isOnline(members[i])) {
								System.out.println("ServerClientThread向"+members[i]+"客户端发送群聊信息:"+m.getMessage());
								ObjectOutputStream temp = ServerThreadManager.getThread(members[i]).getObjectOutputStream();
								temp.writeObject(m);
							}
						}
					} else if (info.getMessageType().equals("RequestFriendList")) {
						//处理并返回好友列表的 String
						System.out.println("ServerClientThread: "+info.getSender()+"正在请求好友列表");
						String sender = info.getSender();
						//发送请求的sender就是服务器返回的新信息的receiver
						ServerClientThread receiverThread = ServerThreadManager.getThread(info.getSender());
						String friends = SqlHelper.getFriends(sender);
						System.out.println("ServerClientThread 好友列表如下："+friends);
						QqMessage friendsMessage = new QqMessage();
						friendsMessage.setReceiver(info.getSender());
						friendsMessage.setMessageType("ReturnFriendList");
						friendsMessage.setMessage(friends);
						//向请求客户端发送好友列表信息
						ObjectOutputStream tempOos = receiverThread.getObjectOutputStream();
						tempOos.writeObject(friendsMessage);
						
					} else if (info.getMessageType().equals("RequestOnline")) {
						//处理并返回在线的好友
						//处理并返回好友列表的 String
						System.out.println("ServerClientThread: "+info.getSender()+"正在请求Online好友列表");
						String sender = info.getSender();
						String friends = SqlHelper.getFriends(sender);
						String onlineFriends = "";
						if(friends.equals("") || friends.equals(null)) {
							//do nothing
						} else {
							String[] tempString = friends.trim().split("\\s+");
							for(int i=0; i < tempString.length;i++) {
								if(ServerThreadManager.isOnline(tempString[i])) {
									onlineFriends+=tempString[i]+" ";
								}
							}
						}
						QqMessage onlineMessage = new QqMessage();
						onlineMessage.setMessageType("ReturnOnline");
						onlineMessage.setMessage(onlineFriends);
						System.out.println("ServerClientThread 在线好友列表如下："+onlineFriends);
						onlineMessage.setReceiver(info.getSender());
						//发送请求的sender就是服务器返回的新信息的receiver
						ServerClientThread receiver = ServerThreadManager.getThread(info.getSender());
						ObjectOutputStream tempOos = receiver.getObjectOutputStream();
						tempOos.writeObject(onlineMessage);
						//Why?!?!?!??!?
						
					} else if (info.getMessageType().equals("RequestGroupMembers")) {
						System.out.println("ServerClientThread: "+info.getSender()+"GroupChat正在从数据库获取QQ群成员表");
						String groupMembers = SqlHelper.getGroupMembers(info.getGroup());
						QqMessage members = new QqMessage("ReturnGroupMembers");
						members.setMessage(groupMembers);
						members.setReceiver(info.getSender());
						members.setGroup(info.getGroup());
						ServerClientThread receiver = ServerThreadManager.getThread(info.getSender());
						ObjectOutputStream tempOos = receiver.getObjectOutputStream();
						tempOos.writeObject(members);
					} else if (info.getMessageType().equals("RequestGroupList")) {
						System.out.println("ServerClientThread: "+info.getSender()+"FriendList正在请求QQ群列表");
						String sender = info.getSender();
						//发送请求的sender就是服务器返回的新信息的receiver
						ServerClientThread receiverThread = ServerThreadManager.getThread(info.getSender());
						String groups = SqlHelper.getGroups(sender);
						System.out.println("ServerClientThread QQ群列表如下："+groups);
						QqMessage groupMessage = new QqMessage();
						groupMessage.setReceiver(info.getSender());
						groupMessage.setMessageType("ReturnGroupList");
						groupMessage.setMessage(groups);
						//向请求客户端发送好友列表信息
						ObjectOutputStream tempOos =receiverThread.getObjectOutputStream();
						tempOos.writeObject(groupMessage);
					} else if (info.getMessageType().equals("SearchForFriend")) {
						ArrayList<String> alist = SqlHelper.getAllUsers();
						Iterator<String> it = alist.iterator();
						String rString = "";
						while(it.hasNext()) {
							String temp = it.next();
							if(!temp.contains(info.getMessage())) {	//剔除掉所有用户名中不包含搜索关键字的字符串
								it.remove();
							} else {
								rString += temp+" ";
							}
						}
						QqMessage result = new QqMessage("ReturnSearchForFriend");
						result.setMessage(rString);
						result.setReceiver(info.getSender());
						ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getSender()).getObjectOutputStream();
						tempOos.writeObject(result);
					} else if (info.getMessageType().equals("RequestAddFriend")) {
						SqlHelper.makeFriends(info.getSender(),info.getReceiver());
						//通知被加的SB 尼玛得更新好友列表了
						
						//复杂的1B
						if(ServerThreadManager.isOnline(info.getReceiver())){	 //if friend is online update friendlist
							ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getReceiver()).getObjectOutputStream();
							QqMessage m1= new QqMessage("ReturnFriendList");
							m1.setReceiver(info.getReceiver());
							m1.setMessage(SqlHelper.getFriends(info.getReceiver()));
							
							tempOos.writeObject(m1);
							tempOos.flush();
							tempOos.reset();
							
							QqMessage m2= new QqMessage("ReturnOnline");
							m2.setReceiver(info.getReceiver());
							
							String friends = SqlHelper.getFriends(info.getReceiver());
							String onlineFriends = "";
							if(friends.equals("") || friends.equals(null)) {
								//do nothing
							} else {
								String[] tempString = friends.trim().split("\\s+");
								for(int i=0; i < tempString.length;i++) {
									if(ServerThreadManager.isOnline(tempString[i])) {
										onlineFriends+=tempString[i]+" ";
									}
								}
							}
							
							
							m2.setMessage(onlineFriends);
							tempOos.writeObject(m2);
							tempOos.flush();
							tempOos.reset();
							
						}
						
						
					} else if (info.getMessageType().equals("SearchForGroup")) {
						System.out.println("接到用户搜索QQ群请求");
						ArrayList<String> alist = SqlHelper.getAllGroups();
						Iterator<String> it = alist.iterator();
						String rString = "";
						while(it.hasNext()) {
							String temp = it.next();
							if(!temp.contains(info.getMessage())) {	//剔除掉所有用户名中不包含搜索关键字的字符串
								it.remove();
							} else {
								rString += temp+" ";
							}
						}
						QqMessage result = new QqMessage("ReturnSearchForGroup");
						result.setMessage(rString);
						System.out.println("返回QQ群搜索结果："+rString);
						result.setReceiver(info.getSender());
						ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getSender()).getObjectOutputStream();
						tempOos.writeObject(result);
					} else if(info.getMessageType().equals("RequestJoinGroup")) {
						
						SqlHelper.joinGroup(info.getSender(),info.getGroup());
						
						QqMessage m = new QqMessage("ReturnGroupList");
						m.setReceiver(info.getSender());
						m.setMessage(SqlHelper.getGroups(info.getSender()));
						ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getSender()).getObjectOutputStream();
						tempOos.writeObject(m);
					} else if(info.getMessageType().equals("RegisterGroup")) {
						boolean b = SqlHelper.registerGroup(info.getSender(),info.getGroup());
						QqMessage m = new QqMessage("ReturnRegisterGroup");
						m.setReceiver(info.getSender());
						if(b) {
							m.setMessage("RegisterGroupSuccessful");
						} else {
							m.setMessage("RegisterGroupFailed");
						}
						ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getSender()).getObjectOutputStream();
						tempOos.writeObject(m);
					} else if (info.getMessageType().equals("RequestUnreadMessages")) {
						ArrayList<QqMessage> alist = SqlHelper.getUnreadMessages(info.getSender());
						Iterator<QqMessage> it=alist.iterator();
						while(it.hasNext()){
							QqMessage temp = it.next();
							ObjectOutputStream tempOos =ServerThreadManager.getThread(info.getSender()).getObjectOutputStream();
							tempOos.writeObject(temp);
						}
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					System.out.println("ServerClientThread关闭中。");
					ServerThreadManager.remove(username);
					s.shutdownOutput();
					s.shutdownInput();
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
	}
}
