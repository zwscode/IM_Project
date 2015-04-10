package com.server.db;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;

import com.common.QqMessage;
/*
 * 1 public static boolean checkLogin(String name,String password) 验证用户名密码返回boolean 来验证登陆信息
 * 2 public static boolean register(String name, String password) 注册用户信息，成功则返回true,否则false
 * 3 public static String getFriends(String sender) 返回friends字符串
 * 4 public static String getFriends(String sender) 返回strangers字符串
 * 5 
 */
public class SqlHelper {
	//想要运行程序得自行修改参数，第一个是数据库的IP地址、端口和名称； 第二个参数是数据库的用户名； 第三个参数时数据库的密码
    private static String[] connectionParameters ={ "jdbc:mysql://localhost:3306/mysql_qq","root","zwssysu"};

    
	public static String getGroupMembers(String name) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM groups WHERE groupname='"+name+"'");
       	 if(rs.next()) {
       		return rs.getString("groupmembers").trim();
       	 }
       	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return null;
	}
	//登陆验证
	public static boolean checkLogin(String name,String password) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+name+"' AND password = '"+password+"'");
       	 if(rs.next()) {
       		 return true;
       	 }
       	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return false;
	}
	//通过用户名,密码来注册
	public static boolean register(String name, String password) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    try {
	       	 // 载入Mysql的驱动字符串
	       	Class.forName("com.mysql.jdbc.Driver");
	       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
	       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
	       	// 获取表达式对象实例
	       	stmt = conn.createStatement();
	       	rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+name+"'");
	       	if(rs.next()) {
	       		conn.close();
	       		return false;
	       	} else {
	       		int i = stmt.executeUpdate("INSERT INTO users (username, password) VALUES ('"+name+"','"+password+"')");
	       		if(i == 0) {
	       			conn.close();
	       			return false;
	       		} else {
	       			return true;
	       		}
	       	}
	    } catch (Exception e) {
	      	 e.printStackTrace();
	       }
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 获取某个用户的所有好友，返回一个空格隔开的字符串（数据库中的friends列的数据）
	 */
	public static String getFriends(String sender) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+sender+"'");
       	 if(rs.next()) {
       		 return rs.getString("friends").trim();
       	 }
       	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return null;
	}
	//返回groups 字符串
	public static String getGroups(String sender) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+sender+"'");
       	 if(rs.next()) {
       		 return rs.getString("groups").trim();
       	 }
       	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return null;
	}
	public static String getStrangers(String sender) {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+sender+"'");
       	 if(rs.next()) {
       		 return rs.getString("stangers").trim();
       	 }
       	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return null;
	}
	//查询好友
	public static ArrayList<String> getAllUsers() {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    ArrayList<String> alist = new ArrayList<String>();
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM users");
       	 
       	 while(!rs.isLast()) {
       		 rs.next();
       		 alist.add(rs.getString("username"));
       	 }
       	 conn.close();
	     } catch (Exception e) {
	      	 e.printStackTrace();
	     }
		return alist;
	}
	//返回所有QQ群
	public static ArrayList<String> getAllGroups() {
		//先定义变量，后使用和关闭
	    Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    ArrayList<String> alist = new ArrayList<String>();
		try {
       	 // 载入Mysql的驱动字符串
       	 Class.forName("com.mysql.jdbc.Driver");
       	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	// 获取表达式对象实例
       	 stmt = conn.createStatement();
       	 rs=stmt.executeQuery("SELECT * FROM groups");
       	 
       	 while(!rs.isLast()) {
       		 rs.next();
       		 alist.add(rs.getString("groupname"));
       	 }
       	 conn.close();
	     } catch (Exception e) {
	      	 e.printStackTrace();
	     }
		return alist;
	}
	//互相添加好友的功能
	public static void makeFriends(String sender, String receiver) {
		// TODO Auto-generated method stub
		Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    String friends1 = null;
	    String friends2 = null;
		try {
			// 载入Mysql的驱动字符串
       	 	Class.forName("com.mysql.jdbc.Driver");
       	 	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 	conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	 	// 获取表达式对象实例
       	 	stmt = conn.createStatement();
          	rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+sender+"'");
       	 	if(rs.next()) {
    	 		friends1 = rs.getString("friends");
    	 	}
       	 	friends1 +=" " + receiver;
       	 	rs = stmt.executeQuery("SELECT * FROM users WHERE username ='"+receiver+"'");
	       	if(rs.next()) {
	 	 		friends2 = rs.getString("friends");
	 	 	}
	       	friends2 += " " +sender;
       	 	stmt.executeUpdate("UPDATE users SET friends = '"+friends1+"' WHERE username='"+sender+"'");
       	 	stmt.executeUpdate("UPDATE users SET friends = '"+friends2+"' WHERE username='"+receiver+"'");
       	 	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
	}
	//添加QQ群的功能
	public static void joinGroup(String sender, String groupname) {
		// TODO Auto-generated method stub
		Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    String newgroups = null;
	    String newgroupmembers = null;
		try {
			// 载入Mysql的驱动字符串
       	 	Class.forName("com.mysql.jdbc.Driver");
       	 	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 	conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	 	// 获取表达式对象实例
       	 	stmt = conn.createStatement();
          	rs=stmt.executeQuery("SELECT * FROM users WHERE username='"+sender+"'");
       	 	if(rs.next()) {
    	 		newgroups = rs.getString("groups");
    	 	}
       	 	newgroups +=" " + groupname;
       	 	rs = stmt.executeQuery("SELECT * FROM groups WHERE groupname ='"+groupname+"'");
	       	if(rs.next()) {
	 	 		newgroupmembers = rs.getString("groupmembers");
	 	 	}
	       	newgroupmembers += " " +sender;
       	 	stmt.executeUpdate("UPDATE users SET groups = '"+newgroups+"' WHERE username='"+sender+"'");
       	 	stmt.executeUpdate("UPDATE groups SET groupmembers = '"+newgroupmembers+"' WHERE groupname='"+groupname+"'");
       	 	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
	}
	public static boolean registerGroup(String admin,String group) {
		// TODO Auto-generated method stub
		Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    String newgroups = null;
	    String newgroupmembers = null;
	    try {
			// 载入Mysql的驱动字符串
       	 	Class.forName("com.mysql.jdbc.Driver");
       	 	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 	conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	 	// 获取表达式对象实例
       	 	stmt = conn.createStatement();
          	rs=stmt.executeQuery("SELECT * FROM groups WHERE groupname='"+group+"'");
       	 	if(rs.next()) {
    	 		return false;
    	 	}
       	 	stmt.executeUpdate("INSERT INTO groups (groupname, admin) VALUES ('"+group+"','"+admin+"')");
       	 	conn.close();
       } catch (Exception e) {
      	 e.printStackTrace();
       }
		return true;
	}
	public static void storeMessage(QqMessage info) {
		// TODO Auto-generated method stub
		Connection conn = null;//声明数据库连接对象
	    Statement stmt = null; //声明数据库表达式对象
	    ResultSet rs = null;//声明结果集对象
	    String newgroups = null;
	    String newgroupmembers = null;
	    try {
			// 载入Mysql的驱动字符串
       	 	Class.forName("com.mysql.jdbc.Driver");
       	 	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
       	 	conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
       	 	// 获取表达式对象实例
       	 	stmt = conn.createStatement();
       	 	stmt.executeUpdate("INSERT INTO qqmessages (type, sender,receiver,message,time,fontSize,color) " +
       	 			"VALUES('"+info.getMessageType()+"','"+info.getSender()+"','"+info.getReceiver()+"','"+info.getMessage()+"','" 
       	 			+info.getTime()+"','"+info.getFontSize()+"','"+info.getFontColor().getRGB()+"')");
       	 	conn.close();
	    } catch (Exception e) {
	      	 e.printStackTrace();
	    }
	}
	public static ArrayList<QqMessage> getUnreadMessages(String receiver) {
		// TODO Auto-generated method stub
				ArrayList<QqMessage> alist = new ArrayList<QqMessage>();
				Connection conn = null;//声明数据库连接对象
			    Statement stmt = null; //声明数据库表达式对象
			    ResultSet rs = null;//声明结果集对象
			    String newgroups = null;
			    String newgroupmembers = null;
			    try {
					// 载入Mysql的驱动字符串
		       	 	Class.forName("com.mysql.jdbc.Driver");
		       	 	// 获取数据库的连接 mysql_qq是数据库的名称， root是SQL用户ID，zwssysu是SQL的用户密码
		       	 	conn = DriverManager.getConnection(connectionParameters[0], connectionParameters[1], connectionParameters[2]); 
		       	 	// 获取表达式对象实例
		       	 	stmt = conn.createStatement();
		       	 	rs = stmt.executeQuery("SELECT * FROM qqmessages WHERE receiver = '"+receiver+"'");
		       	 	if(rs.first()) {
			       	 	while(!rs.isAfterLast()) {
				       		QqMessage m = new QqMessage();
				       		m.setMessageType("Message");
				       		m.setMessage(rs.getString("message"));
				       		m.setSender(rs.getString("sender"));
				       		m.setTime(rs.getString("time"));
				       		m.setReceiver(rs.getString("receiver"));
				       		m.setFontSize(rs.getInt("fontSize"));
				       		m.setFontColor(new Color(rs.getInt("color")));
				       		alist.add(m);
				       		rs.next();
				       	}
				       	stmt.executeUpdate("DELETE FROM qqmessages WHERE receiver='"+receiver+"'");
		       	 	}
			       	
		       	 conn.close();
			    } catch (Exception e) {
			      	 e.printStackTrace();
			    }
			    return alist;
	}
}
