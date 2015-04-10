package com.server.function;

import java.util.*;


public class ServerManager {
	private static HashSet<Server> hm = new HashSet<Server>();
	public static Server getServer(){
		Iterator<Server> it = hm.iterator();
		if(it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}
	public static void putServer(Server s){
		hm.add(s);
	}
}
