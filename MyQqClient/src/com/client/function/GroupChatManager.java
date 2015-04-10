/**
 * 聊天窗口GroupChat 管理器
 */
package com.client.function;

import java.util.HashMap;

import com.client.view.GroupChat;



public class GroupChatManager {
	private static HashMap<String, GroupChat> hm = new HashMap<String, GroupChat>();
	//加入
	public static void addChat(String ownerAndGroup, GroupChat chat) {
		hm.put(ownerAndGroup, chat);
	}
	//取出
	public static GroupChat getChat(String ownerAndFriend) {
		return hm.get(ownerAndFriend);
	}
	//是否包含某个Key
	public static boolean contains(String ownerAndFriend) {
		return hm.containsKey(ownerAndFriend);
	}
}
