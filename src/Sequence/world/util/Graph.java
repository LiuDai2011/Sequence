package Sequence.world.util;

import arc.func.Prov;
import arc.struct.*;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class Graph<T> implements Cloneable {
    protected final Class<T> clazz;
    protected final IntMap<T> nodes = new IntMap<>();
    protected final ObjectIntMap<T> ids = new ObjectIntMap<>();
    protected final IntMap<IntSet> graph = new IntMap<>();
    protected int nodeCount = 0;

    public Graph(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Graph<T> copy() {
        try {
            return (Graph<T>) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <V> V checkKey(IntMap<V> map, int key, Prov<V> def) {
        if (!map.containsKey(key)) map.put(key, def.get());
        return map.get(key);
    }

    protected void checkNode(T node) {
        if (!nodes.containsValue(node, false)) {
            addNode(node);
        }
    }

    public T get(int key) {
        return nodes.get(key);
    }

    public int get(T key) {
        return ids.get(key);
    }

    public void addEdge(T from, T to, boolean d) {
        checkNode(from);
        checkNode(to);
        checkKey(graph, ids.get(from), IntSet::new).add(ids.get(to));
        if (d) checkKey(graph, ids.get(to), IntSet::new).add(ids.get(from));
    }

    public IntSeq all() {
        return ids.values().toArray();
    }

    public IntSet outs(T from) {
        if (!ids.containsKey(from))
            throw new IllegalArgumentException("The graph doesn't have a node valued " + from + ".");
        checkKey(graph, ids.get(from), IntSet::new);
        return graph.get(ids.get(from));
    }

    public T addNode(T node) {
        if (nodes.containsValue(node, false)) throw new RuntimeException("The node " + node + " exists.");
        nodes.put(nodeCount, node);
        ids.put(node, nodeCount);
        nodeCount++;
        return node;
    }

    public Graph<T> merge(Graph<T> other) {
        for (IntMap.Entry<T> node : other.nodes) {
            if (!ids.containsKey(node.value)) {
                addNode(node.value);
            }
        }
        for (IntMap.Entry<IntSet> entry : other.graph) {
            IntSet value = entry.value;
            int size = value.size;
            IntSet.IntSetIterator iterator = value.iterator();
            while (iterator.hasNext) {
                addEdge(other.get(entry.key), other.get(iterator.next()), false);
            }
        }
        return this;
    }

    public Seq<Graph<T>> delete(int nodeId) {
        graph.remove(nodeId);
        for (IntMap.Entry<IntSet> entry : graph) {
            IntSet.IntSetIterator iter = entry.value.iterator();
            while(iter.hasNext){
                int v = iter.next();
                if (v == nodeId) iter.remove();
            }
        }
        ids.remove(nodes.get(nodeId));
        nodes.remove(nodeId);
        return checkConnected();
    }

    private Seq<Graph<T>> checkConnected() {
        DisjointSetUnion dsu = new DisjointSetUnion(nodeCount);
        IntMap<Graph<T>> res = new IntMap<>();
        for (IntMap.Entry<IntSet> entry : graph) {
            IntSet.IntSetIterator iter = entry.value.iterator();
            while(iter.hasNext){
                int v = iter.next();
                dsu.unite(entry.key, v);
            }
        }
        for (IntMap.Entry<T> entry : nodes) {
            Graph<T> tg = res.get(dsu.find(entry.key), () -> new Graph<>(clazz));
            IntSet.IntSetIterator iter = graph.get(entry.key, IntSet::new).iterator();
            while(iter.hasNext){
                int v = iter.next();
                tg.addEdge(get(entry.key), get(v), false);
            }
        }
        return res.values().toArray();
    }

    @Override
    public String toString() {
        return "Graph{" +
                "clazz=" + clazz +
                ", nodes=" + nodes +
                ", graph=" + graph +
                ", nodeCount=" + nodeCount +
                '}';
    }

    public void read(Reads read) {}
    public void write(Writes write) {}
}
