package org.runestar.client.plugins.dev

import io.reactivex.Observable
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.raw.access.XIterableNodeDeque
import org.runestar.client.raw.access.XReflectionCheck
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import java.lang.reflect.Modifier

class ReflectionChecksDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(creations.subscribe(::onCreated))
    }

    private val creations: Observable<XReflectionCheck> get() = XIterableNodeDeque.addFirst.exit
            .filter { it.instance == CLIENT.reflectionChecks }
            .map { it.arguments[0] as XReflectionCheck }

    private fun onCreated(rc: XReflectionCheck) {
        val msg = StringBuilder()
        msg.append(rc.id)
        msg.append(':')
        for (i in 0 until rc.size) {
            msg.append('\n')
            msg.append(messageForOperation(rc, i))
            if (rc.creationErrors[i] != 0) {
                msg.append("\n\t")
                msg.append("Error while creating: ")
                msg.append(creationErrorType(rc.creationErrors[i]).name)
            }
        }
        logger.info(msg.toString())
    }

    private fun creationErrorType(n: Int): Class<out Throwable> {
        return when (n) {
            -1 -> ClassNotFoundException::class.java
            -2 -> SecurityException::class.java
            -3 -> NullPointerException::class.java
            -4 -> Exception::class.java
            -5 -> Throwable::class.java
            else -> throw IllegalStateException()
        }
    }

    private fun messageForOperation(rc: XReflectionCheck, index: Int): String {
        return when (rc.operations[index]) {
            0 -> "getInt <${rc.fields[index]}> = ${getInt(rc, index)}"
            1 -> "setInt <${rc.fields[index]}>"
            2 -> "getModifiers <${rc.fields[index]}>"
            3 -> "invoke <${rc.methods[index]}>"
            4 -> "getModifiers <${rc.methods[index]}>"
            else -> "Nothing"
        }
    }

    private fun getInt(rc: XReflectionCheck, index: Int): String {
        return try {
            val f = rc.fields[index]
            if (!Modifier.isPrivate(f.modifiers)) {
                f.isAccessible = true
            }
            f.getInt(null).toString()
        } catch (e: Exception) {
            e.javaClass.name
        }
    }
}