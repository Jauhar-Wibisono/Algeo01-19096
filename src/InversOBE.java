import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InversOBE {
    public double[][] inverse(double[][] M,int N){
        //Pembuatan matriks MI
        double Mtemp[][] = new double[N][N*2];
        for(int i=0;i<N;i++){
            for(int j=N;j<N*2;j++){
                if (i+N==j) M[i][j] = 1;
                else M[i][j+N] = 0;
            }
        }
        Mtemp = M;

        //Proses OBE
        GaussJordan OBE = new GaussJordan();
        Mtemp = OBE.OBEGaussJ(N,N*2,Mtemp);

        //Pengambilan matriks invers pada IM^-1
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                M[i][j] = Mtemp[i][j+N];
            }
        }
        return M;
    }

    public void driverInverse(){
        Scanner in = new Scanner(System.in);
        int N;
        double Minv[][] = new double[101][101];
        //Input Matriks
        InputMatrix mtrx = new InputMatrix();
        mtrx.input();
        N = InputMatrix.nBrs;
        Minv = InputMatrix.M;

        Minv = inverse(Minv, N);

        //Output
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
                String newLine = System.getProperty("line.separator");
                for(int i=0;i<N;i++){
                    for(int j=0;j<N;j++){
                        fileWriter.write(String.format("%.2f",Minv[i][j])+" ");
                    }
                    fileWriter.write(newLine);
                }
                fileWriter.close();
            }
            catch(IOException err){
                System.out.println("Terjadi error dalam pemasukan file");
                err.printStackTrace();
            }
        }
        
        System.out.println("Berikut adalah matriks inverse dari input,");
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                System.out.print(String.format("%.2f",Minv[i][j])+" ");
            }
            System.out.println();
        }
        in.close();
    }
}
