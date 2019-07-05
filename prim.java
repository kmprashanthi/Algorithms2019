import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

public class prim {

    static int b = 2;
    public static final int INFTY = Integer.MAX_VALUE ;

    static class node{
        int x; // cost in Prim's
        int y; // vertex in Prim's

        node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static class Pair{
        int key;
        int value;

        Pair(int key, int value){
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public int getValue() {
            return value;
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
                if ((A.get(j).x) < (A.get(smaller).x)) {
                    smaller = j;
                }
            }

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

    public static void decreaseKey(ArrayList<node> A, int i, int k){
        A.get(i).x = k  ;//A.set(i,k);
        if (i>0) {
            percolateUp(A, i);
        }
    }

    public static void setBranchfactor(int E, int V){
        double Ed = (double) E;
        double x = Ed/V;
        if (x <= 2.0){
            b = 2;
        }
        else{
            double log2Value =  (Math.log(x) / Math.log(2));
            int floorValue =  (int) Math.floor(log2Value);
            b = (int) Math.pow(2,floorValue) *2;
        }
    }


    public static void primsAlgo( ArrayList<ArrayList<Pair>> G, int V, int E){


        ArrayList<node> A = new ArrayList<node> (V); // arraylist for heap
        ArrayList<Integer> pos = new ArrayList<Integer> (V); // arraylist for keeping position of vertices
        ArrayList<Boolean> flag = new ArrayList<Boolean> (V); // arraylist to check if the vertex is already present in the tree
        int trees = 0;
        int totalCost=0;
        int currCost =0;


        // initialize the min heap
        for (int i=0; i<V; i++){
            node newNode = new node(INFTY, i);
            A.add(newNode);  // initially adding all the vertex with infinity as cost. Percolate up is not needed
        }

        // initialize position for pos array (initially the heap contains in the same order as vertex)
        for (int i=0; i<V;i++){
            pos.add(i,i);
        }

        // initialize flag array
        for (int i=0;i<V;i++){
            flag.add(i,false);
        }

        // actual algorithm
        for (int i=0;i<V; i++){


            if (flag.get(i) == true ){
                continue;
            }

            else{
                trees+=1;
            }

            decreaseKey(A,pos.get(i),0); // new tree in the forest

            for (int j=0; j<A.size(); j++){
                pos.set((A.get(j).y),j); //  pos[A[j].y]=j;
            }


            // while there exists a edge thats connected to the current tree
            while( A.size()>0 && A.get(0).x!=INFTY){

                totalCost += A.get(0).x; // increase cost of the total MST
                pos.set((A.get(0).y),INFTY) ;// pos[A[0].y] = INFTY;  // vertex no more exists in the heap

                // remove min node from 0
                node removed = removeMin(A);
                flag.set(removed.y,true);

                // fix position in pos according to A
                for (int j=0; j<A.size(); j++){
                    pos.set((A.get(j).y),j); //pos[A[j].y]=j;
                }


                // for each edge of removed node(u) change the cost and decrease key
                for (int k=0; k<G.get(removed.y).size(); k++){

                    int destVertex = G.get(removed.y).get(k).getKey();
                    currCost=G.get(removed.y).get(k).getValue();

                    // if the vertex exists in heap queue && if the cost is lesser than the one in heap
                    if ((pos.get(destVertex)!= INFTY) && (currCost < A.get(pos.get(destVertex)).x)){
                        decreaseKey (A,pos.get(destVertex),currCost);
                        for (int j=0; j<A.size(); j++){
                            pos.set((A.get(j).y),j);
                        }
                    }
                }

            }

        }

        System.out.println(b+" "+trees+" "+totalCost);

    }


    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        String line = " ";
        line = s.nextLine();

        // first line contains the vertex and edges
        String[] tmp = line.split( " " );
        int V = Integer.parseInt( tmp[0] );
        int E = Integer.parseInt( tmp[1] );

        // arraylist for graph notation with V size
        ArrayList<ArrayList<Pair>> G =  new ArrayList<ArrayList<Pair>> (V);

        for (int i=0;i<V;i++){
            ArrayList<Pair> e1 = new ArrayList<Pair> ();
            G.add(e1);
        }

        // set branching factor
        setBranchfactor(E,V);

        // get each edge and weights
        for (int i = 0; i<E; i++){

            line = s.nextLine();

            String[] edgetmp = line.split(" ");
            int x1 = Integer.parseInt(edgetmp[0]);
            int x2 = Integer.parseInt(edgetmp[1]);
            int wt = Integer.parseInt(edgetmp[2]);

            // add x2,wt to x1 as list
            Pair p1 = new Pair(x2,wt);
            G.get(x1).add(p1);

            // add x1,wt to x2 as list
            Pair p2 = new Pair (x1,wt);
            G.get(x2).add(p2);

        }
        primsAlgo(G,V,E);
    }
}
