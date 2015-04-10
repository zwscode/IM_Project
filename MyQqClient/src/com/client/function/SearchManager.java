/**
 * Search 管理器
 */
package com.client.function;

import java.util.HashMap;

import com.client.view.Search;

public class SearchManager {
	private static HashMap<String, Search> hm = new HashMap<String, Search>();
	//加入
	public static void add(String username, Search chat) {
		hm.put(username, chat);
	}
	//取出
	public static Search get(String username) {
		return hm.get(username);
	}
	//是否包含某个Key
	public static boolean contains(String username) {
		return hm.containsKey(username);
	}
}
