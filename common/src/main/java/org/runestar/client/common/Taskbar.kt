@file:JvmName("Taskbar")

package org.runestar.client.common

import java.awt.Window

private val taskBarClass = catchToNull { Class.forName("java.awt.Taskbar") }

private val getTaskbar = taskBarClass?.let {
    catchToNull { taskBarClass.getMethod("getTaskbar") }
}

private val taskbar = catchToNull { getTaskbar?.invoke(null) }

private val setWindowProgressValue = taskBarClass?.let {
    catchToNull { taskBarClass.getMethod("setWindowProgressValue", Window::class.java, Integer.TYPE) }
}

fun setWindowProgressValue(window: Window, value: Int) {
    catchToNull { setWindowProgressValue?.invoke(taskbar, window, value) }
}

private inline fun <T> catchToNull(f: () -> T): T? {
    return try {
        f()
    } catch (e: Exception) {
        null
    }
}