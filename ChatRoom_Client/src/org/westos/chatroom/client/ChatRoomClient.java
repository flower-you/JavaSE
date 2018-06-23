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
 * �ͻ���
 * @author Apple
 * 
 * ���ող�����˼·:��Ȼʵ���˿ͻ��˺ͷ������˵Ĳ�ͣ�Ľ���,����������������!
 *   �ͻ��˵ķ���Ϣ�Ͷ���Ϣ����һ���߳�,������Ҳһ��,���ܻ����һ������(��������
 *   	
 *   �Ľ�:����Ӧ�ðѶ���Ϣ�ͷ���Ϣ�ĸ��߼��������߳���?
 *   	������Ϣ�߼��������߳���,һ�����߳��в���Ҫ����¼��.
 */
public class ChatRoomClient {
	
	private static Scanner sc;
	private static InputStream in;
	private static OutputStream out;

	public static void main(String[] args) {
		
		try {
			//�����ͻ��˵�Socket����
			Socket s = new Socket("192.168.10.101", 8888);
			
			in = s.getInputStream();
			out = s.getOutputStream();
			
			sc = new Scanner(System.in);
			
			/**
			 * ע���û���
			 */
			//���ϵ�ע��
			while(true){
				System.out.println("�������û���:");
				String username = sc.nextLine() ;
				
				//ͨ��ͨ���ڵ������д��ȥ
				out.write(username.getBytes()) ;
				
				//��ȡ�������ķ���
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;
				String fkMsg = new String(bys, 0, len) ;
				
				//�����ж�
				if(fkMsg.equals("yes")){
					System.out.println("ע���û����ɹ�...");
					break ;
				}else if(fkMsg.equals("no")){
					System.out.println("�û����Ѿ�ע��,����������....");
				}
 			}
			
			
			//�����ͻ��˶���Ϣ�����߳�
			ClientThread ct = new ClientThread(in) ;
			ct.start() ;
			
			
			//�ṩ�˵�ѡ��
			while(true){
				//��Щ�˵����û�����ѡ��
				System.out.println("��ѡ��Ҫ���еĲ���: 1,˽��  2,����  3,�����б� 4 ,�˳�  5,�����ļ�");
				
				//�����ļ�:����:ByteArrayInputStream ByteOutputStream :�ڴ��������������
				//�ڴ������:ֻ��ȥ����С�ļ��Ķ���,��Ӳ���϶�ȡe:/xx.txt--->����f:/xx.txt
				//�ֽ���--->һ�ζ�ȡһ���ֽ������в���  10KB  ����ĳ���ļ��Ĵ�С����û��10kb,ʹ�ÿ����ֽ�������в���
				//���ֽ������С--->����10kb���ֽ���- ��ǰ�ļ��Ĵ�С
				
//				int num = sc.nextInt() ;
				//ʹ�ù�������¼������
				int num = InputUtil.inputIntType(new Scanner(System.in)) ;
				//ѡ��ṹ���:switch���
				switch(num){
				case 1:	//˽��
					privateTalk() ;
					break ;
					
				case 2: //����
					publicTalk();
					break ;
					
				case 3 ://�����б�
					getOnLineList() ;
					break ;
					
				case 4 ://�˳�  �ͻ��˺ͷ������˶���Ҫ�ر�Socket����
					
					break ;
					
				case 5: //�����ļ�
					
					break ;
				}
			}
			
		
			
			
		
			
			//s.close() ;  //close()�������ͻ��׳�:SocketException
		} catch (SocketException e) {
			//�մ��� :���ÿ���̨�쳣��ӡ������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�����б�
	private static void getOnLineList() throws  IOException{
		
		//Ҫ����Լ������Ϣ��ʽ
		//������:��Ϣ����:��Ϣ����
		//��װ��Ϣ :����ֻ�ǻ�ȡ�����б�(������,����ÿһ�������г���),������,��Ϣ������null(ռλ)
		String msg = null +":"+null+":"+Configs.MSG_ONLINELIST ;
		//����ȥ
		out.write(msg.getBytes()) ;
	}

	//����
	private static void publicTalk() throws IOException {
		//�ͻ��˲��ϵķ�����Ϣ,��ȡ����������������Ϣ
		while (true) {
			System.out.println("����ǰ�����˹���ģʽ ��Ϣ��ʽ ������ ��Ϣ���� ��Ϣ���� -q �˳���ǰģʽ");
			// �����Ҫ�˳���ǰģʽ

			String msg = sc.nextLine();
			if ("-q".equals(msg)) {
				break;
			}

			// ������Ϣ�ĸ�ʽ:
			// ��ѭ����ģʽ�ǶԴ��˵�Ļ�:������  �ÿ�null���� 
			msg =null +":" +msg + ":" + Configs.MSG_PUBLIC;
			out.write(msg.getBytes());
		}
	}

	//˽��
	private static void privateTalk() throws IOException {
		//�ͻ��˲��ϵķ�����Ϣ,��ȡ����������������Ϣ
		while(true){
//			System.out.println("��������Ϣ:");
//			String msg = sc.nextLine() ;
//			//ʹ����������͹�ȥ
////			out.write(msg.getBytes()) ;
			System.out.println("����ǰ������˽��ģʽ ��Ϣ��ʽ ������ ��Ϣ���� ������ -q �˳���ǰģʽ");
			//�����Ҫ�˳���ǰģʽ
			
			
			String msg = sc.nextLine() ;
			if("-q".equals(msg)){
				break ;
			}
			
			//������Ϣ�ĸ�ʽ:
			//��ѭ:������:��Ϣ����:��Ϣ����
			//����-->����	����:���:��Ϣ����
			msg = msg + ":"+Configs.MSG_PRIVATE;
			out.write(msg.getBytes()) ;
			
		}
	}
}
