package com.server.function;
import java.util.*;
import java.util.Map.Entry;

public class ServerThreadManager {
	public static HashMap hm = new HashMap<String, ServerClientThread>();
	//向hashmap hm添加通讯线程
	public static void addThread(String userId, ServerClientThread sct) {
		hm.put(userId, sct);
	}
	//获取通讯线程
	public static ServerClientThread getThread(String userId) {
		return (ServerClientThread)hm.get(userId);
	}
	//返回当前在线qq
	public static String getOnlineUsers() {
		Iterator<Entry<String, ServerClientThread>> it = hm.entrySet().iterator();
		String userString="";
		while(it.hasNext()) {
			Map.Entry<String, ServerClientThread> entry = (Map.Entry<String, ServerClientThread>)it.next();
			String key = entry.getKey();
			userString+=key + " ";
		}
		return userString;
	}
	//返回某用户是否在线
	public static boolean isOnline(String username) {
		if(hm.containsKey(username)) {
			return true;
		} else {
			return false;
		}
	}
	public static void remove(String name) {
		hm.remove(name);
	}
}
