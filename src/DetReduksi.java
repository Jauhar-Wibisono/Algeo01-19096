import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DetReduksi{
    public void driverDetReduksi() {
        InputMatrix inputMtrx = new InputMatrix();
        Scanner in = new Scanner(System.in);
        InputMatrix.M = new double[101][101];
        double det=1;
        int Nrow,Ncol;

        //input
        inputMtrx.input();
        Nrow = InputMatrix.nBrs; Ncol = InputMatrix.nKol;

        //Mengubah matriks dengan OBE
        double Mtemp[][] = new double[Nrow][Ncol];
        Gauss metode= new Gauss();
        Mtemp = metode.OBEGauss(Nrow,Ncol,InputMatrix.M,false);

        for (int i=0;i<Nrow;i++){
            det *= Mtemp[i][i];
        }

        if (Math.abs(det)==0) det = Math.abs(det);
        String detFormat = String.format("%.3f",det); 

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
        in.close();
    }
}