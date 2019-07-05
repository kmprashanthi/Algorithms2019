import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

public class johnsons {

    static int b = 2;
    public static final int INFTY = Integer.MAX_VALUE ;

    static class node{
        int x; // cost
        int y; // vertex

        node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static class Pair{
        int key;  // destination vertex
        int value; // weight of the edge between source and destination

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

    public static int[] dijkstraAlgo( ArrayList<ArrayList<Pair>> G, int V, int E, int source){

        ArrayList<node> A = new ArrayList<node> (V); // arraylist for heap
        ArrayList<Integer> pos = new ArrayList<Integer> (V); // arraylist for keeping position of vertices
        ArrayList<Boolean> flag = new ArrayList<Boolean> (V); // arraylist to check if the vertex is already present in the tree
        int [] cost = new int [V];
        int currCost = 0;
        int destVertex = 0;
        int edgeWeight = 0;


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

        for (int i=0; i<V;i++){
            cost[i]= INFTY;
        }

        // actual algorithm

        decreaseKey(A,pos.get(source),0); // setting source cost to be 0

        for (int j=0; j<A.size(); j++){
            pos.set((A.get(j).y),j); //  pos[A[j].y]=j;
        }

        // while there exists a edge thats connected to the current source vertex
        while( A.size()>0 && A.get(0).x!=INFTY){

            pos.set((A.get(0).y),INFTY) ;// pos[A[0].y] = INFTY;  // vertex no more exists in the heap

            // remove min node from 0
            node removed = removeMin(A);
            cost[removed.y] = removed.x;
            // cost.add(removed.y, removed.x); // set cost from source to current vertex to be the min cost while removing node
            flag.set(removed.y,true);

            int sourceVertex = removed.y;
            int sourceCost = removed.x;

            // fix position in pos according to A
            for (int j=0; j<A.size(); j++){
                pos.set((A.get(j).y),j); //pos[A[j].y]=j;
            }

            // for each edge of removed node(u) change the cost and decrease key
            for (int k=0; k<G.get(sourceVertex).size(); k++){


                destVertex = G.get(sourceVertex).get(k).getKey();
                edgeWeight = G.get(sourceVertex).get(k).getValue();

                //relax edge u->j

                currCost = sourceCost + edgeWeight;
                if ( ((pos.get(destVertex)!= INFTY)) && A.get(pos.get(destVertex)).x > currCost ){
                    decreaseKey(A,pos.get(destVertex),currCost);

                    for (int j=0; j<A.size(); j++){
                        pos.set((A.get(j).y),j);

                    }
                }
            }

        }

        return cost;
    }

    public static int[] bellmanFordAlgo(ArrayList<ArrayList<Pair>> G, int V, int E){

        int [] cost = new int [V+1]; // extra edge
        int source = 0 ;
        int dest = 0 ;
        int weight = 0 ;
        int currCost = 0;

        for (int i=0; i<V+1;i++){
            cost[i]= INFTY;
        }

        // set extra edge distance to 0 - which is at the last position in the array
        cost[V]=0;

        //run for V+1 iterations - including the extra edge
        for (int i=0;i<V+1; i++) {
            for (int j = 0; j < G.size(); j++) {
                for (int k = 0; k < G.get(j).size(); k++) {

                    source = j;
                    dest = G.get(j).get(k).getKey();
                    weight = G.get(j).get(k).getValue();
                    if (cost[source] == INFTY) {
                        continue;
                    } else {
                        currCost = cost[source] + weight; // if j is not infinte then calculate currCost
                    }

                    if (cost[dest] > currCost) {

                        cost[dest] = currCost;
                    }
                }
            }
        }

        // checking for negative weight cycle


        for (int j = 0; j < G.size(); j++) {
            for (int k = 0; k < G.get(j).size(); k++) {

                source = j;
                dest = G.get(j).get(k).getKey();
                weight = G.get(j).get(k).getValue();

                if (cost[source] == INFTY) {
                    continue;
                } else {
                    currCost = cost[source] + weight; // if j is not infinte then calculate currCost
                }

                if (cost[dest] > currCost) {
                    System.out.println("Negative edge weight cycle");
                    System.exit(1);
                }
            }
        }

        return cost;

    }

    public  static int[][] johnsonAlgo(ArrayList<ArrayList<Pair>> G, int V, int E){

        int[][] shortestPath = new int[V][V];
        int [] oneSourceCost = new int [V];
        int [] h = new int[V+1];
        // initializing the shortestPath Matrix
        for (int i=0;i<V;i++){
            for (int j=0;j<V;j++){
                shortestPath[i][j] = INFTY;
            }
        }

        h = bellmanFordAlgo(G,V,E);

        G.remove(G.size()-1);
        G.trimToSize();

        //reweight all edges
        for (int i=0;i<V;i++){
            for (int j=0;j<G.get(i).size();j++){

                G.get(i).get(j).value = G.get(i).get(j).getValue() + h[i]-h[G.get(i).get(j).getKey()];

            }
        }


        // dijkstra part
        for (int i=0 ; i<V; i++){
            oneSourceCost = dijkstraAlgo(G,V,E,i);
            shortestPath[i] = oneSourceCost;
        }

        // re-weighting again to get the actual weights between
        for (int i=0; i<V; i++){
            for (int j=0; j<V ; j++){
                if (shortestPath[i][j] != INFTY){
                    shortestPath [i][j] -= (h[i]-h[j]);
                }
            }
        }


        return shortestPath;
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
        int[][] shortestPath = new int[V][V];

        for (int i=0;i<V+1;i++){
            ArrayList<Pair> e1 = new ArrayList<Pair> ();
            G.add(e1);
        }

        // get each edge and weights
        for (int i = 0; i<E; i++){

            line = s.nextLine();

            String[] edgetmp = line.split(" ");
            int x1 = Integer.parseInt(edgetmp[0]);
            int x2 = Integer.parseInt(edgetmp[1]);
            int wt = Integer.parseInt(edgetmp[2]);

            // only add x2,wt to x1 as list as its a directed weighted graph
            Pair p1 = new Pair(x2,wt);

            G.get(x1).add(p1);

        }

        // adding the extra vertex for reweighting
        for (int i=0;i<V;i++){
            Pair p1 = new Pair(i,0);
            G.get(V).add(p1); // let the extra vertex be at last position (V) and add all other vertex to it with 0  weight
        }

        shortestPath = johnsonAlgo(G,V,E);

        int queries = Integer.parseInt (s.nextLine()); // number of queries

        // answer each query
        for (int i=0;i<queries;i++){

            line = s.nextLine();

            tmp = line.split( " " );
            int S = Integer.parseInt( tmp[0] ); // source vertex
            int D = Integer.parseInt( tmp[1] ); // destination vertex

            // no path exists
            if ( shortestPath[S][D] == INFTY ){
                System.out.println(S+" -> "+D+" = x");
            }
            // path exists
            else{
                System.out.println(S+" -> "+D+" = "+shortestPath[S][D]);
            }
        }

    }
}
