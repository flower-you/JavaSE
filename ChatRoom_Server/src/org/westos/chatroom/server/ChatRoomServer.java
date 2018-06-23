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
 * ��������
 * @author Apple
 */
public class ChatRoomServer {

	public static void main(String[] args) {
		
		try {
			//�����������˵�Socket����
			ServerSocket ss = new ServerSocket(8888) ;
			/**
			 * ����һ��ArrayList����,�ֱ𱣴�ÿ���ͻ������ڵ�ͨ����Scoket
			 */
//			ArrayList<Socket> list = new ArrayList<Socket>() ;
			
			//����HashMap<String,Socket>����,����:���������û�����������Ӧ��ͨ���ڵ�Socket
			HashMap<String, Socket> hm = new HashMap<String, Socket>() ;
			System.out.println("���������ڵȴ�����,���Ժ�...");
			
			//���������:
			int i = 1 ;
			while(true){
				//�����ͻ��˵�����
				Socket s = ss.accept() ; //����
				System.out.println("��"+(i++)+"���ͻ����Ѿ�����...");
				
				//��������һ�������ͻ���������,����ӵ�������
//				list.add(s) ;
				
				//��ȡ������������ͨ���ڵ����������
//				InputStream in = s.getInputStream() ;
//				OutputStream out = s.getOutputStream() ;
				
				//����:������Ϊ�˱����û���,���⿪�������û������߳�
				SaveUserThread st = new SaveUserThread(hm,s) ;
				st.start() ;
				
				
				
				// �����������Ķ�ȡ��Ϣ�����߳�
//				ServerThread st = new ServerThread(in) ;
//				ServerThread st = new ServerThread(s,list) ;
//				st.start() ;
			}
			
			
			//��������¼�����
			//Scanner sc = new Scanner(System.in) ;
			/*//���������ϵĶ���Ϣ,�ͻظ���Ϣ
			while(true){
				
				
				//�ظ���Ϣ
				System.out.println("��ظ���Ϣ:");
				String msgStr = sc.nextLine() ;
				
				//ʹ���������������Ϣ
				out.write(msgStr.getBytes()) ;
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
