import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class splCramer {
	/*public static void main(String[] args) {
	double [][] M = {
			{2, 3, 1, 32},
			{3, 3, 1, -27},
			{2, 4, 1, -2}
	};
	cramerSPL(M, 3);
}*/
public static int nBrs,nKol, N;
public static double[][] A = new double[101][100];
public static double[][] B = new double[101][1];
public static double[][] Maug = new double[101][101];
public static String[] variable = new String[101];

public void input(){
	nBrs = 0;
	nKol = 0;
    Scanner in = new Scanner(System.in);
    int opt;
    System.out.println("1. Masukkan Matriks dari keyboard");
    System.out.println("2. Masukkan Matriks dari file");
    System.out.print("Input pilihan :");
    opt = in.nextInt();

    while(opt<1 || opt>2){
        System.out.println("Masukan tidak valid, silahkan input ulang");
        opt=in.nextInt();
    }

    if (opt==1){
        System.out.print("Input ukuran matriks (NxN): ");
        N = in.nextInt();
        
        System.out.println("Masukan matriks koefisien (A[i][j])");
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                A[i][j] = in.nextInt();
            }
        }
        
        System.out.println("Masukan matriks hasil persamaan (B[i])");
        for (int i=0;i<N;i++){
            B[i][0] = in.nextInt();
        }
        
        //Matrix Augmented SPL
        for(int i=0;i<N;i++){
            Maug[i][N] = B[i][0];
            for(int j=0;j<N;j++){
                Maug[i][j]= A[i][j];
;
            }
        }
    }else{
        String namaFile;
        boolean error;
        do{
            System.out.print("Masukan nama file: ");
            namaFile = in.next();
            error = false;
            try{
                Scanner  fileCount = new Scanner(new BufferedReader(new FileReader("../test/"+namaFile)));
                while(fileCount.hasNextLine()) {
                    nBrs++;
                    String[] line = fileCount.nextLine().trim().split(" ");
                    nKol = line.length;
                }
                fileCount.close();
                if (nBrs == nKol - 1) N = nBrs;
                else N = -99999999;
                Scanner file = new Scanner(new File("../test/"+namaFile));
                for (int i=0;i<nBrs;i++){
                    for (int j=0;j<nKol;j++){
                        if (file.hasNextDouble()){
                            Maug[i][j] = file.nextDouble();
                        }
                    }
                }
            }
            catch(FileNotFoundException err){
                System.out.println("Terjadi error dalam proses pembacaan file.");
                err.printStackTrace();
                error = true;
            }
        }while (error);
    }
}
public static void output(String[] Var, String[] solusi, int Nvar, boolean isSolve){
    Scanner in = new Scanner(System.in);
    int opt;
    System.out.println("Apakah Anda ingin masukan output kedalam file ?");
    System.out.println("1.Ya   2.Tidak");
    opt = in.nextInt();
    while(opt<1 || opt>2){
        System.out.println("Masukan tidak valid, silahkan input ulang");
        opt=in.nextInt();
    }
    if (opt==1){//Hasil Determinan dimasukan dalam file
        String namaFile;
        System.out.print("Masukan nama file: ");
        namaFile = in.next();
        try {
            File file = new File("../test/"+namaFile);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("../test/"+namaFile);
            String newLine = System.getProperty("line.separator");
            if (isSolve){
                for (int j=0;j<Nvar-1;j++){
                    fileWriter.write(variable[j]+" = "+solusi[j]+newLine);
                }
            }else fileWriter.write("SPL tidak memiliki solusi atau determinan matriks 0 atau bukan matriks persegi.");
            fileWriter.close();
        }
        catch(IOException err){
            System.out.println("Terjadi error dalam pemasukan file");
            err.printStackTrace();
        }
    }

    //Output di command line
    if (isSolve){
        for(int j=0;j<Nvar-1;j++){
            System.out.println(variable[j]+" = "+solusi[j]);
        }
    }else System.out.println("SPL tidak memiliki solusi atau determinan matriks 0.");
}

public static void  cramerSPL(double[][]M, int N) {
	boolean isSolve = true;
	if (N == -99999999) {
		isSolve = false;
		N = 0;
	}
String[] solusi = new String[101];
//Proses invers
/*AX = B -> X = A^-1 * B*/
double [][] A = new double[N][N];
double [][] B = new double [N][1];
for (int i = 0; i<N; i++) {
	B[i][0] = M[i][N];
	for (int j=0; j<N; j++) {
		A[i][j] = M[i][j];
	}
}
double det = detKofaktor.determinan(A, N);
if(det == 0) isSolve = false;
double [][] hasil = new double[N][1];
double [][] temp = new double[N][N];
for (int i = 0; i<N; i++) {
	for (int j=0; j<N; j++) {
		temp[i][j] = A[i][j];
	}
}
for(int j = 0; j < N; j++){
	for(int i = 0; i < N; i++){
		temp[i][j] = B[i][0];
    }
	hasil[j][0] = (detKofaktor.determinan(temp, N))/det;
	for (int k=0; k<N;k++) {
		temp[k][j] = A[k][j];
	}
 }

//Array variabel solusi
for(int i=0;i<N;i++){
    variable[i] = "x" + Integer.toString(i+1);
}
//Solusi SPL unik
for(int i=0;i<N;i++){
	solusi[i] = String.format("%.3f",hasil[i][0]);
        }
output(variable,solusi,N+1,isSolve);
}
}