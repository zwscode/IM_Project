package com.common;

import java.awt.Color;

import javax.swing.ImageIcon;

public class QqMessage implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageType;
	private String message;
	private String sender;
	private String group;
	private Color fontColor = Color.black;
	private int fontSize = 12;
	private ImageIcon iconMessage;
	
	
	public ImageIcon getIconMessage() {
		return iconMessage;
	}
	public void setIconMessage(ImageIcon iconMessage) {
		this.iconMessage = iconMessage;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int textSize) {
		this.fontSize = textSize;
	}
	public Color getFontColor() {
		return fontColor;
	}
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	private String receiver;
	private String time;
	public QqMessage(String messageType1) {
		this.messageType = messageType1;
	}
	public QqMessage(){}
	public String getMessage() {
		return message;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public void setMessage(String con) {
		this.message = con;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}
