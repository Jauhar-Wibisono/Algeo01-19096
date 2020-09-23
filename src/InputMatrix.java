import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

public class InputMatrix {
    // Input matrix bakal bikin variabel nBrs, nKol, sama matrix M
    // Jadi assign variabel ini ke variabel lokal aja
    // Buat akses variabel ini di class lain pake "InputMatrix.M"
    public static int nBrs,nKol;
    public static double[][] M;
    public void input(){
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
            System.out.print("Input Jumlah Baris&Kolom:");
            nBrs = in.nextInt();
            nKol = nBrs;
            
            for(int i=0;i<nBrs;i++){
                for(int j=0;j<nKol;j++){
                    M[i][j] = in.nextInt();
                }
            }
        }else{
            String namaFile;
            System.out.print("Masukan nama file: ");
            namaFile = in.next();
            
            try{
                Scanner  fileCount = new Scanner(new BufferedReader(new FileReader("../test/"+namaFile)));
                while(fileCount.hasNextLine()) {
                    nBrs++;
                    String[] line = fileCount.nextLine().trim().split(" ");
                    nKol = line.length;
                }
                fileCount.close();

                Scanner file = new Scanner(new File("../test/"+namaFile));
                for (int i=0;i<nBrs;i++){
                    for (int j=0;j<nKol;j++){
                        if (file.hasNextDouble()){
                            M[i][j] = file.nextDouble();
                        }
                    }
                }
            }
            catch(FileNotFoundException err){
                System.out.println("Terjadi error dalam proses pembacaan file.");
                err.printStackTrace();
            }
        }
    }
}
