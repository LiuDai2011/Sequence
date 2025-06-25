package sequence.world.meta

import arc.Events
import arc.struct.Seq
import arc.util.pooling.Pool
import arc.util.pooling.Pool.Poolable
import arc.util.pooling.Pools
import mindustry.game.EventType

object PlaceHolderDrawer {
    private val requestPool: Pool<Request> = Pools.get(Request::class.java) { Request() }
    val requests: Seq<Request> = Seq()

    init {
        Events.run(EventType.Trigger.drawOver) {
            for (request in requests) {
                request()
            }
            requestPool.freeAll(requests)
            requests.clear()
        }
        Events.on(EventType.ResetEvent::class.java) {
            requestPool.freeAll(requests)
            requests.clear()
        }
    }

    fun add(schematic: MultiBlockSchematic, x: Int, y: Int) = requests.add(requestPool.obtain().set(schematic, x, y))

    operator fun invoke(schematic: MultiBlockSchematic, x: Int, y: Int) = add(schematic, x, y)

    data class Request(var schematic: MultiBlockSchematic = MultiBlockSchematic.empty, var x: Int = 0, var y: Int = 0) :
        Poolable {
        operator fun invoke() {
            schematic.draw(x, y)
        }

        override fun reset() {
            schematic = MultiBlockSchematic.empty
            x = 0
            y = 0
        }

        fun set(schematic: MultiBlockSchematic, x: Int, y: Int): Request {
            this.x = x
            this.y = y
            this.schematic = schematic
            return this
        }
    }
}