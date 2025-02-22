package sequence.util.struct

abstract class Request : Comparable<Request> {
    abstract val priority: Int

    override operator fun compareTo(other: Request) = priority.compareTo(other.priority)
}

class RequestQueue
