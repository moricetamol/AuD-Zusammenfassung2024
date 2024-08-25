import java.util.HashSet;
import java.util.Set;

class AdjacencyMatrix implements AdjacencyRepresentation {
    private boolean[][] matrix;
    AdjacencyMatrix(int size) {
        matrix = new boolean[size][size];
    }
    public void addEdge(int from, int to) {
        matrix[from][to] = true;
    }
    public void removeEdge(int from, int to) {
        matrix[from][to] = false;
    }
    public boolean hasEdge(int from, int to) {
        return matrix[from][to];
    }
    Set<Integer> getAdjacentIndices(int index) {
        //TODO: H1 a)
        Set<Integer> to = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            if (hasEdge(index, i)) {
                to.add(i);
            }
        }
        return to;
    }
}
