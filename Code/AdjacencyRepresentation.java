interface AdjacencyRepresentation {
    void addEdge(int from, int to);
    void removeEdge(int from, int to);
    boolean hasEdge(int from, int to);
}
