package Sequence.world.util;

import Sequence.world.meta.IO;
import arc.func.Prov;
import arc.struct.IntMap;
import arc.struct.IntSeq;
import arc.struct.ObjectIntMap;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class Graph<T extends IO> implements IO {
    protected final IntMap<T> nodes = new IntMap<>();
    protected final ObjectIntMap<T> nodesId = new ObjectIntMap<>();
    protected final IntMap<IntSeq> graph = new IntMap<>();
    protected int nodeCount = 0;

    protected static <V> V checkKey(IntMap<V> map, int key, Prov<V> def) {
        if (!map.containsKey(key)) map.put(key, def.get());
        return map.get(key);
    }

    public T get(int key) {
        return nodes.get(key);
    }

    public void addEdge(T from, T to, boolean d) {
        checkNode(from);
        checkNode(to);
        checkKey(graph, nodesId.get(from), IntSeq::new).add(nodesId.get(to));
        if (d) checkKey(graph, nodesId.get(to), IntSeq::new).add(nodesId.get(from));
    }

    public IntSeq all() {
        return nodesId.values().toArray();
    }

    public IntSeq outs(T from) {
        if (!nodesId.containsKey(from))
            throw new IllegalArgumentException("The graph doesn't have a node valued " + from + ".");
        checkKey(graph, nodesId.get(from), IntSeq::new);
        return graph.get(nodesId.get(from));
    }

    public T addNode(T node) {
        if (nodes.containsValue(node, false)) throw new RuntimeException("The node " + node + " exists.");
        nodes.put(nodeCount++, node);
        nodesId.put(node, nodeCount++);
        return node;
    }

    protected void checkNode(T node) {
        if (!nodes.containsValue(node, false)) {
            addNode(node);
        }
    }

    @Override
    public void read(Reads read) {
        int size;

        nodeCount = read.i();
        size = read.i();
        for (int i = 0; i < size; i++) {
            int key = read.i();
//            T value = ;
            // TODO
        }
    }

    @Override
    public void write(Writes write) {
        write.i(nodeCount);
        write.i(nodes.size);
        for (IntMap.Entry<T> entry : nodes) {
            write.i(entry.key);
            entry.value.write(write);
        }
        write.i(nodesId.size);
        for (ObjectIntMap.Entry<T> entry : nodesId) {
            entry.key.write(write);
            write.i(entry.value);
        }
        write.i(graph.size);
        for (IntMap.Entry<IntSeq> entry : graph) {
            write.i(entry.key);
            write.i(entry.value.size);
            for (int i : entry.value.toArray()) {
                write.i(entry.value.get(i));
            }
        }
    }
}
