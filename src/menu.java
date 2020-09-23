/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * menu.java
 */

import java.util.Scanner;

public class menu{
	public static void main(String args[]){
		Scanner in=(new Scanner(System.in));
		int choice;
		System.out.println("MENU");
		System.out.println("4. interpolasi polinom");
		choice=in.nextInt();
		if (choice==4){
			interpolasi tmp=(new interpolasi());
			tmp.driver_interpolasi();
		}
	}
}