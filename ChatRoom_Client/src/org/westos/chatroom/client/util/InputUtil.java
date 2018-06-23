package org.westos.chatroom.client.util;

import java.util.Scanner;

public class InputUtil {
	
	//一直要我们录入整数为止
	public static int inputIntType(Scanner sc) {
		int choose = 0;
		while (true) {
			try {
				//录入用户输入的整数
				choose = sc.nextInt();
				break;
			} catch (Exception e) {
				sc = new Scanner(System.in);
				System.out.println("输入的类型不正确，请重新输入:");
			}
		}
		return choose;
	}
}
