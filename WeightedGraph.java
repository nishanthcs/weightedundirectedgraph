import java.util.*;

class Vertex {
    public String val;
    public boolean isVisited;

    public Vertex(String val){
        this.val = val;
        this.isVisited = false;
    }

    public void visit(){
        this.isVisited = true;
        System.out.println("Visiting "+val);
    }
    public void visit(boolean noprint){
        this.isVisited = true;
    }
}

class Edge implements Comparable {
    public int start;
    public int end;
    public int weight;

    public Edge(int s, int e, int w) {
        start = s;
        end = e;
        weight = w;
    }

    /* 
    * Comparison is necessary since Edges are loaded into Priority Queue
    */
    public int compareTo(Object o){
        Edge e = (Edge) o;
        if(this.weight < e.weight) {
            return -1;
        }else if(this.weight > e.weight) {
            return 1;
        }else {
            return 0;
        }
    }
}

/**
 * Weighted Graph based that uses a Dependency Matrix to 
 * hold edges.
 *
 * 
 */
public class WeightedGraph{
    public Vertex[] vertexList;
    public int[][] adjMatrix;
    int size;
    int i;

    public WeightedGraph(int n) {
        this.size = n;
        this.adjMatrix = new int[n][n];
        this.vertexList = new Vertex[n];
    }

    public void addVertex(String s) {
        vertexList[i] = new  Vertex(s);
        i++;
    }

    public void addEdge(int s, int d, int w){
        this.adjMatrix[s][d] = w;
        this.adjMatrix[d][s] = w;
    }

    public void printEdges(){
        for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++) {
                System.out.print("\t"+adjMatrix[i][j]);
            }
            System.out.print("\n");
        }
    }

    /**
     * Calculates Minimum spanning tree of Undirected Graph.
     * Overall TC O(N) ; Space Complexity O(N)
    */ 
    private void mst(){
        /* Priority Queue to hold the edges */
        Queue<Edge> edgePriorityQueue= new PriorityQueue<>();
        HashSet<Integer> mst = new HashSet<>();
        int currentVertex = 0;
        // Start with vertex A ie. 0
        mst.add(currentVertex);
        // Just a counter to see how many loops it takes
        int j = 1;
        // O(N)
        while(true){
            System.out.println("Round "+j++);
            // O(N)
            fillPriorityQueue(currentVertex, mst, edgePriorityQueue);

            // O(1)
            Edge pathEdge = edgePriorityQueue.poll();
            // Additional pruning to priority queue to delete all the edges that
            // lead to a vertex already part of the tree.
            // Pruning could take additional O(N)
            while(pathEdge !=null && mst.contains(pathEdge.end)){
                System.out.println("Pruning queue. Removed "+ getEdgeString(pathEdge));
                pathEdge = edgePriorityQueue.poll();
            }
            if(pathEdge == null) {
                // Exit condition
                break;
            }

            // Add in MST
            mst.add(pathEdge.end);
            // Found the edge with the lowest weight, add the end of the edge
            // to MST set
            System.out.println(vertexList[pathEdge.start].val+
                    vertexList[pathEdge.end].val+pathEdge.weight);

            // Start from the end vertex of that newly added Edge. 
            currentVertex = pathEdge.end;
            System.out.println("---------------------------------------------------------");
        }



    }

    /**
     * Returns all the adjacent edges to Vertex s in  O(N)
     * 
     * Before an edge is added, it is filtered based on vertices covered so far.
     * @param s Source Vertex
     * @param mst HashSet containing the vertices covered so far in MST
     * @param edgePriorityQueue A priority queue containing all the edges that have 
     * are visible/covered. 
     */
    private void fillPriorityQueue(int s, HashSet<Integer> mst, Queue<Edge> edgePriorityQueue) {
        for(int i=0;i<size;i++) {
            // There is a weighted edge between s and i
            if(adjMatrix[s][i] > 0
                    // It is not a self edge
                    && s != i
                    // Minimum Spanning Tree does not contain the destination vertex
                    && !mst.contains(i)) {
                // Add EDGE to priority queue.
                edgePriorityQueue.add(new Edge(s,i,adjMatrix[s][i]));
            }
        }
    }

    /**
     * To print.  
     * 
     */
    private String getEdgeString(Edge pathEdge) {
        return vertexList[pathEdge.start].val+vertexList[pathEdge.end].val+pathEdge.weight;
    }

    public static void main(String []args){

        WeightedGraph wg = new WeightedGraph(6);
        wg.addVertex("A");
        wg.addVertex("B");
        wg.addVertex("C");
        wg.addVertex("D");
        wg.addVertex("E");
        wg.addVertex("F");

        wg.addEdge(0,1,6);
        wg.addEdge(0,3,4);
        wg.addEdge(1,3,7);
        wg.addEdge(2,3,8);

        wg.addEdge(1,2,10);
        wg.addEdge(1,4,7);
        wg.addEdge(2,4,5);
        wg.addEdge(3,4,12);

        wg.addEdge(2,5,6);
        wg.addEdge(4,5,7);

        //wg.printEdges();
        wg.mst();


    }
}