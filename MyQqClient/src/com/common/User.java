/**
 * 用户信息类
 */
package com.common;

import java.util.*;

import javax.swing.ImageIcon;

import com.client.function.FriendListManager;
import com.client.view.FriendList;

public class User implements java.io.Serializable{
	private String name;
	private String password;
	private HashSet<String> friends = new HashSet<String>();
	private HashSet<String> strangers = new HashSet<String>();
	private ImageIcon head;
	public ImageIcon getHead() {
		return head;
	}
	public void setHead(ImageIcon head) {
		this.head = head;
	}
	//通过其他User创建User
	public User(User user) {
		this.name = user.getName();
		this.password = user.getPassword();
		this.setFriends(user.getFriends());
		this.setStrangers(user.getStrangers());
		this.head = user.getIcon();
	}
	//通过用户名创建User
	public User(String username) {
		this.name = username;
		this.password = "";
		this.head = new ImageIcon("image/mm.jpg");
	}
	public User() {
		this.name ="default";
		this.password="";
		this.head = new ImageIcon("image/mm.jpg");
	}
	//获得头像
	public ImageIcon getIcon() {
		return head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addFriend(String other) {
		friends.add(other);
	}
	
	//通过好友名删除好友
	public void deleteFriend(String name) {
		Iterator<String> it=friends.iterator();
		while(it.hasNext()){
			if(it.next().equals(name)) {
				it.remove();
			}
		}
	}
	//用一个hashset给friends赋值
	public void setFriends(HashSet<String> newfriends) {
		friends = (HashSet<String>)newfriends.clone();
	}
	//返回friends hashset
	public HashSet<String> getFriends(){
		return friends;
	}
	
	
	//陌生人strangers操作
	public void addStranger(String user) {
		strangers.add(user);
	}
	//delete 陌生人
	public void deleteStranger(String name) {
		Iterator<String> it=friends.iterator();
		while(it.hasNext()){
			if(it.next().equals(name)) {
				it.remove();
			}
		}
	}
	
	public void setStrangers(HashSet<String> newstrangers) {
		strangers = (HashSet<String>)newstrangers.clone();
	}
	
	public HashSet<String> getStrangers(){
		return strangers;
	}
	public void updateFriendsList(QqMessage m) {
		// TODO Auto-generated method stub
		//更新好友列表
		System.out.println("User 开始更新好友列表: ");
		if(m.getMessage().equals(null)||m.getMessage().equals("")||m.getMessage().equals(" ")){
			System.out.println("User:该用户没有好友");
			return;
		}
		String friends[]=m.getMessage().split("\\s+");
		
		this.setFriends(new HashSet<String>());
		for(int i=0;i<friends.length;i++)
		{
			this.addFriend(friends[i]);
		}
		System.out.println("User 好友列表更新完毕: ");
		for(int i=0;i<friends.length;i++) {
			System.out.println(friends[i]);
		}
		//接下来就跑到FriendList里面去更新一下当前的好友列表
		FriendList friendlist = FriendListManager.get(this.getName());
		if(friendlist!=null){
			friendlist.getQqOwner().setFriends(this.getFriends());
			friendlist.updateFriendsList(friends);
		} else {
			System.out.println("找不到FriendList对象");
		}
		
	}
	
}
