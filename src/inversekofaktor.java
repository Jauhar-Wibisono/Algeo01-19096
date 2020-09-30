import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class inversekofaktor {
/*public static void main(String[] args) {
	driverinverskofaktor();
}*/
public static void driverinverskofaktor() {
    InputMatrix inputMtrx = new InputMatrix();
    Scanner in = new Scanner(System.in);
    InputMatrix.M = new double[101][101];
    int Nrow, Nkol;

    //input
    inputMtrx.input();
    Nrow = InputMatrix.nBrs;
    Nkol = InputMatrix.nKol;
    //Perhitungan invers
    double hasil[][] = inverskofaktor(InputMatrix.M, Nrow);
	String inversFormat = "";
    if ((hasil[0][0]== -99999999) || (Nkol != Nrow)) inversFormat = "Matriks tidak memiliki invers.";
    else {
    	for (int i=0;i<Nrow;i++) {
    		for (int j=0; j<Nrow; j++) {
    			inversFormat += String.format("%.3f ", hasil[i][j]);
    		}
    		inversFormat += "\n";
    	}
    }
    
    //Output Hasil Invers
    int opt;
    System.out.println("Apakah anda ingin masukan output kedalam file ?");
    System.out.println("1.Ya   2.Tidak");
    opt = in.nextInt();
    while(opt<1 || opt>2){
        System.out.println("Masukan tidak valid, silahkan input ulang");
        opt=in.nextInt();
    }
    if (opt==1){//Hasil Invers dimasukan dalam file
        String namaFile;
        System.out.print("Masukan nama file: ");
        namaFile = in.next();
        try {
            File file = new File("../test"+namaFile);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("../test/"+namaFile);
            fileWriter.write(inversFormat);
            fileWriter.close();
        }
        catch(IOException err){
            System.out.println("Terjadi error dalam pemasukan file");
            err.printStackTrace();
        }
    }
    
    System.out.println(inversFormat);
//  in.close();   
}
public static double[][] inverskofaktor(double[][] matriks, int N){
	if (N == 1) {
		double satu[][] = new double[1][1];
		satu[0][0] = (1/matriks[0][0]);
		return satu;
	}
	double [][]temp = new double[N][N];
	double det = (detKofaktor.determinan(matriks, N));
	if (N==2) {
		double tempo = matriks[0][0];
		matriks[0][0] = matriks[1][1];
		matriks[1][1] = tempo;
		tempo = -matriks[0][1];
		matriks[0][1] = -matriks[1][0];
		matriks[1][0] = tempo;
		temp = matriks;
	}
	else if (N != 0){
	double[][]kecil = new double[N-1][N-1];
	int count1 = 0;
	int count2 = 0;
	int sign = 1;
	for (int i = 0; i<N; i++) {
		if ((i %2 ==0))sign = 1;
		else sign = -1;
		for (int j=0; j<N;j++) {
			count1 =0;
			count2 =0;
			for (int k =0; k<N;k++) {
				if (count2 !=0) {
					count2 = 0;
					count1 +=1;
				}
				for (int l=0; l<N;l++) {
					if ((k != i) && (l != j)) {
						kecil[count1][count2] = matriks[k][l];
						count2 +=1;
					}
			}
		}
		temp[i][j] = sign * detKofaktor.determinan(kecil, N-1);
		if (temp[i][j] == -0) temp[i][j] = 0;
		sign*= -1;
	}
	}
	}
	double [][]transpose = new double[N][N];
	for (int i=0; i<N; i++) { /*diubah jadi adjoin*/
		for (int j=0; j<N; j++) {
			transpose[i][j] = temp[j][i];
		}
	}
	/*hitung invers*/
	if (det == 0 || N == 0) {
		double [][]failure = {
				{-99999999}
		};
		return failure;
	}
	else {
		double[][]hasil = new double[N][N];
		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				hasil[i][j]= (1/det) * transpose[i][j];
		}
	}
	return hasil;
}
}
}