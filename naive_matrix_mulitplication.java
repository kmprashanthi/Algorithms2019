import java.util.Scanner;

public class naive {
    public static void main(String[] args) {

        //inputs taken from console
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        long [] p = new long[n+1];
        for(int m=0; m<n+1; m++ ) {
            p[m] = s.nextInt();
        }

        long q = 0;

        for (int i=1; i <n ; i++){
            q = q + (p[0]*p[i]*p[i+1]);
        }

        System.out.println (q);
    }
}
