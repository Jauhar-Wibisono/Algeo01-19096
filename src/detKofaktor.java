import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.Scanner;

public class detKofaktor {
	/*public static void main(String[] args) {
		driverdetKofaktor();
	}*/
    public static void driverdetKofaktor() {
        InputMatrix inputMtrx = new InputMatrix();
        Scanner in = new Scanner(System.in);
        int Nrow, Nkol;

        //input
        inputMtrx.input();
        Nrow = InputMatrix.nBrs;
        Nkol = InputMatrix.nKol;
        String detFormat;
        if (Nrow != Nkol) {
        	detFormat = "Bukan matriks persegi, tidak mempunyai determinan.";
        }
        else {
            //Perhitungan determinan
            double hasil = determinan(InputMatrix.M, Nrow);
            if (Math.abs(hasil)==0) hasil = Math.abs(hasil);
            detFormat = String.format("%.3f",hasil);
        }
        //Output Hasil Determinan
        int opt;
        System.out.println("Apakah anda ingin masukan output kedalam file ?");
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
                File file = new File("../test"+namaFile);
                file.createNewFile();
                FileWriter fileWriter = new FileWriter("../test/"+namaFile);
                fileWriter.write(detFormat);
                fileWriter.close();
            }
            catch(IOException err){
                System.out.println("Terjadi error dalam pemasukan file");
                err.printStackTrace();
            }
        }
        System.out.println("Determinan = "+detFormat);
//      in.close();   
}
public static double determinan(double[][] matriks, int N) {
	int i,j,k,y,l;
	double [][]temp  = new double [N][N];
	double det = 0;
	boolean geser;
	i = 0;
	if (N ==2) return (matriks[0][0] *matriks[1][1]) - (matriks[0][1]*matriks[1][0]);
	else{
		for (j = 0; j<N;j++){
			k = i+1;
			while (k<N){
				geser = false;
				l = 0;
				while (l<(N-1)){
					if (l ==j || geser) {
						y = (l+1) % (N);
						temp[k-1][l] = matriks[k][y];
						geser = true;
					}
					else temp[k-1][l] = matriks[k][l];
					l++;
				}
				k++;
			}
			det += (Math.pow(-1, (i+j)))*(matriks[i][j])*determinan(temp, N-1);	
		}
		if (det == -0) det = 0;
		return det;
	}
}
}