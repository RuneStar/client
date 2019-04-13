@file:Suppress("DEPRECATION")

package org.runestar.client.common

import java.applet.AppletContext
import java.applet.AppletStub
import java.awt.Desktop
import java.awt.Dimension
import java.io.IOException
import java.io.InputStream
import java.net.URL

data class JavConfig(
        val properties: Map<String, String>,
        val parameters: Map<Int, String>
) : AppletStub, AppletContext {

    operator fun get(key: String): String = properties.getValue(key)

    /**
     * Example: `http://oldschool7.runescape.com/gamepack_1583061.jar`
     */
    val gamepackUrl get() = URL(get(CODEBASE) + get(INITIAL_JAR))

    val appletMinSize get() = Dimension(
            get(APPLET_MINWIDTH).toInt(),
            get(APPLET_MINHEIGHT).toInt()
    )

    val appletMaxSize get() = Dimension(
            get(APPLET_MAXWIDTH).toInt(),
            get(APPLET_MAXHEIGHT).toInt()
    )

    val windowPreferredSize get() = Dimension(
            get(WINDOW_PREFERREDWIDTH).toInt(),
            get(WINDOW_PREFERREDHEIGHT).toInt()
    )

    /**
     * Example: `"client"`
     */
    val initialClass get() = get(INITIAL_CLASS).removeSuffix(".class")

    companion object {

        private const val CODEBASE = "codebase"
        private const val INITIAL_JAR = "initial_jar"
        private const val INITIAL_CLASS = "initial_class"
        private const val WINDOW_PREFERREDWIDTH = "window_preferredwidth"
        private const val WINDOW_PREFERREDHEIGHT = "window_preferredheight"
        private const val APPLET_MAXWIDTH = "applet_maxwidth"
        private const val APPLET_MAXHEIGHT = "applet_maxheight"
        private const val APPLET_MINWIDTH = "applet_minwidth"
        private const val APPLET_MINHEIGHT = "applet_minheight"

        /**
         * @param[world] world component of the url: oldschool{world}.runescape.com, examples: `"2"`, `"6b"`, `""` for
         *               the default world
         */
        @Throws(IOException::class)
        fun load(world: String = ""): JavConfig {
            val properties = HashMap<String, String>()
            val parameters = HashMap<Int, String>()
            val url = URL("http://oldschool$world.runescape.com/jav_config.ws")
            url.openStream().bufferedReader(Charsets.ISO_8859_1).forEachLine { line ->
                val split1 = line.split('=', limit = 2)
                when(split1[0]) {
                    "param" -> {
                        val split2 = split1[1].split('=', limit = 2)
                        parameters[split2[0].toInt()] = split2[1]
                    }
                    "msg" -> {}
                    else -> properties[split1[0]] = split1[1]
                }
            }
            return JavConfig(properties, parameters)
        }
    }

    override fun getDocumentBase(): URL = codeBase

    override fun appletResize(width: Int, height: Int) {}

    override fun getParameter(name: String): String? = parameters[name.toInt()]

    override fun getCodeBase() = URL(get(CODEBASE))

    override fun getAppletContext(): AppletContext = this

    override fun isActive() = true

    override fun getApplet(name: String?) = throw UnsupportedOperationException()

    override fun getApplets() = throw UnsupportedOperationException()

    override fun getAudioClip(url: URL) = throw UnsupportedOperationException()

    override fun getImage(url: URL) = throw UnsupportedOperationException()

    override fun getStream(key: String) = throw UnsupportedOperationException()

    override fun getStreamKeys() = throw UnsupportedOperationException()

    override fun setStream(key: String, stream: InputStream?) = throw UnsupportedOperationException()

    override fun showDocument(url: URL) { Desktop.getDesktop().browse(url.toURI()) }

    override fun showDocument(url: URL, target: String) = showDocument(url)

    override fun showStatus(status: String) = throw UnsupportedOperationException()
}