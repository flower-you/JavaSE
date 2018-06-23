package org.westos.chatroom.client;

import java.io.IOException;
import java.io.InputStream;

import org.westos.chatroom.client.configs.Configs;
import org.westos.chatroom.client.util.TimeUtil;

/**
 * 客户端读取消息的子线程...
 * @author Apple
 */
public class ClientThread extends Thread {
	private InputStream in ;
	
	public ClientThread(InputStream in) {
		this.in = in ;
	}

	@Override
	public void run() {
		
		try {
			while(true){
				//不断的读消息
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;//实际字节数
				String msgStr = new String(bys, 0, len) ;
//				System.out.println(msgStr);
				//客户端读取消息的子线程:得到服务器转发过来消息:
				//格式:发送者:消息内容:消息类型:时间
				//拆分消息:拿到我们需要的数据
				String[] msgs = msgStr.split(":") ;
				String sender = msgs[0] ;//发送者
				String msgContent = msgs[1] ;//消息内容
				int msgType = Integer.parseInt(msgs[2]) ;//消息类型
				//String--->long  --->Long,Byte,Short,都存在这些方法 都和自己对应的基本数据能进行转换
				long msgTime = Long.parseLong(msgs[3]) ;
				
				//时间毫秒值-->具体的日期对象-->文本格式
				//需要将Date日期--->具体的年月日实时分秒:
				/*Date d = new Date(msgTime) ;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒") ;
				String time = sdf.format(d) ;*/
				String timeStr = TimeUtil.changeMils2Date(msgTime, "yyyy年MM月dd日 HH时mm分ss秒") ;
				//拿到了需要的数据
				//根据不同的消息类型,做出不同的展示
				if(msgType==Configs.MSG_PRIVATE){
					//显示系统时间
					System.out.println(timeStr);
					System.out.println(sender+"对你说:"+msgContent);
				}else if(msgType==Configs.MSG_ONLINE){
					//上线
					System.out.println(timeStr);
					System.out.println(sender+":"+msgContent);
				}else if(msgType==Configs.MSG_PUBLIC){
					//公聊
					System.out.println(timeStr);
					System.out.println(sender+"对大家说:"+msgContent);
				}else if(msgType==Configs.MSG_ONLINELIST){
					//在线列表
					System.out.println(timeStr);
					System.out.println("当前在线好友");
					System.out.println(msgContent);
				}
				
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
