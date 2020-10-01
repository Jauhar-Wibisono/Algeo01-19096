/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * interpolasi.java
 */

import java.util.Scanner;
import java.io.*;

public class interpolasi{
	// atribut
	// method
	interpolasi(){} // konstruktor
	static double[] GaussJordan(int n, double a[][]){
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
		// selesaikan SPL dengan eliminasi Gauss-Jordan
		return GaussJordan(n,a);
	}
	public static void driver_interpolasi(){
		Scanner in=new Scanner(System.in);
		BufferedReader in2=new BufferedReader(new InputStreamReader(System.in));
		int n=0;
		double x[]=new double[101], y[]=new double[101];
		double qx=0;
		// baca masukan
		System.out.printf("1. Masukan titik dari keyboard\n2. Masukan titik dari file\n");
		int choice;
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.printf("Masukan tidak valid, ulangi masukan\n");
			choice=in.nextInt();
		}
		if (choice==1){
			System.out.printf("Masukkan jumlah titik: ");
			n=in.nextInt();
			System.out.printf("Masukkan titik-titik (x dan y dipisah spasi):\n");
			for (int i=0;i<n;i++){
				x[i]=in.nextDouble();
				y[i]=in.nextDouble(); 
			}
		}
		else{ // choice == 2
			// diasumsikan file input berada di folder test
			boolean error;
			do{
				error=false;
				String s="";
				System.out.printf("Masukkan nama file: ");
				try{
					s=in2.readLine();
				}
				catch (IOException err){
					err.printStackTrace();
				}
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
					err.printStackTrace();
					error=true;
				}
			} while(error);
		}
		// input nilai x yang akan ditaksir nilai fungsinya
		System.out.printf("Masukkan nilai x yang akan ditaksir nilai fungsinya: ");
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
		// cetak jawaban
		System.out.printf("Polinom Interpolasi:\n");
		for (int i=n;i>=0;i--){
			if (i<n){
				if (koef[i]>0) System.out.printf("+");
			}
			System.out.printf("%f x^%d\n",koef[i],i);
		}
		System.out.printf("Taksiran fungsi pada x=%f: %f\n",qx,ans);
		// beri pilihan simpan jawaban
		System.out.printf("Apakah Anda ingin menyimpan jawaban dalam file?\n1. Ya\n2. Tidak\n");
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.printf("Masukan tidak valid, ulangi masukan\n");
			choice=in.nextInt();
		}
		if (choice==1){
			String s="";
			System.out.printf("Masukkan nama file: ");
			try{
					s=in2.readLine();
				}
				catch (IOException err){
					err.printStackTrace();
				}
			try{
				FileWriter filewriter=new FileWriter("../test/"+s);
				filewriter.write("Polinom Interpolasi:\n");
				for (int i=n;i>=0;i--){
					if (i<n){
						if (koef[i]>0) filewriter.write("+");
					}
					filewriter.write(Double.toString(koef[i])+" x^"+Integer.toString(i)+"\n");
				}
				filewriter.write("Taksiran fungsi pada x="+Double.toString(qx)+": "+Double.toString(ans)+"\n");
				filewriter.close();
			}
			catch (IOException err){
				err.printStackTrace();
			}
			System.out.println();
		}
	}
}