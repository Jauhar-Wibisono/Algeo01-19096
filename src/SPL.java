import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SPL {
    public static int nBrs,nKol;
    public static double[][] A = new double[101][100];
    public static double[][] B = new double[101][1];
    public static double[][] Maug = new double[101][101];
    public static String[] variable = new String[101];
    
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
            System.out.print("Input Jumlah Persamaan:");
            nBrs = in.nextInt();
            System.out.print("Input Jumlah Variabel:");
            nKol = in.nextInt();
            
            System.out.println("Masukan matriks koefisien (A[i][j])");
            for(int i=0;i<nBrs;i++){
                for(int j=0;j<nKol;j++){
                    A[i][j] = in.nextInt();
                }
            }
            
            System.out.println("Masukan matriks hasil persamaan (B[i])");
            for (int i=0;i<nBrs;i++){
                B[i][0] = in.nextInt();
            }
            
            //Matrix Augmented SPL
            nKol++;
            for(int i=0;i<nBrs;i++){
                for(int j=0;j<nKol;j++){
                    if (j!=nKol) Maug[i][j]= A[i][j];
                    else Maug[i][j] = B[i][0];
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
                            Maug[i][j] = file.nextDouble();
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

    public void output(String[] Var, String[] solusi, int Nvar, boolean isSolve){
        Scanner in = new Scanner(System.in);
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
                if (isSolve){
                    for (int j=0;j<Nvar-1;j++){
                        fileWriter.write(variable[j]+" = "+solusi[j]+newLine);
                    }
                }else fileWriter.write("SPL tidak memiliki solusi");
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
        }else System.out.println("SPL tidak memiliki solusi");
    }

    public void SPLGauss(double[][] M,int Nbar,int Ncol){
        String[] solusi = new String[101];
        //Proses OBE
        Gauss OBE = new Gauss();
        M = OBE.OBEGauss(Nbar, Ncol, M, true);

        boolean solusiUnik, isSolve=true;

        //Array variabel solusi
        for(int i=0;i<Ncol-1;i++){
            variable[i] = "x" + Integer.toString(i+1);
        }

        //Cek ada solusi atau tidak
        if (checkBar(M, Nbar-1, Ncol-1) && M[Nbar-1][Ncol-1]!=0){
            isSolve=false;
        }
        else {//Ada solusi
            //Elminasi baris yang elemennya 0 semua
            for(int i=Nbar-1;i>=0;i--){
                if(checkBar(M, i, Ncol)) Nbar--;
            }
            
            //Periksa jumlah solusi yang ada
            if(Nbar<(Ncol-1)) solusiUnik=false;
            else solusiUnik=true;

            if (solusiUnik){//Solusi SPL unik
                for(int i=Nbar-1;i>=0;i--){
                    double temp = M[i][Ncol-1];
                    for(int j=i+1;j<Ncol-1;j++){
                        double value = Double.parseDouble(solusi[j]);
                        temp -= (M[i][j])*value;
                    }
                    solusi[i] = String.format("%.2f",temp);
                }
            }else{//Solusi SPL lebih dari 1
                char param = 't';
                int Nvar = Ncol-(1+Nbar);
                double[][] koefparam = new double[Ncol][Nvar+1];
                for(int i=0;i<Ncol;i++){
                    for(int j=0;j<=Nvar;j++){
                        koefparam[i][j]=0;
                    }
                }

                //Permisalan variabel
                for(int j=Ncol-2;j>=Nbar;j--){
                    solusi[j] = Character.toString(param);
                    param--;
                    koefparam[j][j-Nvar]=1;    
                }

                //Perhitungan solusi
                for(int i=Nbar-1;i>=0;i--){
                    String temp="";
                    koefparam[i][0] = M[i][Ncol-1];
                    //Subtitusi mundur
                    for(int j=i+1;j<Ncol-1;j++){
                        //Penyimpanan koefisen variabel yang dimisalkan
                        for(int l=0;l<=Nvar;l++){
                            koefparam[i][l] -= M[i][j]*koefparam[j][l];
                        } 
                    }
                    //Buat persamaan parametrik
                    boolean zero=false;
                    for(int l=0;l<=Nvar;l++){
                        double value=koefparam[i][l];
                        String op;
                        //Cek mines
                        if (value<0) {op="-"; value = Math.abs(value);}
                        else op="+";

                        if(value==0) {//Koefisien 0
                            if(l==0) zero = true;
                        }
                        else if(value==1){//Koefisien 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + solusi[l+Nvar]; zero=false;}
                            else temp = temp + op + solusi[l+Nvar];
                        }
                        else{//Koefisien > 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + String.format("%.2f",value)+solusi[l+Nvar]; zero=false;}
                            else temp = temp + op + String.format("%.2f",value)+solusi[l+Nvar];
                        }
                    } 
                    solusi[i]=temp;
                }
            }
        }
        output(variable,solusi,Ncol,isSolve);
    }
    
    public void SPLGaussJordan(double[][] M,int Nbar,int Ncol){
        String[] solusi = new String[100];
        //Proses OBE
        GaussJordan OBE = new GaussJordan();
        M = OBE.OBEGaussJ(Nbar, Ncol, M);

        //Array variabel solusi
        for(int i=0;i<Ncol-1;i++){
            variable[i] = "x" + Integer.toString(i+1);
        }

        boolean solusiUnik, isSolve=true;
        //Periksa ada solusi SPL atau tidak
        if (checkBar(M, Nbar-1, Ncol-1) && M[Nbar-1][Ncol-1]!=0){
            isSolve=false;
        }
        else{
            //Eliminasi baris yang elemen nya 0 semua
            for(int i=Nbar-1;i>=0;i--){
                if(checkBar(M, i, Ncol)) Nbar--;
            }
            
            //Periksa jumlah solusi yang ada
            if(Nbar<(Ncol-1)) solusiUnik=false;
            else solusiUnik=true;

            if(solusiUnik){//Solusi SPL unik
                for(int i=0;i<Nbar;i++){
                    solusi[i]=String.format("%.2f",M[i][Ncol-1]);
                }
            }else{//Solusi lebih dari 1
                char param = 't';
                int Nvar = Ncol-(1+Nbar);
                double[][] koefparam = new double[Ncol][Nvar+1];
                for(int i=0;i<Ncol;i++){
                    for(int j=0;j<=Nvar;j++){
                        koefparam[i][j]=0;
                    }
                }
                //Pemisalan variabel
                for(int j=Ncol-2;j>=Nbar;j--){
                    solusi[j]=Character.toString(param);
                    koefparam[j][j-Nvar]=1;
                    param--;
                }

                //Perhitungan solusi
                for(int i=Nbar-1;i>=0;i--){
                    String temp="";
                    koefparam[i][0] = M[i][Ncol-1];
                    //Penyimpanan koefisen variabel yang dimisalkan
                    for(int j=Ncol-(1+Nvar);j<Ncol-1;j++){
                        for(int l=0;l<=Nvar;l++){
                            koefparam[i][l] -= M[i][j]*koefparam[j][l];
                        } 
                    }
                    //Buat persamaan parametrik
                    boolean zero=false;
                    for(int l=0;l<=Nvar;l++){
                        double value=koefparam[i][l];
                        String op;
                        //Cek mines
                        if (value<0) {op="-"; value = Math.abs(value);}
                        else op="+";

                        if(value==0) {//Koefisien 0
                            if(l==0) zero = true;
                        }
                        else if(value==1){//Koefisien 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + solusi[l+Nvar]; zero=false;}
                            else temp = temp + op + solusi[l+Nvar];
                        }
                        else{//Koefisien > 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + String.format("%.2f",value)+solusi[l+Nvar]; zero=false;}
                            else temp = temp + op + String.format("%.2f",value)+solusi[l+Nvar];
                        }
                    } 
                    solusi[i]=temp;
                }
            }
        }
        output(variable,solusi,nKol,isSolve);
    }

    public boolean checkBar(double[][] M,int i,int Ncol){
        boolean nol = true;

        for(int j=0;j<Ncol&&nol;j++){
            if (M[i][j] != 0) nol=false;
        }

        return nol;
    }
}
