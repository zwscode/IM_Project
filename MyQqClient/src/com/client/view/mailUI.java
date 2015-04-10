package com.client.view;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class mailUI extends JFrame {
	JTextArea Text_message;
	JTextField Text_from;
	JTextField Text_to;
	JTextField Text_subject;
	JTextField Text_userName;
	JPasswordField Text_password;

	public mailUI() {
		// TODO Auto-generated constructor stub
		Text_message = new JTextArea();
		Text_from = new JTextField();
		Text_to = new JTextField();
		Text_subject = new JTextField();
		Text_userName = new JTextField();
		Text_password = new JPasswordField();
		JButton sendButton = new JButton("send");
		JButton clearButton = new JButton("clear");
		JButton quitButton = new JButton("quit");
		JLabel from = new JLabel("          From: ");
		JLabel to = new JLabel("               To: ");
		JLabel subject = new JLabel("     Subject: ");
		JLabel userName = new JLabel("Username: ");
		JLabel password = new JLabel("Password: ");
		JLabel message = new JLabel("Message");
		

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p5_1 = new JPanel();
		JPanel p5_2 = new JPanel();

		p1.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		p3.setLayout(new BorderLayout());
		p4.setLayout(new BorderLayout());
		p5.setLayout(new GridLayout(1, 2));
		p5_1.setLayout(new BorderLayout());
		p5_2.setLayout(new BorderLayout());

		p1.add(from, BorderLayout.WEST);
		p1.add(Text_from, BorderLayout.CENTER);

		p2.add(to, BorderLayout.WEST);
		p2.add(Text_to, BorderLayout.CENTER);

		p3.add(subject, BorderLayout.WEST);
		p3.add(Text_subject, BorderLayout.CENTER);

		p4.add(message, BorderLayout.WEST);

		p5_1.add(userName, BorderLayout.WEST);
		p5_1.add(Text_userName, BorderLayout.CENTER);
		p5_2.add(password, BorderLayout.WEST);
		p5_2.add(Text_password, BorderLayout.CENTER);

		p5.add(p5_1);
		p5.add(p5_2);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new GridLayout(5, 1));
		mainPanel.add(p5);
		mainPanel.add(p1);
		mainPanel.add(p2);
		mainPanel.add(p3);
		mainPanel.add(p4);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.add(sendButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(quitButton);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		Text_message.setLineWrap(true);
		Text_message.setWrapStyleWord(true);
		Text_message.setEditable(true);
		JScrollPane scroll = new JScrollPane(Text_message);
		messagePanel.add(scroll,BorderLayout.CENTER);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel,BorderLayout.NORTH);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		getContentPane().add(messagePanel,BorderLayout.CENTER);
		
		sendButton.addActionListener(new sendListener());
		clearButton.addActionListener(new clearListener());
		quitButton.addActionListener(new quitListener());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(600,400);
		setResizable(false);
		setTitle("mail");
		setVisible(true);
	}

	
	class quitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	
	}
	
	class clearListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// �����������
			Text_from.setText("");
			Text_message.setText("");
			Text_password.setText("");
			Text_subject.setText("");
			Text_to.setText("");
			Text_userName.setText("");
		}
	}

	class sendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// ����sendMail���������ʼ�
			try {
				sendMail();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	
	
	}

	private void sendMail() throws IOException{
		
		String str_subjectString = Text_subject.getText();
		String str_from = Text_from.getText();
		String str_to = Text_to.getText();
		String str_userName = Text_userName.getText();
		String str_password = new String(Text_password.getPassword());
		
		int port = 25;
		int i = str_from.indexOf("@");
		String hoststrString = str_from.substring(i+1);
		Socket socket = new Socket("smtp.".concat(hoststrString),port);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream outPrintStream = new PrintStream(socket.getOutputStream());
		
		if (in == null || outPrintStream == null) {
			System.out.println("can not find stream");
			return;
		}
		
		String from_server = in.readLine();
		String to_server;
		
		to_server = "helo " + str_userName;
		outPrintStream.println(to_server);
		
		from_server = in.readLine();
		
		to_server = "AUTH LOGIN"+ "\r\n";
		outPrintStream.print(to_server);
		
		from_server = in.readLine();
		
		String psw = new sun.misc.BASE64Encoder().encode(str_userName.getBytes());
		outPrintStream.println(psw);
		
		from_server = in.readLine();
	
		psw = new sun.misc.BASE64Encoder().encode(str_password.getBytes());
		outPrintStream.println(psw);
			
		from_server = in.readLine();
		
		outPrintStream.println("mail from:<" + str_from +">");
		
		from_server = in.readLine();
		
		outPrintStream.println("RCPT TO: <" + str_to+">");
		
		from_server = in.readLine();

		outPrintStream.println("DATA");
		
		from_server = in.readLine();
		
		outPrintStream.println("FROM: " + str_from);
		outPrintStream.println("TO: " + str_to);
		outPrintStream.println("Subject: " + str_subjectString);
		outPrintStream.println("");
		outPrintStream.println("");
		outPrintStream.print(Text_message.getText()+"\r\n");
		outPrintStream.println(".");
		
		from_server = in.readLine();
	
		outPrintStream.print("QUIT"+"\r\n");
		
		from_server = in.readLine();
		
		if (from_server.equals("221 Bye")) {
			JOptionPane.showMessageDialog(null, "Mail sends successful");
		}
	}

}
