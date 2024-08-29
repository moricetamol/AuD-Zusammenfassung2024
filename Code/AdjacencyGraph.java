import java.util.HashMap;
import java.util.Map;

class AdjacencyGraph<N>{
    private final AdjacencyRepresentation representation; // adjlist or matrix
    private final Map<N, Integer> nodeToIndex = new HashMap<>();
    private final Map<Integer, N> indexToNode = new HashMap<>();

    public AdjacencyGraph(AdjacencyRepresentation representation) {
        this.representation = representation;
    }


}
