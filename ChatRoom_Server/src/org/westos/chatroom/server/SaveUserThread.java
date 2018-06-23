package org.westos.chatroom.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.westos.chartroo.server.configs.Configs;

/**
 * 服务器保存用户名的线程
 * @author Apple
 */
public class SaveUserThread extends Thread {
	
	private HashMap<String, Socket> hm ;
	private Socket s ;
	private InputStream in;
	private OutputStream out;
	private String username;
	public SaveUserThread(HashMap<String, Socket> hm, Socket s) {
		this.hm = hm ;
		this.s = s ;
	}

	@Override
	public void run() {
		try{
			in = s.getInputStream();
			out = s.getOutputStream();
			//不断的读取客户端发送的用户名
			while(true){
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;
				username = new String(bys, 0, len);
				
				//获取到用户名,服务器需要检验用户名是否存在,并且给客户端反馈过去
				//如果判断:直接使用Map集合中的功能:ContainsKey(String key):判断集合中是否包含指定的键
				if(!hm.containsKey(username)){//用户名不存在
					
					//不存在,就添加集合中
					hm.put(username, s) ;
					
					//给客户端反馈
					out.write("yes".getBytes()) ;
					break ;
				}else{
					//如果存在,不需要再进行存储
					//给客户端反馈
					out.write("no".getBytes()) ;
				}
				
			}
			/**
			 * 上线提醒功能:
			 */
			//遍历HashMap集合,获取到用户名对应的通道Socket对象,排除自己!
			//遍历HashMap<String,Socket>集合
			//entrySet():获取键值对对象 
			//keySet():获取所有的键的集合
			Set<String> keySet = hm.keySet() ;
			for (String key : keySet) {
				
				//如何排除自己
				if(username.equals(key)){
					continue ; //跳出当前循环,立即进入下一个循环
				}
				
				//通过集合获取
				Socket socket = hm.get(key) ;  //获取到每一个用户所对应的通道内的Socket
				
				//通过用户所在的通道Sokcet对象获取输出流发送
//				socket.getOutputStream().write((username+"上线了").getBytes()) ;
				
				//该保存用户名线程是服务器开的线程-->也要遵循服务器转发格式
				//发送者:消息内容:消息类型:时间
				
				OutputStream os = socket.getOutputStream() ;
				//进行转发
				String zfMsg = username + ":" + "上线了" + ":"
						+ Configs.MSG_ONLINE + ":" + System.currentTimeMillis();
				
				//转发过去
				os.write(zfMsg.getBytes()) ;
				
			}
			
			// 注册成功之后,在这里开启服务器的读取消息的子线程
			new ServerThread(hm, s,username).start() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
	}
}
