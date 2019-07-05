import java.util.Scanner;

public class dp {
    public static void main(String[] args) {

        //inputs taken from console
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        long[] p = new long[n+1];
        for(int m=0; m<n+1; m++ ) {
            p[m] = s.nextInt();
        }

        long q = matrixChainOrder(p);

        System.out.println (q);
    }

    public static long matrixChainOrder (long [] p){

        int n = p.length-1; //number of matrices is one less than the size of dimension input
        long[][] m = new long [n+1][n+1];
        long q;
        int j;
        for (int i=1;i<=n;i++){
            m[i][i]=0;
        }
        for (int l=2;l<=n;l++){
            for (int i=1;i<=n-l+1;i++){
                j= i+l-1;
                m[i][j]= 9223372036854775807L;
                for (int k =i;k<=j-1;k++){
                    q = m[i][k]+m[k+1][j]+ (p[i-1]*p[k]*p[j]);
                    if (q < m[i][j]){
                        m[i][j] = q;
                    }
                }
            }
        }
        return m[1][n];
    }
}
