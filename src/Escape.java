import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {
    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds){}
    public static Result escape(Input.Walls w, int[] broken){
        int n = w.cells();
        int cols = w.cols();
        int[][] adj=Graphs.open(w, broken);
        int deadEnds=0;
        for (int i=0; i<n; i++){
            if (adj[i].length==1) deadEnds++;
        }
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        int[] stack = new int[n];
        int top=0;
        stack[top]=0;
        visited[0]=true;
        int visitedCount=0;
        while (top>=0){
            int u=stack[top--];
            visitedCount++;
            if (u==n-1) break;
            int[] order = {u-cols, u+1, u+cols, u-1};
            for (int i=3; i >=0; i--){
                int v=order[i];
                boolean isOpen=false;
                for (int neighbor : adj[u]){
                    if (neighbor==v){
                        isOpen=true;
                        break;
                    }
                }
                if (isOpen && !visited[v]){
                    visited[v]=true;
                    parent[v]=u;
                    stack[++top]=v;
                }
            }
        }
        List<Integer> pathList = new ArrayList<>();
        int current=n-1;
        while (current!=-1){
            pathList.add(current);
            current=parent[current];
        }
        Collections.reverse(pathList);
        int[] path = new int[pathList.size()];
        for (int i=0; i<path.length; i++){
            path[i]=pathList.get(i);
        }
        return new Result(path, visitedCount, deadEnds);
    }
}
