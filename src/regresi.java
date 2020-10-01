/*
 * Author: Jauhar Wibisono 13519160
 * 23 September 2020
 * regresi.java
 */

import java.util.Scanner;
import java.io.*;

public class regresi{
	// atribut
	// method
	regresi(){} // konstruktor
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
	public static double[] regresi(int n, int m, double x[][], double y[]){
		// buat matriks tmp, yaitu matriks augmented [1|x|y]
		double tmp[][]=new double[101][101];
		for (int i=0;i<m;i++) tmp[i][0]=1;
		for (int j=1;j<=n;j++){
			for (int i=0;i<m;i++){
				tmp[i][j]=x[i][j-1];
			}
		}
		for (int i=0;i<m;i++) tmp[i][n+1]=y[i];
		// buat matriks SPL
		double a[][]=new double[101][101];
		for (int i=0;i<=n;i++){
			for (int j=0;j<=n+1;j++){
				a[i][j]=0;
				for (int k=0;k<m;k++){
					a[i][j]+=tmp[k][i]*tmp[k][j];
				}
			}
		}
		// selesaikan SPL dengan eliminasi Gauss-Jordan
		return GaussJordan(n+1,a);
	}
	public static void driver_regresi(){
		Scanner in=new Scanner(System.in);
		BufferedReader in2=new BufferedReader(new InputStreamReader(System.in));
		int n=0, m=0;
		double x[][]=new double[101][101], y[]=new double[101];
		double xk[]=new double[101];
		// baca masukan
		System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\n");
		int choice;
		choice=in.nextInt();
		while(choice<1 || choice>2){
			System.out.printf("Masukan tidak valid, masukan diulang\n");
			choice=in.nextInt();
		}
		if (choice==1){
			System.out.printf("Masukkan banyak peubah x: ");
			n=in.nextInt();
			System.out.printf("Masukkan banyak persamaan: ");
			m=in.nextInt();
			System.out.printf("Masukkan %d baris persamaan:\n",m);
			for (int i=0;i<m;i++){
				for (int j=0;j<n;j++) x[i][j]=in.nextDouble();
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
					BufferedReader file=new BufferedReader(new FileReader(new File("../test/"+s))); 
					String line;
					n=0;
					m=0;
					try{
						while ((line=file.readLine())!=null){
							String parts[]=line.split(" ");
							n=parts.length-1;
							for (int i=0;i<n;i++) x[m][i]=Double.parseDouble(parts[i]);
							y[m]=Double.parseDouble(parts[n]);
							m++;
						}
					}
					catch(IOException err){
						err.printStackTrace();
					}
				}
				catch (FileNotFoundException err){
					err.printStackTrace();
					error=true;
				}
			} while (error);
		}
		// imput nilai-nilai x yang akan ditaksir nilai fungsinya
		System.out.printf("Masukkan %d nilai x yang akan ditaksir nilai fungsinya:\n",n);
		for (int i=0;i<n;i++) xk[i]=in.nextDouble();
		// dapatkan persamaan regresi
		double b[]=regresi(n,m,x,y);
		// hitung nilai taksiran
		double ans=b[0];
		for (int i=0;i<n;i++) ans+=b[i+1]*xk[i];
		// cetak jawaban
		System.out.printf("Persamaan Regresi:\n");
		for (int i=0;i<=n;i++){
			if (i>0){
				if (b[i]>0) System.out.printf("+");
			}
			if (i==0) System.out.printf("%f\n",b[i]);
			else System.out.printf("%f x%d\n",b[i],i);
		}
		System.out.printf("Nilai taksiran: %f\n",ans);
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
				filewriter.write("Persamaan Regresi:\n");
				for (int i=0;i<=n;i++){
					if (i>0){
						if (b[i]>0) filewriter.write("+");
					}
					if (i==0) filewriter.write(Double.toString(b[i])+"\n");
					else filewriter.write(Double.toString(b[i])+" x"+Integer.toString(i)+"\n");
				}
				filewriter.write("Nilai taksiran pada x=[");
				for (int i=0;i<n;i++){
					if (i>0) filewriter.write(",");
					filewriter.write(Double.toString(xk[i]));
				}
				filewriter.write("]\nadalah "+Double.toString(ans));
				filewriter.close();
			}
			catch (IOException err){
				err.printStackTrace();
			}
			System.out.println();
		}
	}
}