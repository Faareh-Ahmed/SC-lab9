package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mutable weighted directed graph implementation with labeled vertices.
 */
public class Graph<L>{

    private final Map<L, Map<L, Integer>> adjacencyMap;

    // Constructor
    public Graph() {
        this.adjacencyMap = new HashMap<>();
    }

    public boolean add(L vertex) {
        if (adjacencyMap.containsKey(vertex)) {
            return false;
        }
        adjacencyMap.put(vertex, new HashMap<>());
        return true;
    }

    public int set(L source, L target, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }

        // Ensure both source and target vertices exist
        add(source);
        add(target);

        Map<L, Integer> targets = adjacencyMap.get(source);

        if (weight == 0) {
            // Remove edge if weight is zero
            return targets.remove(target) != null ? weight : 0;
        }

        // Add or update the edge
        return targets.put(target, weight) != null ? targets.get(target) : 0;
    }

    public boolean remove(L vertex) {
        if (!adjacencyMap.containsKey(vertex)) {
            return false;
        }

        // Remove all edges from the graph
        adjacencyMap.remove(vertex);

        // Remove all edges to the vertex
        for (Map<L, Integer> targets : adjacencyMap.values()) {
            targets.remove(vertex);
        }

        return true;
    }

    public Set<L> vertices() {
        return new HashSet<>(adjacencyMap.keySet());
    }

    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();

        for (Map.Entry<L, Map<L, Integer>> entry : adjacencyMap.entrySet()) {
            L source = entry.getKey();
            Map<L, Integer> targets = entry.getValue();

            if (targets.containsKey(target)) {
                sources.put(source, targets.get(target));
            }
        }

        return sources;
    }

    public Map<L, Integer> targets(L source) {
        return adjacencyMap.getOrDefault(source, new HashMap<>());
    }

    public void addEdge(L source, L target, int weight) {
        set(source, target, weight);
    }

    /**
     * Factory method to create an empty graph.
     */
    public static <L> Graph<L> empty() {
        return new Graph<>();
    }
}
