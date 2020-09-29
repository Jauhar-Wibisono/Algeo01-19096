import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SPL {
    public static int nBrs,nKol; 
    public static double[][] Maug = new double[101][101];
    public static String[] variable = new String[101];
    
    public void input(){
        double[][] A = new double[101][100];
        double[][] B = new double[101][1];
        Scanner in = new Scanner(System.in);
        int opt;
        System.out.println("1. Masukkan Matriks dari keyboard");
        System.out.println("2. Masukkan Matriks dari file");
        System.out.print("Input pilihan: ");
        opt = in.nextInt();

        while(opt<1 || opt>2){
            System.out.println("Masukan tidak valid, silahkan input ulang");
            opt=in.nextInt();
        }

        if (opt==1){
            System.out.print("Input Jumlah Persamaan: ");
            nBrs = in.nextInt();
            System.out.print("Input Jumlah Variabel: ");
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
                    if (j!=nKol-1) Maug[i][j]= A[i][j];
                    else Maug[i][j] = B[i][0];
                }
            }
        }else{
            String namaFile;
            boolean error;
            do{
                error=false;
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
                    error = true;
                    System.out.println("Terjadi error dalam proses pembacaan file.\nSilahkan input ulang nama file.");
                    err.printStackTrace();
                }
            } while (error);
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
        for (int i=0; i<Nbar; i++){
            if (checkBar(M, i, Ncol-1) && M[i][Ncol-1]!=0){
                isSolve=false;
            }
        }
        if (isSolve) {//Ada solusi
            //Elminasi baris yang elemennya 0 semua
            for(int i=Nbar-1;i>=0;i--){
                if(checkBar(M, i, Ncol)) Nbar--;
            }
            
            //Periksa jumlah solusi yang ada
            if(Nbar<(Ncol-1)) solusiUnik=false;
            else solusiUnik=true;

            if (solusiUnik){//Solusi SPL unik
                //Subtitusi mundur
                for(int i=Nbar-1;i>=0;i--){
                    double temp = M[i][Ncol-1];
                    for(int j=i+1;j<Ncol-1;j++){
                        double value = Double.parseDouble(solusi[j]);
                        temp -= (M[i][j])*value;
                    }
                    solusi[i] = String.format("%.2f",temp);
                }
            }else{//Solusi SPL lebih dari 1
                int Nvar = Ncol-(1+Nbar);

                //Deklarasi matriks koefparam[i][j]
                //i=idx variabel X, j=koef. variabel parametrik
                double[][] koefparam = new double[Ncol+1][Nvar+1];
                for(int i=0;i<=Ncol;i++){
                    for(int j=0;j<=Nvar;j++){
                        if (i==Ncol) koefparam[i][j]=Ncol-1;
                        else koefparam[i][j]=0;
                    }
                }

                //Pemisalan variabel
                int count=0;//count jumlah variabel yang dijadikan param (<=Nvar)
                int x=0;//idx kolom jika ada pergeseran diagonal

                //Periksa diagonal awal
                char param = 'p';
                for(int i=0;i<Nbar;i++){
                    if (M[i][x]==0){
                        solusi[x]=Character.toString(param);
                        boolean nol = true;
                        count++;
                        koefparam[x][count]=1;
                        koefparam[Ncol][count]=x;
                        param++; x++;
                        //Periksa kolom sebelahnya 0 atau tidak
                        for(int l=x;l<Ncol-1&&nol;l++){
                            if (M[i][l]==0){//Jika 0 juga di misalkan
                                solusi[l]=Character.toString(param);
                                count++;
                                koefparam[l][count]=1;
                                koefparam[Ncol][count]=l;
                                param++; x++;
                            }else nol = false;
                        }
                    }
                    x++;
                }
                //Periksa variabel yang tidak termasuk diagonal awal
                int k = Nbar-1;
                for(int j=Ncol-(1+Nvar);j<Ncol-1&&count!=Nvar;j++){
                    if (M[k][j]!=0&&!checkBar(M, k, j)){
                        solusi[j]=Character.toString(param);
                        count++;
                        koefparam[j][count]=1;
                        koefparam[Ncol][count]=j;
                        param++;                                        
                    }else if(M[k][j]==0&&checkCol(M, j, Nbar)){
                        solusi[j]=Character.toString(param);
                        count++;
                        koefparam[j][count]=1;
                        koefparam[Ncol][count]=j;
                        param++; 
                    }
                }

                //Perhitungan solusi
                for(int i=Nbar-1;i>=0;i--){
                    //Cari diagonal 1
                    boolean found=false;
                    int y=0;
                    for(int j=0;j<Ncol-1&&!found;j++){
                        //Cari kolom pertama yang elemennya bukan 0
                        if (M[i][j]!=0){
                            found = true;
                            y = j;
                        }
                    }
                    koefparam[y][0] = M[i][Ncol-1];
                    //Penyimpanan koefisen variabel yang dimisalkan
                    for (int m=y+1;m<Ncol-1;m++){
                        for (int l=0;l<=Nvar;l++){
                            koefparam[y][l] -= M[i][m]*koefparam[m][l];
                        }
                    }

                    //Buat persamaan parametrik
                    String temp="";
                    boolean zero=false;
                    for(int l=0;l<=Nvar&&found;l++){
                        double value=koefparam[y][l];
                        String op;
                        int varIdx = (int) koefparam[Ncol][l];
                        //Cek mines
                        if (value<0&&l!=0) {op="-"; value = Math.abs(value);}
                        else op="+";

                        if(value==0) {//Koefisien 0
                            if(l==0) zero = true;
                        }
                        else if(value==1){//Koefisien 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + solusi[varIdx]; zero=false;}
                            else temp = temp + op + solusi[varIdx];
                        }
                        else{//Koefisien > 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + String.format("%.2f",value)+solusi[varIdx]; zero=false;}
                            else temp = temp + op + String.format("%.2f",value)+solusi[varIdx];
                        }
                    }
                    if (found) solusi[y]=temp;
                
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
        //Periksa ada solusi atau tidak
        for (int i=0; i<Nbar; i++){
            if (checkBar(M, i, Ncol-1) && M[i][Ncol-1]!=0){
                isSolve=false;
            }
        }
        if(isSolve){
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
                int Nvar = Ncol-(1+Nbar);//Jumlah variabel parametrik

                //Deklarasi matriks koefparam[i][j]
                //i=idx variabel X, j=koef. variabel parametrik
                double[][] koefparam = new double[Ncol+1][Nvar+1];
                for(int i=0;i<=Ncol;i++){
                    for(int j=0;j<=Nvar;j++){
                        if (i==Ncol) koefparam[i][j]=Ncol-1;
                        else koefparam[i][j]=0;
                    }
                }

                //Pemisalan variabel
                int count=0;//count jumlah variabel yang dijadikan param (<=Nvar)
                int x=0;//idx kolom jika ada pergeseran diagonal

                //Periksa diagonal awal
                char param = 'p';
                for(int i=0;i<Nbar;i++){
                    if (M[i][x]==0){
                        solusi[x]=Character.toString(param);
                        boolean nol = true;
                        count++;
                        koefparam[x][count]=1;
                        koefparam[Ncol][count]=x;
                        param++; x++;
                        //Periksa kolom sebelahnya 0 atau tidak
                        for(int l=x;l<Ncol-1&&nol;l++){
                            if (M[i][l]==0){//Jika 0 juga di misalkan
                                solusi[l]=Character.toString(param);
                                count++;
                                koefparam[l][count]=1;
                                koefparam[Ncol][count]=l;
                                param++; x++;
                            }else nol = false;
                        }
                    }
                    x++;
                }
                //Periksa variabel yang tidak termasuk diagonal awal
                int k = Nbar-1;
                for(int j=Ncol-(1+Nvar);j<Ncol-1&&count!=Nvar;j++){
                    if (M[k][j]!=0&&!checkBar(M, k, j)){
                        solusi[j]=Character.toString(param);
                        count++;
                        koefparam[j][count]=1;
                        koefparam[Ncol][count]=j;
                        param++;                                        
                    }else if(M[k][j]==0&&(checkCol(M, j, Nbar))){
                        solusi[j]=Character.toString(param);
                        count++;
                        koefparam[j][count]=1;
                        koefparam[Ncol][count]=j;
                        param++; 
                    }
                }

                //Perhitungan solusi
                for(int i=Nbar-1;i>=0;i--){
                    //Pencarian elemen bukan 0
                    int y=0;
                    boolean found=false;
                    for(int j=0;j<Ncol-1&&!found;j++){
                        if (M[i][j]!=0){
                            found=true;
                            y=j;
                        }
                    }
                    koefparam[y][0] = M[i][Ncol-1];

                    //Penyimpanan koefisen variabel yang dimisalkan di array koefparam
                    for(int j=y+1;j<Ncol-1;j++){
                        for(int l=0;l<=Nvar;l++){
                            koefparam[y][l] -= M[i][j]*koefparam[j][l];
                        } 
                    }

                    System.out.println("Variabel ke"+(y+1));
                    for(int j=0;j<Ncol-1;j++){
                        for(int l=0;l<=Nvar;l++){
                            System.out.print(koefparam[j][l]+" ");
                        } 
                        System.out.println();
                    }

                    //Buat persamaan parametrik
                    String temp="";
                    boolean zero=false;
                    for(int l=0;l<=Nvar;l++){
                        double value=koefparam[y][l];
                        String op;
                        int idxVar = (int) koefparam[Ncol][l];

                        //Cek mines
                        if (value<0&&l!=0) {op="-"; value = Math.abs(value);}
                        else op="+";

                        if(value==0) {//Koefisien 0
                            if(l==0) zero = true;
                        }
                        else if(value==1){//Koefisien 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + solusi[idxVar]; zero=false;}
                            else temp = temp + op + solusi[idxVar];
                        }
                        else{//Koefisien > 1
                            if (l==0) temp = temp + String.format("%.2f",value);
                            else if(zero) {temp = temp + String.format("%.2f",value)+solusi[idxVar]; zero=false;}
                            else temp = temp + op + String.format("%.2f",value)+solusi[idxVar];
                        }
                    } 
                    if (found) solusi[y]=temp;
                }
            }
        }
        output(variable,solusi,Ncol,isSolve);
    }

    public boolean checkBar(double[][] M,int i,int Ncol){
        boolean nol = true;

        for(int j=0;j<Ncol&&nol;j++){
            if (M[i][j] != 0) nol=false;
        }

        return nol;
    }

    public boolean checkCol(double[][] M,int j,int Nbar){
        boolean nol = true;

        for(int i=0;i<Nbar&&nol;i++){
            if (M[i][j] != 0) nol=false;
        }

        return nol;
    }
}
