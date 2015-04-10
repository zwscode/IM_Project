package com.client.function;



import java.util.HashMap;
import com.common.User;

public class UserManager {
	private static HashMap<String, User> hm = new HashMap<String, User>();
	public static void add(String owner, User u) {
		hm.put(owner, u);
	}
	public static User get(String owner) {
		return hm.get(owner);
	}
}
