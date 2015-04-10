package com.server.function;
import java.net.*;
import java.io.*;
import java.util.*;

import com.common.*;
import com.server.db.SqlHelper;

public class Server extends ServerSocket{
	private ServerSocket ss;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Server myserver = new Server();
	}
	public Server() throws IOException {
		try {
			//监听3456端口
			System.out.println("服务器监听3456");
			ss = new ServerSocket(3456);
			while(true){
				Socket s = ss.accept();
				QqMessage send = new QqMessage();
				
				//获取ObjectInputStream
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				QqMessage  receive = (QqMessage)ois.readObject();
				System.out.println("MyServer从客户端接收到"+receive.getSender()+" "+receive.getMessage());
				if(receive.getMessageType().equals("CheckLogin")) {
					System.out.println("收到客户端"+receive.getSender()+"ConnectToServer的登陆请求");
					//处理接收到的结果,然后设置Info类中的infoType
					//先判断该用户是否已经在线
					if(ServerThreadManager.isOnline(receive.getSender())) {
						send.setMessageType("AlreadyOnline");
						oos.writeObject(send);
						oos.flush();
						continue;
					}
					send.setMessageType("CheckLogin");
					if(SqlHelper.checkLogin(receive.getSender(), receive.getMessage())) {
						send.setMessage("LoginSuccessful");
						oos.writeObject(send);
						oos.flush();
						//登陆成功后新建一个线程用来和服务器进行通讯
						ServerClientThread sct = new ServerClientThread(s,receive.getSender());
						//将线程保存到hashmap中
						ServerThreadManager.addThread(receive.getSender(), sct);
						sct.start();
						//
						String friends = SqlHelper.getFriends(receive.getSender());
						String[] temp = friends.split(" ");
						if(temp.length !=0) {
							for(int i=0;i<temp.length;i++) {
								if(ServerThreadManager.isOnline(temp[i])) {
									ObjectOutputStream oos2 = ServerThreadManager.getThread(temp[i]).getObjectOutputStream();
									QqMessage onlineMessage = new QqMessage();
									onlineMessage.setMessageType("ReturnOnline");
									//消息的内容是上线的这个家伙
									onlineMessage.setReceiver(temp[i]);
									onlineMessage.setMessage(receive.getSender());
									oos2.writeObject(onlineMessage);
									System.out.println("向"+temp[i]+"发送我上线的消息！！");
								}
							}
						System.out.println("All Friends Alerted!");
						} else {
							System.out.println("This poor soul has no friend!!!");
						}
						
						
					} else {
						send.setMessage("LoginFailed");
						oos.writeObject(send);
					}
					
				} else if (receive.getMessageType().equals("Register")) {
					System.out.println("MyServer收到客户端ConnectToServer的注册请求");
					send.setMessageType("Register");
					if(SqlHelper.register(receive.getSender(), receive.getMessage())) {
						
						send.setMessage("RegisterSuccessful");
					} else {
						send.setMessage("RegiseterFailed");
					}
					oos.writeObject(send);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(!ss.isClosed()) {
				try {
					ss.close();
					ss = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
	}
}
