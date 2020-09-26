public class GaussJordan {
    public double[][] OBEGaussJ(final int nBrs, final int nKol, final double[][] M){
        final int FirstIdx = 0, LastIdxBar = nBrs-1, LastIdxKol = nKol-1, y;

        double temp, kons;
        boolean swap;

        // Proses OBE
        if (nKol>nBrs) y = nBrs;
        else y = nKol;

        for (int l = FirstIdx; l<y; l++) {
            int x=l;
            swap = false;

            // Cek diagonal 0
            if (M[l][x] == 0) {
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

                if (swap) x=l;

                //Cek kolom jika tidak ada baris yang dapat diswap
                for(int i=0;i<nKol-1&&!swap&&l+i<nKol;i++){
                    if (M[l][l+i]!=0) {x=l+i; swap=true;};
                }
            }

            if (M[l][x]!=0){
                //Membuat elemen diagonal menjadi 1 dan perkalian barisnya
                kons = 1/M[l][x];
                for(int j=FirstIdx; j<=LastIdxKol; j++){
                    M[l][j] *= kons;
                    if (Math.abs(M[l][j])==0) M[l][j] = Math.abs(M[l][j]);
                }
                

                int i=(l+1)%nBrs; 
                while(i!=l){
                    //Cari konstanta pembuat 0
                    kons = (M[i][x])/(M[l][x]);
                    M[i][x]=0;
                    
                    for(int j=x+1; j<=LastIdxKol; j++){
                        M[i][j] -= kons*(M[l][j]);
                        if (Math.abs(M[i][j])==0) M[i][j] = Math.abs(M[i][j]);
                    }
                    i= (i+1) % nBrs;
                }
            }
        }
        return M;
    }
}
