package org.westos.chatroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.westos.chartroo.server.configs.Configs;

/**
 * �������˶�ȡ��Ϣ�����߳�
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
			//��ȡͨ���� ����������
			InputStream in = s.getInputStream() ;
			OutputStream out = s.getOutputStream() ;
			while (true) {
				// ���ϵĶ�ȡ�ͻ��˷�����������Ϣ
				byte[] bys = new byte[1024];
				int len = in.read(bys);
				String msg = new String(bys, 0, len);
				//msg���ǿͻ��� ����������Ϣ:  ������:��Ϣ����:������
				System.out.println(msg); //ͨ���������ж��Ƿ��ող�Լ������Ϣ��ʽ
				
				//��ǰ��:��ϢҪ���
			/*	String[] msgs = msg.split(":") ;//msgs[0],msgs[1],msgs[2]
				//�õ�������Ҫ������
//				String receiver = msgs[0] ; //������
				
				
				//������������Ϣ����:Ҫ��ѭ:
				//ת����ʽ
				//������:��Ϣ����:������
				//�ó����������ڵ�ͨ��
				//������get(�����������ÿ���ͻ������ڵ�Socket)
				Socket socket = list.get(Integer.parseInt(msgs[0])); 
				
				//��ȡ���������ڵ�ͨ���ڵ�Scoket����,��ȡ�����д��ȥ
				OutputStream os = socket.getOutputStream();
				os.write((msgs[2]+":"+"����˵:"+msgs[1]).getBytes()) ;*/
				
				/**
				 * �������˵Ķ���Ϣ�����߳�:
				 * �õ��ղſͻ��˷��Ͷ���Ϣ: ������:��Ϣ����:��Ϣ����
				 * 
				 * ��������Ӧ�ý���Ϣ���,������װ--->ת����ʽ
				 * ������:��Ϣ����:��Ϣ����:ʱ��
				 */
				//��Լ������Ϣ�Ĳ��
				String[] msgs = msg.split(":") ;
				String receiver = msgs[0] ; //������
				String msgContent = msgs[1] ;//��Ϣ����
				int msgType = Integer.parseInt(msgs[2]) ; //��Ϣ����
				//��ȡϵͳʱ��
				long time = System.currentTimeMillis() ;
				
				//���ݲ�ͬ����Ϣ����������ͬ�Ĵ���
				if(msgType==Configs.MSG_PRIVATE){
					//���˽����������
					//1)��ȡ���������ڵ�ͨ��
					Socket socket = hm.get(receiver) ;
					//2)��ѭת����ʽ
					//������:��Ϣ����:��Ϣ����:ʱ��
					String zfMsg = username+":"+msgContent+":"+msgType+":"+time;
					
					//ת���ͻ���--->�ͻ������߳�ȥ��
					socket.getOutputStream().write(zfMsg.getBytes()) ;
				}else if(msgType==Configs.MSG_PUBLIC){
					//�����߼�
					//��Ҫ�������ϻ�ȡ���еĿͻ������ڵ�ͨ��,�ų��Լ�
					Set<String> keySet = hm.keySet() ;
					for (String key : keySet) {
						
						//��Ҫ�ų��Լ�
						if(username.equals(key)){
							continue ;
						}
						
						//��ȡ�����Լ����ڵ�ͨ���ڵ�Socket
						Socket socket = hm.get(key) ;
						//Ҫ��ѭת����ʽ
						//������:��Ϣ����:��Ϣ����:ʱ��
						String zfMsg = username + ":" + msgContent + ":"
								+ msgType + ":" + time;
						socket.getOutputStream().write(zfMsg.getBytes()) ;
						
					}
				}else if(msgType==Configs.MSG_ONLINELIST){
					
					//�ַ���������
					StringBuffer sb = new StringBuffer() ;
					//�����ʼ������
					int i = 1 ;
					//��ȡ�����б�
					//�߼�:����HashMap<String,Socket>����,��ȡ�û�
					Set<String> keySet = hm.keySet() ;
					for (String key : keySet) {
						
						//��Ҫ�ų��Լ�
						if(username.equals(key)){
							continue ;
						}
						
						//1,����
						//2,����
						//3,����
						//��Ҫһ������,����Щ�˵�����װ����
						sb.append((i++)).append(",").append(key).append("\n") ;
					}
					//��ȡ�û���ͨ����socket?��ȡ���������ڵ�ͨ��,д��������
					//��Ҫ��ѭ������ת����ʽ
					//������:��Ϣ����:��Ϣ����:ʱ��
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
