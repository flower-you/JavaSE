package org.westos.chatroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 服务器端
 * @author Apple
 */
public class ChatRoomServer {

	public static void main(String[] args) {
		
		try {
			//创建服务器端的Socket对象
			ServerSocket ss = new ServerSocket(8888) ;
			/**
			 * 创建一个ArrayList集合,分别保存每个客户端所在的通道内Scoket
			 */
//			ArrayList<Socket> list = new ArrayList<Socket>() ;
			
			//创建HashMap<String,Socket>集合,作用:用来保存用户名和它所对应的通道内的Socket
			HashMap<String, Socket> hm = new HashMap<String, Socket>() ;
			System.out.println("服务器正在等待连接,请稍后...");
			
			//定义个变量:
			int i = 1 ;
			while(true){
				//监听客户端的连接
				Socket s = ss.accept() ; //阻塞
				System.out.println("第"+(i++)+"个客户端已经连接...");
				
				//服务器端一监听到客户端连接了,就添加到集合中
//				list.add(s) ;
				
				//获取服务器端所在通道内的输入输出流
//				InputStream in = s.getInputStream() ;
//				OutputStream out = s.getOutputStream() ;
				
				//分析:服务器为了保存用户名,另外开启保存用户名的线程
				SaveUserThread st = new SaveUserThread(hm,s) ;
				st.start() ;
				
				
				
				// 开启服务器的读取消息的子线程
//				ServerThread st = new ServerThread(in) ;
//				ServerThread st = new ServerThread(s,list) ;
//				st.start() ;
			}
			
			
			//创建键盘录入对象
			//Scanner sc = new Scanner(System.in) ;
			/*//服务器不断的读消息,和回复消息
			while(true){
				
				
				//回复消息
				System.out.println("请回复消息:");
				String msgStr = sc.nextLine() ;
				
				//使用输出流对象发送消息
				out.write(msgStr.getBytes()) ;
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
