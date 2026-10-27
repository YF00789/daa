import java.util.*;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        List<Integer> brokenList = new ArrayList<>();
        for (int i=0; i<w.count(); i++){
            if (dsu.union(w.a()[i], w.b()[i])){
                brokenList.add(i);
            }
        }
        int[] result = new int[brokenList.size()];
        for (int i=0; i<result.length; i++){
            result[i]=brokenList.get(i);
        }
        return result;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] indices = new Integer[w.count()];
        for (int i=0; i<indices.length; i++){
            indices[i]=i;
        }
        Arrays.sort(indices, Comparator.comparingInt(i -> w.cost()[i]));
        DisjointSet dsu = new DisjointSet(w.cells());
        List<Integer> brokenList = new ArrayList<>();
        for (int i : indices){
            if (dsu.union(w.a()[i], w.b()[i])){
                brokenList.add(i);
            }
        }
        int[] result = new int[brokenList.size()];
        for (int i=0; i<result.length; i++){
            result[i]=brokenList.get(i);
        }
        return result;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        List<int[]>[] adj = new ArrayList[n];
        for (int i=0; i<n; i++){
            adj[i] = new ArrayList<>();
        }
        for (int i=0; i<w.count(); i++){
            adj[w.a()[i]].add(new int[]{w.b()[i], w.cost()[i]});
            adj[w.b()[i]].add(new int[]{w.a()[i], w.cost()[i]});
        }
        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{0, 0});
        long totalCost=0;
        int edgesUsed=0;
        while (!pq.isEmpty() && edgesUsed<n){
            int[] current=pq.poll();
            int u=current[0];
            if (visited[u]) continue;
            visited[u]=true;
            totalCost+=current[1];
            edgesUsed++;
            for (int[] edge : adj[u]){
                if (!visited[edge[0]]){
                    pq.add(edge);
                }
            }
        }
        return totalCost;
    }
}