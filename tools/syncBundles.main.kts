enum class Language(val suffix: String) {
    English(""), Chinese("zh_CN");

    override fun toString(): String {
        return suffix
    }
}

val English = Language.English
val Chinese = Language.Chinese

data class Properties(val key: String, val attributes: Map<Language, String>, val children: List<Properties>) {
    fun toString(filter: (Language) -> Boolean): String {
        var ret = ""
        val isRoot = key == ""
        val isLeaf = children.isEmpty()
        if (isLeaf) stack += key
        else if (!isRoot) stack += "$key."
        for ((k, v) in attributes) {
            if (!filter(k)) continue
            ret += "$stack=$v\n"
        }
        for (c in children) {
            ret += "$c\n"
        }
        if (isLeaf) stack = stack.dropLast(key.length)
        else if (!isRoot) stack = stack.dropLast(key.length + 1)
        return ret
    }

    override fun toString(): String = toString { true }

    companion object {
        var stack: String = ""
    }
}

fun branch(key: String, vararg children: Properties): Properties =
    Properties(
        key,
        mapOf(),
        children.toList()
    )

fun leaf(key: String, vararg attribute: Pair<Language, String>): Properties =
    Properties(
        key,
        mapOf(*attribute),
        listOf()
    )

fun name(vararg attribute: Pair<Language, String>) = leaf("name", *attribute)
fun description(vararg attribute: Pair<Language, String>) = leaf("description", *attribute)
fun details(vararg attribute: Pair<Language, String>) = leaf("details", *attribute)

val root =
    branch(
        "",
        branch(
            "block",
            branch(
                "seq-havoc",
                name(
                    English to "Havoc",
                    Chinese to "gesp"
                ),
                description(
                    English to "Eng desp",
                    Chinese to "Chn desp"
                )
            )
        )
    )

println(root)
println(root.toString { it == Language.Chinese })
