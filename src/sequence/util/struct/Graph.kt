package sequence.util.struct

import arc.struct.ObjectMap
import arc.struct.OrderedMap
import arc.struct.Seq

class Graph<T : Any>(clazz: Class<T>) : Cloneable, Iterable<Graph.Edge<T>> {
    val nodes: Seq<T> = Seq(clazz)
    val edges: OrderedMap<T, Seq<Edge<T>>> = OrderedMap()

    fun link(u: T, v: T) {
        edges[u, { Seq(Edge::class.java) }].add(Edge(u, v))
    }

    fun linkUnique(u: T, v: T) {
        edges[u, { Seq(Edge::class.java) }].addUnique(Edge(u, v))
    }

    class Edge<T>(var u: T, var v: T) {
        override fun toString() = "Edge(${u} -> ${v})"
        operator fun component1() = u
        operator fun component2() = v
        override fun equals(other: Any?): Boolean {
            if (other !is Edge<*>) return false
            return u == other.u && v == other.v
        }

        override fun hashCode(): Int {
            var result = u?.hashCode() ?: 0
            result = 31 * result + (v?.hashCode() ?: 0)
            return result
        }
    }

    class GraphIterator<T : Any>(graph: Graph<T>) : Iterator<Edge<T>> {
        private val iter: ObjectMap.Entries<T, Seq<Edge<T>>>
        private var iter2: Iterator<Edge<T>>?

        init {
            iter = graph.edges.iterator()
            iter2 = if (graph.edges.isEmpty) {
                null
            } else iter.next().value.iterator()
        }

        override fun hasNext(): Boolean {
            while (iter2?.hasNext() != true) {
                if (!iter.hasNext()) return false
                iter2 = iter.next().value.iterator()
            }
            return true
        }

        override fun next(): Edge<T> {
            while (iter2?.hasNext() != true) {
                if (!iter.hasNext()) throw NoSuchElementException()
                iter2 = iter.next().value.iterator()
            }
            return iter2!!.next()
        }
    }

    override operator fun iterator(): GraphIterator<T> {
        return GraphIterator(this)
    }
}
