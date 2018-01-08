package org.runestar.client.common

import java.awt.Image
import java.awt.Window

object AwtTaskbar {

    private val taskBarClass = catchToNull { Class.forName("java.awt.Taskbar") }

    private val getTaskbar = taskBarClass?.let {
        catchToNull { it.getMethod("getTaskbar") }
    }

    private val taskbar = catchToNull { getTaskbar?.invoke(null) }

    private val setWindowProgressValue = taskBarClass?.let {
        catchToNull { it.getMethod("setWindowProgressValue", Window::class.java, Integer.TYPE) }
    }

    private val setWindowIconBadge = taskBarClass?.let {
        catchToNull { it.getMethod("setWindowIconBadge", Window::class.java, Image::class.java) }
    }

    fun setWindowProgressValue(window: Window, value: Int) {
        catchToNull { setWindowProgressValue?.invoke(taskbar, window, value) }
    }

    fun setWindowIconBadge(window: Window, badge: Image?) {
        catchToNull { setWindowIconBadge?.invoke(taskbar, window, badge) }
    }

    private inline fun <T> catchToNull(f: () -> T): T? {
        return try {
            f()
        } catch (e: Exception) {
            null
        }
    }
}