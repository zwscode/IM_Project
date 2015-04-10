/*
* 这是一个专门在登录前和服务器通信的类，用来验证登陆信息和注册用户
* 登陆后，用户与服务器的通信交给 SocketThread来处理 
*/
package com.client.function;

import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

import com.common.*;

public class ConnectToServer {
	public Socket s;
	//发送登录请求
	public boolean sendLoginInfo(String name, String password){
		boolean b = false;
		try{
			s = new Socket("127.0.0.1",3456);
			QqMessage send = new QqMessage();
			send.setMessageType("CheckLogin");
			send.setSender(name);
			send.setMessage(password);
			//将用户对象发送给服务器用来验证用户账号密码信息
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(send);
			oos.flush();
			
			//接收从服务器返回的QqMessage来确认是否登陆成功
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			QqMessage receive = (QqMessage)ois.readObject();
			//验证通过
			
			if(receive.getMessageType().equals("CheckLogin")) {
				if(receive.getMessage().equals("LoginSuccessful")) {
					b = true;
					//创建SocketThread并添加到储存SocketThread的HashMap<String,SocketThread>中去
					ClientThread td = new ClientThread(s,name);
					//创建Socket线程，并开始监听服务器返回的消息
					td.start();
					
					//将该socket线程加入Manager的HashMap
					ClientThreadManager.addThread(send.getSender(), td);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return b;
		
	}
	public boolean sendRegisterInfo(String name, String password){
		boolean b = false;
		try{
			s = new Socket("127.0.0.1",3456);
			QqMessage send = new QqMessage();
			send.setMessageType("Register");
			send.setSender(name);
			send.setMessage(password);
			//将用户对象发送给服务器用来验证用户账号密码信息
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(send);
			
			//接收从服务器返回的QqMessage来确认是否登陆成功
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			QqMessage receive = (QqMessage)ois.readObject();
			
			//验证通过
			if(receive.getMessageType().equals("Register")) {
				if(receive.getMessage().equals("RegisterSuccessful")) {
					b=true;
				}
			}
			s.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
}
