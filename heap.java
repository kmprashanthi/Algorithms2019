import java.util.Scanner;
import java.util.ArrayList;

public class heap {

    static int b = 2;
    static int key_comp = 0;

    static class node{
        int x;
        int y;

        node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static int log2(int n){

        for (int i = 0; i <= n; i++) {
            if (Math.pow(2, i) == n) return i;
        }
        return -1;
    }

    public static void percolateUp(ArrayList<node> A, int i){
        //if A[ i ] is lesser than its parent
        //Swap A[ i ] and A[ parent( i ) ]
        //Percolate-Up( A, parent( i ) )

        int x1;
        int y1;
        int parent = (i-1) >> log2(b);
        key_comp +=1;
        if ( ( (A.get(i).x) < (A.get(parent).x) ) ){
            x1 = A.get(i).x;
            y1 = A.get(i).y;
            A.get(i).x = A.get(parent).x;
            A.get(i).y = A.get(parent).y;
            A.get(parent).x = x1;
            A.get(parent).y = y1;
        }
        if (parent > 0 ) {
            percolateUp(A, parent);
        }
    }

    public static void insertValue (ArrayList<node> A,node newNode){
        // Increment the heap size
        // put v in the new, last element
        // percolateUp(A, index_of_last element)

        A.add(newNode);
        int i = A.size()-1;

        if (i > 0) {
            percolateUp(A, i);
        }
    }

    public static void minHeapify(ArrayList<node> A,int i){
        //let smaller be the index of the child with the smaller key
        //if A[ i ] > A[ smaller ]
        //Swap A[ i ] with A[ smaller ]
        //minHeapify( A, smaller )

        int x1;
        int y1;
        int max_child;
        if (i==0){
            max_child = b;
        }
        else {
            max_child = (i+1)<< log2(b);
        }
        int min_child = max_child - (b-1);
        if (min_child < A.size()) {
            int smaller = min_child;
            for (int j = min_child+1; ((j <= max_child) && (j <= A.size() - 1)); j++) {
                key_comp += 1;
                if ((A.get(j).x) < (A.get(smaller).x)) {
                    smaller = j;
                }
            }

            key_comp += 1;
            if ((A.get(i).x) > (A.get(smaller).x)) {
                x1 = A.get(i).x;
                y1 = A.get(i).y;

                A.get(i).x = A.get(smaller).x;
                A.get(i).y = A.get(smaller).y;

                A.get(smaller).x = x1;
                A.get(smaller).y = y1;
            }
            minHeapify(A, smaller);
        }
    }

    public static node removeMin(ArrayList<node> A){
        //v = A[0] , since we are starting the array from 0
        //Copy the last heap element to A[ 0 ]
        //Decrease the heap size
        //minHeapify( A, 0 )

        node min = A.get(0);
        node last = A.get(A.size()-1);
        A.set(0,last);
        A.remove(A.size()-1);
        A.trimToSize();
        minHeapify(A,0);

        return min;
    }



    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        if (args.length != 0){

            try {
                b = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException nfe) {
                System.out.println(" No branching factor specified, setting it to default ");
            }
        }


        ArrayList<node> A = new ArrayList<node>();

        Scanner s = new Scanner(System.in);

        String line = " ";
        while (s.hasNext()){
            line = s.nextLine();
            if  (line.equals("-1")){
                node min = removeMin(A);
                System.out.print(min.x);
                System.out.print(" ");
                System.out.println(min.y);
            }
            else{
                String[] tmp = line.split( " " );
                int x = Integer.parseInt( tmp[0] );
                int y = Integer.parseInt( tmp[1] );
                if (tmp.length > 2 ){
                    System.out.println(" Invalid input format");
                }

                node newNode = new node(x,y);
                insertValue(A,newNode);
            }
        }

        System.out.print("key comparisons: ");
        System.out.println(key_comp);
        long endTime   = System.currentTimeMillis();
        long totalTime = (endTime - startTime);
        //System.out.println ("The total time taken");
        //System.out.println (totalTime);
    }
}
