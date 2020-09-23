/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * interpolasi.java
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class interpolasi{
	// atribut
	// method
	interpolasi(){}
	static float[] tmp_GaussJordan(int n, float a[][]){
		// buat matriks eselon baris
		for (int i=0;i<n;i++){
			if (Math.abs(a[i][i])<1e-7){
				// diagonal sekarang nol, cari yang tidak nol
				boolean found=false;
				int j=i+1;
				while (j<n && !found){
					if (Math.abs(a[i][j])>1e-7){
						found=true;
						// tukar baris
						for (int k=i;k<=n;k++){
							float tmp=a[i][k];
							a[i][k]=a[j][k];
							a[j][k]=tmp;
						}
					}
				}
			}
			if (Math.abs(a[i][i])>1e-7){
				// eliminasi kolom i
				float tmp1=1/a[i][i];
				for (int j=i;j<=n;j++) a[i][j]*=tmp1;
				for (int j=i+1;j<n;j++){
					float tmp2=a[j][i]/a[i][i];
					for (int k=i;k<=n;k++) a[j][k]-=tmp2*a[i][k];
				}
			}
		}
		// buat matriks eselon baris tereduksi
		for (int i=n-1;i>=0;i--){
			for (int j=i-1;j>=0;j--){
				float tmp=a[j][i]/a[i][i];
				for (int k=i;k<=n;k++) a[j][k]-=tmp*a[i][k];
			}
		}
		// buat array solusi
		float ret[]=new float[101];
		for (int i=0;i<n;i++) ret[i]=a[i][n];
		return ret;
	}
	public static float interpolate(int n, float x[], float y[], float qx){
		// buat matriks SPL
		float a[][]=new float[101][101];
		for (int i=0;i<n;i++){
			float tmp=1;
			for (int j=0;j<n;j++){
				a[i][j]=tmp;
				tmp*=x[i];
			}
			a[i][n]=y[i];
		}
		// selesaikan SPL dengan Gauss-Jordan
		float koef[]=tmp_GaussJordan(n,a);
		// hitung f(qx), f fungsi polinom hasil interpolasi
		float ret=0;
		float tmp=1;
		for (int i=0;i<n;i++){
			ret+=koef[i]*tmp;
			tmp*=qx;
		}
		return ret;
	}
	public static void driver_interpolasi(){
		Scanner in=(new Scanner(System.in));
		int n=0;
		float x[]=(new float[101]), y[]=(new float[101]);
		float qx=0, ans;
		System.out.println("1. masukan dari keyboard");
		System.out.println("2. masukan dari file");
		int choice;
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.println("masukan tidak valid, masukan diulang");
			choice=in.nextInt();
		}
		if (choice==1){
			n=in.nextInt();
			for (int i=0;i<n;i++){
				x[i]=in.nextFloat();
				y[i]=in.nextFloat(); 
			}
			qx=in.nextFloat();
		}
		else{ // choice == 2
			// diasumsikan file input berada di folder test dan namanya tidak mengandung spasi
			// diasumsikan nilai x yang akan ditaksir ada di baris terakhir file input
			String s;
			System.out.print("masukkan nama file: ");
			s=in.next();
			try{
				Scanner file=(new Scanner(new File("../test/"+s))); 
				n=0;
				while (file.hasNextFloat()){
					float tmp=file.nextFloat();
					if (file.hasNextFloat()){
						x[n]=tmp;
						y[n]=file.nextFloat();
						n++;
					}
					else qx=tmp;
				}
			}
			catch (FileNotFoundException err){
				System.out.println("terjadi error");
				err.printStackTrace();
			}
		}
		ans=interpolate(n,x,y,qx);
		System.out.println(ans);
		System.out.println("Apakah Anda ingin menyimpan jawaban dalam file?");
		System.out.println("1. ya");
		System.out.println("2. tidak");
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.println("masukan tidak valid, masukan diulang");
			choice=in.nextInt();
		}
		if (choice==1){
			String s;
			System.out.print("masukkan nama file: ");
			s=in.next();
			try{
				File file=(new File("../test"+s));
				file.createNewFile();
				FileWriter filewriter=(new FileWriter("../test"+s));
				filewriter.write(Float.toString(ans));
				filewriter.close();
			}
			catch (IOException err){
				System.out.println("terjadi error");
				err.printStackTrace();
			}
		}
	}
}