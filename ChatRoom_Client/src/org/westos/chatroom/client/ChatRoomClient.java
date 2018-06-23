package org.westos.chatroom.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.westos.chatroom.client.configs.Configs;
import org.westos.chatroom.client.util.InputUtil;

/**
 * 客户端
 * @author Apple
 * 
 * 按照刚才这种思路:虽然实现了客户端和服务器端的不停的交互,但是这种做法不好!
 *   客户端的发消息和读消息都在一个线程,服务器也一样,可能会出现一种问题(互相阻塞
 *   	
 *   改进:到底应该把读消息和发消息哪个逻辑放在子线程中?
 *   	将读消息逻辑放在子线程中,一般子线程中不需要键盘录入.
 */
public class ChatRoomClient {
	
	private static Scanner sc;
	private static InputStream in;
	private static OutputStream out;

	public static void main(String[] args) {
		
		try {
			//创建客户端的Socket对象
			Socket s = new Socket("192.168.10.101", 8888);
			
			in = s.getInputStream();
			out = s.getOutputStream();
			
			sc = new Scanner(System.in);
			
			/**
			 * 注册用户名
			 */
			//不断的注册
			while(true){
				System.out.println("请输入用户名:");
				String username = sc.nextLine() ;
				
				//通过通道内的输出流写过去
				out.write(username.getBytes()) ;
				
				//读取服务器的反馈
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;
				String fkMsg = new String(bys, 0, len) ;
				
				//进行判断
				if(fkMsg.equals("yes")){
					System.out.println("注册用户名成功...");
					break ;
				}else if(fkMsg.equals("no")){
					System.out.println("用户名已经注册,请重新输入....");
				}
 			}
			
			
			//开启客户端读消息的子线程
			ClientThread ct = new ClientThread(in) ;
			ct.start() ;
			
			
			//提供菜单选项
			while(true){
				//这些菜单供用户进行选择
				System.out.println("请选择要进行的操作: 1,私聊  2,公聊  3,在线列表 4 ,退出  5,发送文件");
				
				//发送文件:采用:ByteArrayInputStream ByteOutputStream :内存操作输入和输出流
				//内存操作流:只能去发送小文件的东西,从硬盘上读取e:/xx.txt--->保存f:/xx.txt
				//字节流--->一次读取一个字节数进行操作  10KB  可能某个文件的大小可能没有10kb,使用空子字节数组进行补齐
				//空字节数组大小--->整个10kb的字节数- 当前文件的大小
				
//				int num = sc.nextInt() ;
				//使用工具类来录入数据
				int num = InputUtil.inputIntType(new Scanner(System.in)) ;
				//选择结构语句:switch语句
				switch(num){
				case 1:	//私聊
					privateTalk() ;
					break ;
					
				case 2: //公聊
					publicTalk();
					break ;
					
				case 3 ://在线列表
					getOnLineList() ;
					break ;
					
				case 4 ://退出  客户端和服务器端都需要关闭Socket对象
					
					break ;
					
				case 5: //发送文件
					
					break ;
				}
			}
			
		
			
			
		
			
			//s.close() ;  //close()本身方法就会抛出:SocketException
		} catch (SocketException e) {
			//空处理 :不让控制台异常打印出出来
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//在线列表
	private static void getOnLineList() throws  IOException{
		
		//要符合约定的消息格式
		//接收者:消息内容:消息类型
		//组装消息 :最终只是获取在线列表(几个人,并且每一个人罗列出来),接收者,消息内容用null(占位)
		String msg = null +":"+null+":"+Configs.MSG_ONLINELIST ;
		//发过去
		out.write(msg.getBytes()) ;
	}

	//公聊
	private static void publicTalk() throws IOException {
		//客户端不断的发送消息,读取服务器发过来的消息
		while (true) {
			System.out.println("您当前进入了公聊模式 消息格式 接受者 消息内容 消息类型 -q 退出当前模式");
			// 如果需要退出当前模式

			String msg = sc.nextLine();
			if ("-q".equals(msg)) {
				break;
			}

			// 输入消息的格式:
			// 遵循公聊模式是对大家说的话:接收者  用空null代替 
			msg =null +":" +msg + ":" + Configs.MSG_PUBLIC;
			out.write(msg.getBytes());
		}
	}

	//私聊
	private static void privateTalk() throws IOException {
		//客户端不断的发送消息,读取服务器发过来的消息
		while(true){
//			System.out.println("请输入消息:");
//			String msg = sc.nextLine() ;
//			//使用输出流发送过去
////			out.write(msg.getBytes()) ;
			System.out.println("您当前进入了私聊模式 消息格式 接受者 消息内容 发送者 -q 退出当前模式");
			//如果需要退出当前模式
			
			
			String msg = sc.nextLine() ;
			if("-q".equals(msg)){
				break ;
			}
			
			//输入消息的格式:
			//遵循:接收者:消息内容:消息类型
			//张三-->李四	李四:你好:消息类型
			msg = msg + ":"+Configs.MSG_PRIVATE;
			out.write(msg.getBytes()) ;
			
		}
	}
}
