package sequence.world.meta.imagine

import arc.struct.PQueue

abstract class Request : Comparable<Request> {
    abstract val priority: Int

    override operator fun compareTo(other: Request) = priority.compareTo(other.priority)
}

class RequestQueue : PQueue<Request>()
