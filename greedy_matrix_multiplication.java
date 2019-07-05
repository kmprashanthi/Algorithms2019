import java.util.Scanner;

public class greedy {
    public static void main(String[] args) {

        //inputs taken from console
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        long[] p = new long[n+1];
        for(int m=0; m<n+1; m++ ) {
            p[m] = s.nextInt();
        }

        long q = recursiveGreedy(0,n,p);

        System.out.println (q);
    }

    public static int findMin(int i, int j, long[] p){
        long minVal = 9223372036854775807L;
        int minPos = 0;
        for (int m=i+1;m<j;m++){
            if (p[m]< minVal){
                minVal=p[m];
                minPos = m;
            }
        }
        return  minPos;
    }
    public static long recursiveGreedy (int i, int j, long [] p){
        if (i+1==j){
            return 0;
        }
        else{
            int k = findMin(i,j,p);
            return recursiveGreedy(i,k,p)+recursiveGreedy(k,j,p)+(p[i]*p[k]*p[j]);
        }

    }
}
