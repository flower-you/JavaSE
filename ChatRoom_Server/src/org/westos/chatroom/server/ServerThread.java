package org.westos.chatroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.westos.chartroo.server.configs.Configs;

/**
 * 服务器端读取消息的子线程
 * @author Apple
 */
public class ServerThread extends Thread {
//	private InputStream in ;
	
	
//	public ServerThread(InputStream in) {
//		this.in = in ;
//	}
	
//	private Socket s ;
//	private ArrayList<Socket> list ;
/*
	public ServerThread(Socket s, ArrayList<Socket> list) {
		this.s = s ;
		this.list = list ;
	}*/

	private HashMap<String, Socket> hm ;
	private Socket s ;
	private String  username ;
	
	public ServerThread(HashMap<String, Socket> hm, Socket s, String username) {
		this.hm= hm ;
		this.s = s ;
		this.username = username ;
	}

	@Override
	public void run() {
		try {
			//获取通道内 输入和输出流
			InputStream in = s.getInputStream() ;
			OutputStream out = s.getOutputStream() ;
			while (true) {
				// 不断的读取客户端发送来过的消息
				byte[] bys = new byte[1024];
				int len = in.read(bys);
				String msg = new String(bys, 0, len);
				//msg就是客户端 发送来的消息:  接受者:消息内容:发送者
				System.out.println(msg); //通过这个输出判断是否按照刚才约定的消息格式
				
				//以前的:消息要拆分
			/*	String[] msgs = msg.split(":") ;//msgs[0],msgs[1],msgs[2]
				//拿到我们需要的数据
//				String receiver = msgs[0] ; //接收者
				
				
				//服务器拆后的消息发过:要遵循:
				//转发格式
				//发送者:消息内容:接收者
				//拿出接受者所在的通道
				//集合中get(索引代表的是每个客户端所在的Socket)
				Socket socket = list.get(Integer.parseInt(msgs[0])); 
				
				//获取接收者所在的通道内的Scoket对象,获取输出流写过去
				OutputStream os = socket.getOutputStream();
				os.write((msgs[2]+":"+"对你说:"+msgs[1]).getBytes()) ;*/
				
				/**
				 * 服务器端的读消息的子线程:
				 * 拿到刚才客户端发送端消息: 接收者:消息内容:消息类型
				 * 
				 * 服务器端应该将消息拆分,重新组装--->转发格式
				 * 发送者:消息内容:消息类型:时间
				 */
				//新约定的消息的拆分
				String[] msgs = msg.split(":") ;
				String receiver = msgs[0] ; //接收者
				String msgContent = msgs[1] ;//消息内容
				int msgType = Integer.parseInt(msgs[2]) ; //消息类型
				//获取系统时间
				long time = System.currentTimeMillis() ;
				
				//根据不同的消息类型做出不同的处理
				if(msgType==Configs.MSG_PRIVATE){
					//针对私聊做出处理
					//1)获取接收者所在的通道
					Socket socket = hm.get(receiver) ;
					//2)遵循转发格式
					//发送者:消息内容:消息类型:时间
					String zfMsg = username+":"+msgContent+":"+msgType+":"+time;
					
					//转发客户端--->客户端子线程去读
					socket.getOutputStream().write(zfMsg.getBytes()) ;
				}else if(msgType==Configs.MSG_PUBLIC){
					//公聊逻辑
					//需要遍历集合获取所有的客户端所在的通道,排除自己
					Set<String> keySet = hm.keySet() ;
					for (String key : keySet) {
						
						//需要排除自己
						if(username.equals(key)){
							continue ;
						}
						
						//获取除过自己所在的通道内的Socket
						Socket socket = hm.get(key) ;
						//要遵循转发格式
						//发送者:消息内容:消息类型:时间
						String zfMsg = username + ":" + msgContent + ":"
								+ msgType + ":" + time;
						socket.getOutputStream().write(zfMsg.getBytes()) ;
						
					}
				}else if(msgType==Configs.MSG_ONLINELIST){
					
					//字符串缓冲区
					StringBuffer sb = new StringBuffer() ;
					//定义初始化遍历
					int i = 1 ;
					//获取在线列表
					//逻辑:遍历HashMap<String,Socket>集合,获取用户
					Set<String> keySet = hm.keySet() ;
					for (String key : keySet) {
						
						//需要排除自己
						if(username.equals(key)){
							continue ;
						}
						
						//1,王五
						//2,张三
						//3,李四
						//需要一个容器,将这些人的名字装起来
						sb.append((i++)).append(",").append(key).append("\n") ;
					}
					//获取用户的通道的socket?获取发送者所在的通道,写给发送者
					//需要遵循服务器转发格式
					//发送者:消息内容:消息类型:时间
					String zfMsg = username + ":" + sb.toString() + ":"
							+ msgType + ":" + time;
					Socket socket = hm.get(username);
					socket.getOutputStream().write(zfMsg.getBytes()) ;
				}
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
