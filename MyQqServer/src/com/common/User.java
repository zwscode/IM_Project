/**
 * 用户信息类
 */
package com.common;

import java.util.*;

import javax.swing.ImageIcon;

public class User implements java.io.Serializable{
	private String name;
	private String password;
	private HashSet<String> friends = new HashSet<String>();
	private HashSet<String> strangers = new HashSet<String>();
	private ImageIcon head;
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
	
}
