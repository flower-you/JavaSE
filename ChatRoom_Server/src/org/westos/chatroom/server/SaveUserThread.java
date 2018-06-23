package org.westos.chatroom.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.westos.chartroo.server.configs.Configs;

/**
 * �����������û������߳�
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
			//���ϵĶ�ȡ�ͻ��˷��͵��û���
			while(true){
				byte[] bys = new byte[1024] ;
				int len = in.read(bys) ;
				username = new String(bys, 0, len);
				
				//��ȡ���û���,��������Ҫ�����û����Ƿ����,���Ҹ��ͻ��˷�����ȥ
				//����ж�:ֱ��ʹ��Map�����еĹ���:ContainsKey(String key):�жϼ������Ƿ����ָ���ļ�
				if(!hm.containsKey(username)){//�û���������
					
					//������,����Ӽ�����
					hm.put(username, s) ;
					
					//���ͻ��˷���
					out.write("yes".getBytes()) ;
					break ;
				}else{
					//�������,����Ҫ�ٽ��д洢
					//���ͻ��˷���
					out.write("no".getBytes()) ;
				}
				
			}
			/**
			 * �������ѹ���:
			 */
			//����HashMap����,��ȡ���û�����Ӧ��ͨ��Socket����,�ų��Լ�!
			//����HashMap<String,Socket>����
			//entrySet():��ȡ��ֵ�Զ��� 
			//keySet():��ȡ���еļ��ļ���
			Set<String> keySet = hm.keySet() ;
			for (String key : keySet) {
				
				//����ų��Լ�
				if(username.equals(key)){
					continue ; //������ǰѭ��,����������һ��ѭ��
				}
				
				//ͨ�����ϻ�ȡ
				Socket socket = hm.get(key) ;  //��ȡ��ÿһ���û�����Ӧ��ͨ���ڵ�Socket
				
				//ͨ���û����ڵ�ͨ��Sokcet�����ȡ���������
//				socket.getOutputStream().write((username+"������").getBytes()) ;
				
				//�ñ����û����߳��Ƿ����������߳�-->ҲҪ��ѭ������ת����ʽ
				//������:��Ϣ����:��Ϣ����:ʱ��
				
				OutputStream os = socket.getOutputStream() ;
				//����ת��
				String zfMsg = username + ":" + "������" + ":"
						+ Configs.MSG_ONLINE + ":" + System.currentTimeMillis();
				
				//ת����ȥ
				os.write(zfMsg.getBytes()) ;
				
			}
			
			// ע��ɹ�֮��,�����￪���������Ķ�ȡ��Ϣ�����߳�
			new ServerThread(hm, s,username).start() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
	}
}
