/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * regresi.java
 */

// pending, gak paham sama format masukannya

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class regresi{
	// atribut
	// method
	regresi(){} // konstruktor
	public static double[] regresi(int n, double x[], double y[]){
		// buat matriks SPL
		double a[][]=new double[101][101];
		for (int i=0;i<n;i++){

		}
	}
	public static void driver_regresi(){
		Scanner in=new Scanner(System.in);
		int n=0;
		double x[]=new double[101], y[]=new double[101];
		double qx;
		// baca masukan
		System.out.printf("1. masukan dari keyboard\n2. masukan dari file\n");
		int choice;
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.printf("masukan tidak valid, masukan diulang\n");
			choice=in.nextInt();
		}
		if (choice==1){
			n=in.nextInt();
			for (int i=0;i<n;i++){
				x[i]=in.nextDouble();
				y[i]=in.nextDouble(); 
			}
			qx=in.nextDouble();
		}
		else{ // choice == 2
			// diasumsikan file input berada di folder test dan namanya tidak mengandung spasi
			// diasumsikan nilai x yang akan ditaksir ada di baris terakhir file input
			String s;
			System.out.printf("masukkan nama file (nama file tidak boleh mengandung spasi): ");
			s=in.next();
			try{
				Scanner file=new Scanner(new File("../test/"+s)); 
				n=0;
				while (file.hasNextDouble()){
					double tmp=file.nextDouble();
					if (file.hasNextDouble()){
						x[n]=tmp;
						y[n]=file.nextDouble();
						n++;
					}
					else qx=tmp;
				}
			}
			catch (FileNotFoundException err){
				System.out.printf("terjadi error\n");
				err.printStackTrace();
			}
		}
		// dapatkan persamaan regresi
		double koef[]=regresi(n,x,y);

	}
}