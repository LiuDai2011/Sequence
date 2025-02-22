package sequence.util

inline fun <reified T : Any> T.getField(name: String) =
    this::class.java.getField(name).run {
        isAccessible = true
        this
    }
