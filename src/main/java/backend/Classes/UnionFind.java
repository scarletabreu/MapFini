package backend.Classes;

import java.util.HashMap;

public class UnionFind {
    private final HashMap<Integer, Integer> parent = new HashMap<>();

    // Encuentra el representante del conjunto al que pertenece un elemento
    public int find(int id) {
        if (!parent.containsKey(id)) {
            parent.put(id, id); // Si no existe, se inicializa el padre como sí mismo
        }
        if (parent.get(id) != id) {
            parent.put(id, find(parent.get(id))); // Compresión de caminos
        }
        return parent.get(id);
    }

    // Une dos conjuntos
    public void union(int id1, int id2) {
        int root1 = find(id1);
        int root2 = find(id2);
        if (root1 != root2) {
            parent.put(root1, root2); // Une root1 a root2
        }
    }

    // Verifica si dos elementos están en el mismo conjunto
    public boolean connected(int id1, int id2) {
        return find(id1) == find(id2);
    }
}
