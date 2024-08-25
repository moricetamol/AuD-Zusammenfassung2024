import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class AdjacencyList implements AdjacencyRepresentation {
    LinkedList<Integer>[] adj;
    AdjacencyList(int size) {
        adj = new LinkedList[size];
        for (int i = 0; i < size; i++)
            adj[i] = new LinkedList<>();
    }
    public void addEdge(int from, int to) {
        adj[from].add(to);
    }
    public void removeEdge(int from, int to) {
        adj[from].remove(to);
    }
    public boolean hasEdge(int from, int to) {
        return adj[from].contains(to);
    }
    Set<Integer> getAdjacentIndices(int index) {
        Set<Integer> to = new HashSet<>();
        LinkedList<Integer> adjLI = new LinkedList<>(adj[index]);
        while (!adjLI.isEmpty()) {
            to.add(adjLI.removeFirst());
        }
        return to;
    }
}
