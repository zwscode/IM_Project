/**
 * 管理客户端Thread的类
 * 由于一个电脑可能运行着多个QQ客户端就需要一个SocketThreadManager来管理他们，
 * 这样在需要的时候就能够找出相应的SocketThread来执行操作
 * 
 */
package com.client.function;
import java.util.*;

public class ClientThreadManager {
	private static HashMap<String, ClientThread> hm = new HashMap<String, ClientThread>();
	public static void addThread(String userId, ClientThread td) {
		hm.put(userId, td);
	}
	public static ClientThread getThread(String userId) {
		return (ClientThread)hm.get(userId);
	}
	public static boolean contains(String chosenLabel) {
		// TODO Auto-generated method stub
		if(hm.containsKey(chosenLabel)) {
			return true;
		}
		return false;
	}
	public static void remove(String name) {
		hm.remove(name);
	}
}
