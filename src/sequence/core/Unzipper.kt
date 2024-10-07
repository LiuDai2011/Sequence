package sequence.core

import arc.files.Fi
import arc.struct.Seq
import arc.struct.StringMap
import sequence.SeqMod
import sequence.core.SqLog.debug
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.StandardCharsets

object Unzipper {
    val version by lazy { VersionController() }

    fun find(name: String, predicate: (Fi) -> Boolean = { f: Fi -> f.name() == name }): Fi? =
        findAll(name, predicate)?.first()

    fun find(predicate: (Fi) -> Boolean = { false }): Fi? =
        findAll(predicate)?.first()

    fun findAll(name: String, predicate: (Fi) -> Boolean = { f: Fi -> f.name() == name }): Seq<Fi>? {
        return try {
            SeqMod.MOD.root.findAll(predicate)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun findAll(predicate: (Fi) -> Boolean = { false }): Seq<Fi>? {
        return try {
            SeqMod.MOD.root.findAll(predicate)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun unzip(file: Fi?, version: String): Fi {
        requireNotNull(file) { "File is null." }
        var fi = SqVars.unzipDirectory.child(
            file.path()
                .replace("/", "-")
                .replace(".", "-") + "/"
        )
        if (!fi.exists()) fi.mkdirs()
        fi = fi.child("c" + file.path().hashCode() % 1048576 + "/")
        if (!fi.exists()) fi.mkdirs()
        if (fi.child("version").exists()) {
            if (fi.child("version").readString() == version) {
                debug("Version valid: @ (version @)", fi.absolutePath(), version)
                return fi.child(file.name())
            } else {
                for (fi1 in fi.list()) {
                    fi1.delete()
                }
                debug("Replace @ to version: @", fi.absolutePath(), version)
            }
        }
        val a = fi.child(file.name())
        try {
            a.write().use { write -> write.write(file.readBytes()) }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val b = fi.child("version")
        try {
            b.write().use { write -> write.write(version.toByteArray(StandardCharsets.UTF_8)) }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return a
    }

    class VersionController {
        val versions = StringMap()

        init {
            check(!init) { "Cannot invoke 'public VersionController()' again." }
            init = true
            version = find("version.properties") ?: throw FileNotFoundException("version.properties")
            val strings = version.readString().split("\\n".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (string in strings) {
                val str = string.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val key = str[0]
                val value = str[1]
                versions.put(key, value)
                debug("@;@", key, value)
            }
        }

        operator fun get(key: String): String {
            if (versions.containsKey(key)) return versions[key]
            throw RuntimeException("Don't have the key.")
        }

        companion object {
            lateinit var version: Fi
            private var init = false
        }
    }
}
