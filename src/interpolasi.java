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
	interpolasi(){} // konstruktor
	static double[] tmp_GaussJordan(int n, double a[][]){
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
							double tmp=a[i][k];
							a[i][k]=a[j][k];
							a[j][k]=tmp;
						}
					}
				}
			}
			if (Math.abs(a[i][i])>1e-7){
				// eliminasi kolom i
				double tmp1=1/a[i][i];
				for (int j=i;j<=n;j++) a[i][j]*=tmp1;
				for (int j=i+1;j<n;j++){
					double tmp2=a[j][i]/a[i][i];
					for (int k=i;k<=n;k++) a[j][k]-=tmp2*a[i][k];
				}
			}
		}
		// buat matriks eselon baris tereduksi
		for (int i=n-1;i>=0;i--){
			// eliminasi kolom i
			for (int j=i-1;j>=0;j--){
				double tmp=a[j][i]/a[i][i];
				for (int k=i;k<=n;k++) a[j][k]-=tmp*a[i][k];
			}
		}
		// buat array solusi
		double ret[]=new double[101];
		for (int i=0;i<n;i++) ret[i]=a[i][n];
		return ret;
	}
	public static double[] interpolate(int n, double x[], double y[]){
		// buat matriks SPL
		double a[][]=new double[101][101];
		for (int i=0;i<n;i++){
			double tmp=1;
			for (int j=0;j<n;j++){
				a[i][j]=tmp;
				tmp*=x[i];
			}
			a[i][n]=y[i];
		}
		// selesaikan SPL dengan Gauss-Jordan
		return tmp_GaussJordan(n,a);
	}
	public static void driver_interpolasi(){
		Scanner in=new Scanner(System.in);
		int n=0;
		double x[]=new double[101], y[]=new double[101];
		double qx=0;
		// baca masukan
		System.out.printf("1. masukan titik dari keyboard\n2. masukan titik dari file\n");
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
					x[n]=file.nextDouble();
					y[n]=file.nextDouble();
					n++;
				}
			}
			catch (FileNotFoundException err){
				System.out.printf("terjadi error\n");
				err.printStackTrace();
			}
		}
		// input nilai x yang akan ditaksir nilai fungsinya
		System.out.printf("masukkan nilai x yang akan ditaksir nilai fungsinya: ");
		qx=in.nextDouble();
		// dapatkan polinom interpolasi
		double koef[]=interpolate(n,x,y);
		// hitung nilai taksiran fungsi di x=qx
		double ans=0;
		double tmp=1;
		for (int i=0;i<n;i++){
			ans+=tmp*koef[i];
			tmp*=qx;
		}
		System.out.printf("polinom interpolasi:\n");
		for (int i=n;i>=0;i--){
			if (i<n){
				if (koef[i]>0) System.out.printf("+");
			}
			System.out.printf("%fx^%d\n",koef[i],i);
		}
		System.out.printf("taksiran fungsi pada x=%f: %f\n",qx,ans);
		// beri pilihan simpan jawaban
		System.out.printf("Apakah Anda ingin menyimpan jawaban dalam file?\n1. ya\n2. tidak\n");
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.printf("masukan tidak valid, masukan diulang\n");
			choice=in.nextInt();
		}
		if (choice==1){
			String s;
			System.out.printf("masukkan nama file: ");
			s=in.next();
			try{
				File file=(new File("../test"+s));
				file.createNewFile();
				FileWriter filewriter=new FileWriter("../test/"+s);
				filewriter.write("polinom interpolasi:\n");
				for (int i=n;i>=0;i--){
					if (i<n){
						if (koef[i]>0) filewriter.write("+");
					}
					filewriter.write(Double.toString(koef[i])+"x^"+Integer.toString(i)+"\n");
				}
				filewriter.write("taksiran fungsi pada x="+Double.toString(qx)+": "+Double.toString(ans)+"\n");
				filewriter.close();
			}
			catch (IOException err){
				System.out.printf("terjadi error\n");
				err.printStackTrace();
			}
		}
	}
}