package org.westos.chatroom.client.util;

import java.util.Scanner;

public class InputUtil {
	
	//һֱҪ����¼������Ϊֹ
	public static int inputIntType(Scanner sc) {
		int choose = 0;
		while (true) {
			try {
				//¼���û����������
				choose = sc.nextInt();
				break;
			} catch (Exception e) {
				sc = new Scanner(System.in);
				System.out.println("��������Ͳ���ȷ������������:");
			}
		}
		return choose;
	}
}
