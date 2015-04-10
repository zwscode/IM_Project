/**
 * 聊天窗口Chat 管理器
 */
package com.client.function;

import java.util.HashMap;

import com.client.view.Chat;

public class ChatManager {
	private static HashMap<String, Chat> hm = new HashMap<String, Chat>();
	//加入
	public static void addChat(String ownerAndFriend, Chat chat) {
		hm.put(ownerAndFriend, chat);
	}
	//取出
	public static Chat getChat(String ownerAndFriend) {
		return hm.get(ownerAndFriend);
	}
	//是否包含某个Key
	public static boolean contains(String ownerAndFriend) {
		return hm.containsKey(ownerAndFriend);
	}
}
