/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * menu.java
 */

import java.util.Scanner;

public class menu{
	public static void main(String args[]){
		boolean keluar=false;
		int choice;
		while (!keluar){
			Scanner in=(new Scanner(System.in));
			System.out.println("MENU");
			System.out.printf("1. Sistem Persamaan Linier\n2. Determinan\n3. Matriks Balikan\n4. Interpolasi Polinom\n5. Regresi Linear Berganda\n6. Keluar\n");
			choice=in.nextInt();
			while (choice<1 || choice>6){
				System.out.printf("Masukan tidak valid, ulangi masukan\n");
				choice=in.nextInt();
			}
			if (choice==1){
				// SPL
				System.out.printf("1. Metode Eliminasi Gauss\n2. Metode Eliminasi Gauss-Jordan\n3. Metode Matriks Balikan\n4. Kaidah Cramer\n");
				choice=in.nextInt();
				while (choice<1 || choice>4){
					System.out.printf("Masukan tidak valid, ulangi masukan\n");
					choice=in.nextInt();
				}
				if (choice==1){
					// Metode eliminasi Gauss
					SPL tmp=new SPL();
					tmp.input();
					tmp.SPLGauss(tmp.Maug,tmp.nBrs,tmp.nKol);
				}
				else if (choice==2){
					// Metode eliminasi Gauss-Jordan
					SPL tmp=new SPL();
					tmp.input();
					tmp.SPLGaussJordan(tmp.Maug,tmp.nBrs,tmp.nKol);
				}
				else if (choice==3){
					// Metode matriks balikan
					splinverse tmp=new splinverse();
					tmp.input();
					System.out.printf("%d",tmp.N);
					tmp.inversSPL(tmp.Maug,tmp.N);
				}
				else if (choice == 4){
					// Kaidah Cramer
					splCramer tmp=new splCramer();
					tmp.input();
					tmp.cramerSPL(tmp.Maug,tmp.N);
				}
			}
			else if (choice==2){
				// Determinan
				System.out.printf("1. Metode reduksi baris\n2. Metode ekspansi kofaktor\n");
				choice=in.nextInt();
				while (choice<1 || choice>2){
					System.out.printf("Masukan tidak valid, ulangi masukan\n");
					choice=in.nextInt();
				}
				if (choice==1){
					// Metode reduksi baris
					DetReduksi tmp=new DetReduksi();
					tmp.driverDetReduksi();
				}
				else if (choice ==2){
					// Metode ekspansi kofaktor
					detKofaktor tmp=new detKofaktor();
					tmp.driverdetKofaktor();
				}
			}
			else if (choice==3){
				// Matriks balikan
				System.out.printf("1. Metode reduksi baris\n2. Metode ekspansi kofaktor\n");
				choice=in.nextInt();
				while (choice<1 || choice>2){
					System.out.printf("Masukan tidak valid, ulangi masukan\n");
					choice=in.nextInt();
				}
				if (choice==1){
					// Metode reduksi baris
					InversOBE tmp=new InversOBE();
					tmp.driverInverse();
				}
				else if (choice == 2){
					// Metode ekspansi kofaktor
					inversekofaktor tmp=new inversekofaktor();
					tmp.driverinverskofaktor();
				}
			}
			else if (choice==4){
				// Interpolasi polinom
				interpolasi tmp=new interpolasi();
				tmp.driver_interpolasi();
			}
			else if (choice==5){
				// Regresi linier berganda
				regresi tmp=new regresi();
				tmp.driver_regresi();
			}
			else if (choice == 6){
				keluar=true;
			}
			System.out.println();
		}
	}
}