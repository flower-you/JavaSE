package org.westos.chatroom.client;

import java.io.IOException;
import java.io.InputStream;

import org.westos.chatroom.client.configs.Configs;
import org.westos.chatroom.client.util.TimeUtil;

/**
 * �ͻ��˶�ȡ��Ϣ�����߳�...
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
				//���ϵĶ���Ϣ
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;//ʵ���ֽ���
				String msgStr = new String(bys, 0, len) ;
//				System.out.println(msgStr);
				//�ͻ��˶�ȡ��Ϣ�����߳�:�õ�������ת��������Ϣ:
				//��ʽ:������:��Ϣ����:��Ϣ����:ʱ��
				//�����Ϣ:�õ�������Ҫ������
				String[] msgs = msgStr.split(":") ;
				String sender = msgs[0] ;//������
				String msgContent = msgs[1] ;//��Ϣ����
				int msgType = Integer.parseInt(msgs[2]) ;//��Ϣ����
				//String--->long  --->Long,Byte,Short,��������Щ���� �����Լ���Ӧ�Ļ��������ܽ���ת��
				long msgTime = Long.parseLong(msgs[3]) ;
				
				//ʱ�����ֵ-->��������ڶ���-->�ı���ʽ
				//��Ҫ��Date����--->�����������ʵʱ����:
				/*Date d = new Date(msgTime) ;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��") ;
				String time = sdf.format(d) ;*/
				String timeStr = TimeUtil.changeMils2Date(msgTime, "yyyy��MM��dd�� HHʱmm��ss��") ;
				//�õ�����Ҫ������
				//���ݲ�ͬ����Ϣ����,������ͬ��չʾ
				if(msgType==Configs.MSG_PRIVATE){
					//��ʾϵͳʱ��
					System.out.println(timeStr);
					System.out.println(sender+"����˵:"+msgContent);
				}else if(msgType==Configs.MSG_ONLINE){
					//����
					System.out.println(timeStr);
					System.out.println(sender+":"+msgContent);
				}else if(msgType==Configs.MSG_PUBLIC){
					//����
					System.out.println(timeStr);
					System.out.println(sender+"�Դ��˵:"+msgContent);
				}else if(msgType==Configs.MSG_ONLINELIST){
					//�����б�
					System.out.println(timeStr);
					System.out.println("��ǰ���ߺ���");
					System.out.println(msgContent);
				}
				
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
