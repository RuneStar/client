package org.runestar.client.common

import java.awt.Image
import java.awt.PopupMenu
import java.awt.Window

/**
 * Proxy for `java.awt.Taskbar`. Does nothing on Java 8.
 */
object AwtTaskbar {

    // sun.awt.windows.WTaskbarPeer - Windows

    private val taskbarClass = catchToNull { Class.forName("java.awt.Taskbar") }

    private val taskBarStateClass = catchToNull {
        @Suppress("UNCHECKED_CAST")
        Class.forName("java.awt.Taskbar\$State") as Class<Enum<*>>
    }

    private val getTaskbar = taskbarClass?.let {
        catchToNull { it.getMethod("getTaskbar") }
    }

    private val taskbar = catchToNull { getTaskbar?.invoke(null) }

    private val setWindowProgressValue = taskbarClass?.let {
        catchToNull { it.getMethod("setWindowProgressValue", Window::class.java, Integer.TYPE) }
    }

    private val setWindowIconBadge = taskbarClass?.let {
        catchToNull { it.getMethod("setWindowIconBadge", Window::class.java, Image::class.java) }
    }

    private val requestWindowUserAttention = taskbarClass?.let {
        catchToNull { it.getMethod("requestWindowUserAttention", Window::class.java) }
    }

    private val setWindowProgressState = taskbarClass?.let {
        catchToNull { it.getMethod("setWindowProgressState", Window::class.java, taskBarStateClass) }
    }

    private val setIconImage = taskbarClass?.let {
        catchToNull { it.getMethod("setIconImage", Image::class.java) }
    }

    private val setMenu = taskbarClass?.let {
        catchToNull { it.getMethod("setMenu", PopupMenu::class.java) }
    }

    fun setWindowProgressValue(window: Window, value: Int) {
        catchToNull { setWindowProgressValue?.invoke(taskbar, window, value) }
    }

    fun setWindowIconBadge(window: Window, badge: Image?) {
        catchToNull { setWindowIconBadge?.invoke(taskbar, window, badge) }
    }

    fun requestWindowUserAttention(window: Window) {
        catchToNull { requestWindowUserAttention?.invoke(taskbar, window) }
    }

    fun setWindowProgressState(window: Window, state: State) {
        val s = taskBarStateClass?.enumConstants?.firstOrNull { it.name == state.name }
        catchToNull { setWindowProgressState?.invoke(taskbar, window, s) }
    }

    fun setIconImage(image: Image) {
        catchToNull { setIconImage?.invoke(taskbar, image) }
    }

    fun setMenu(menu: PopupMenu) {
        catchToNull { setMenu?.invoke(taskbar, menu) }
    }

    enum class State {
        /**
         * Stops displaying the progress.
         */
        OFF,
        /**
         * The progress indicator displays with normal color and determinate
         * mode.
         */
        NORMAL,
        /**
         * Shows progress as paused, progress can be resumed by the user.
         * Switches to the determinate display.
         */
        PAUSED,
        /**
         * The progress indicator displays activity without specifying what
         * proportion of the progress is complete.
         */
        INDETERMINATE,
        /**
         * Shows that an error has occurred. Switches to the determinate
         * display.
         */
        ERROR
    }

    private inline fun <T> catchToNull(f: () -> T): T? {
        return try {
            f()
        } catch (e: Exception) {
            null
        }
    }
}