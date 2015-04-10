package com.client.function;

import java.util.HashMap;
import com.client.view.*;

public class FriendListManager {
	private static HashMap<String, FriendList> hm = new HashMap<String, FriendList>();
	public static void add(String owner, FriendList friendList) {
		hm.put(owner, friendList);
	}
	public static FriendList get(String owner) {
		return hm.get(owner);
	}
	public static boolean contains(String chosenLabel) {
		// TODO Auto-generated method stub
		if(hm.containsKey(chosenLabel)) {
			return true;
		} else {
			return false;
		}
		
	}
}
