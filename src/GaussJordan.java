public class GaussJordan {
    public double[][] OBEGaussJ(final int nBrs, final int nKol, final double[][] M){
        final int FirstIdx = 0, LastIdxBar = nBrs-1, LastIdxKol = nKol-1, y;

        double temp, kons;
        boolean swap;

        // Proses OBE
        if (nKol > nBrs) y = LastIdxKol-nBrs;
        else y = LastIdxKol;

        for (int l = FirstIdx; l<=y; l++) {
            int x;
            swap = false;

            // Cek diagonal 0
            if (M[l][l] == 0) {
                x = l;
                while (!swap && x<=LastIdxBar) {// cari baris yang bisa diswap
                    if (M[x][l] != 0) {
                        swap = true;
                    }
                    if (!swap) x++;
                }

                for(int k=FirstIdx; k<=LastIdxKol&&swap; k++){//swap baris
                    temp=M[l][k]; 
                    M[l][k]=M[x][k];
                    M[x][k]= temp;
                }
            }

            //Membuat elemen diagonal menjadi 1 dan perkalian barisnya
            kons = 1/M[l][l];
            for(int j=FirstIdx; j<=LastIdxKol; j++){
                M[l][j] *= kons;
                if (Math.abs(M[l][j])==0) M[l][j] = Math.abs(M[l][j]);
            }
            

            int i=(l+1)%nBrs; 
            while(i!=l&&M[l][l]!=0){
                //Cari konstanta pembuat 0
                kons = (M[i][l])/(M[l][l]);
                M[i][l]=0;
                
                for(int j=l+1; j<=LastIdxKol; j++){
                    M[i][j] -= kons*(M[l][j]);
                    if (Math.abs(M[i][j])==0) M[i][j] = Math.abs(M[i][j]);
                }
                i= (i+1) % nBrs;
            }
        }
        return M;
    }
}
