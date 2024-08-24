package Sequence.world.util.struct;

import arc.func.Prov;
import arc.struct.IntMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class Graph<T> implements Cloneable {
    protected final Class<T> clazz;
    protected final IntTable<T> nodes = new IntTable<>();
    protected final IntMap<IntSet> graph = new IntMap<>();

    public Graph(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected static <V> V checkKey(IntMap<V> map, int key, Prov<V> def) {
        if (!map.containsKey(key)) map.put(key, def.get());
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public Graph<T> copy() {
        try {
            return (Graph<T>) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int nodeSize() {
        return nodes.size();
    }

    protected void checkNode(T node) {
        if (!nodes.containsValue(node)) {
            addNode(node);
        }
    }

    public T get(int key) {
        return nodes.getValue(key);
    }

    public int get(T key) {
        return nodes.getKey(key);
    }

    public void addEdge(T from, T to, boolean d) {
        checkNode(from);
        checkNode(to);
        checkKey(graph, nodes.getKey(from), IntSet::new).add(nodes.getKey(to));
        if (d) checkKey(graph, nodes.getKey(to), IntSet::new).add(nodes.getKey(from));
    }

    public Seq<Integer> all() {
        return nodes.map21.values().toSeq();
    }

    public IntSet outs(T from) {
        if (!nodes.containsValue(from))
            throw new IllegalArgumentException("The graph doesn't have a node valued " + from + ".");
        checkKey(graph, nodes.getKey(from), IntSet::new);
        return graph.get(nodes.getKey(from));
    }

    public T addNode(T node) {
        if (nodes.containsValue(node)) throw new RuntimeException("The node " + node + " exists.");
        nodes.put(nodes.size(), node);
        return node;
    }

    public Graph<T> merge(Graph<T> other) {
        for (ObjectTable.Entry<Integer, T> node : other.nodes) {
            if (!nodes.containsKey(node.a1)) {
                addNode(node.a2);
            }
        }
        for (IntMap.Entry<IntSet> entry : other.graph) {
            IntSet value = entry.value;
            IntSet.IntSetIterator iterator = value.iterator();
            while (iterator.hasNext) {
                addEdge(other.get(entry.key), other.get(iterator.next()), false);
            }
        }
        return this;
    }

    public Graph<T> delete(int nodeId) {
        graph.remove(nodeId);
        for (IntMap.Entry<IntSet> entry : graph) {
            IntSet.IntSetIterator iter = entry.value.iterator();
            while (iter.hasNext) {
                int v = iter.next();
                if (v == nodeId) iter.remove();
            }
        }
        nodes.removeByKey(nodeId);
        return this;
    }

    public void clear() {
        graph.clear();
        nodes.clear();
    }

    @SuppressWarnings("unused")
    private Seq<Graph<T>> checkConnected() {
        if (true) throw new RuntimeException("Unsupported method!");
        return null;

//        DisjointSetUnion dsu = new DisjointSetUnion(nodeCount);
//        IntMap<Graph<T>> res = new IntMap<>();
//        for (IntMap.Entry<IntSet> entry : graph) {
//            IntSet.IntSetIterator iter = entry.value.iterator();
//            while(iter.hasNext){
//                int v = iter.next();
//                dsu.unite(entry.key, v);
//            }
//        }
//        for (IntMap.Entry<T> entry : nodes) {
//            Graph<T> tg = res.get(dsu.find(entry.key), () -> new Graph<>(clazz));
//            IntSet.IntSetIterator iter = graph.get(entry.key, IntSet::new).iterator();
//            while(iter.hasNext){
//                int v = iter.next();
//                tg.addEdge(get(entry.key), get(v), false);
//            }
//        }
//        return res.values().toArray();
    }

    @Override
    public String toString() {
        return "Graph{" +
                "clazz=" + clazz +
                ", nodes=" + nodes +
                ", graph=" + graph +
                '}';
    }

    public void read(Reads read) {
    }

    public void write(Writes write) {
    }
}
