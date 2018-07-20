package org.runestar.client.common

import java.awt.Desktop
import java.awt.Dimension
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.security.InvalidParameterException

data class JavConfig(
        val default: Map<String, String>,
        val parameters: Map<String, String>,
        val messages: Map<String, String>
) {

    operator fun get(key: Key.Default): String = default.getValue(key.keyString)

    operator fun get(key: Key.Message): String = messages.getValue(key.keyString)

    operator fun get(key: Key.Parameter): String = parameters.getValue(key.keyString)

    /**
     * Example: `http://oldschool7.runescape.com/`
     *
     * @see[Key.Default.CODEBASE]
     */
    val codebase get() = URL(get(Key.Default.CODEBASE))

    /**
     * Example: `http://oldschool7.runescape.com/gamepack_1583061.jar`
     */
    val gamepackUrl get() = URL(get(Key.Default.CODEBASE) + get(Key.Default.INITIAL_JAR))

    val appletMinSize get() = Dimension(
            get(Key.Default.APPLET_MINWIDTH).toInt(),
            get(Key.Default.APPLET_MINHEIGHT).toInt()
    )

    val appletMaxSize get() = Dimension(
            get(Key.Default.APPLET_MAXWIDTH).toInt(),
            get(Key.Default.APPLET_MAXHEIGHT).toInt()
    )

    val windowPreferredSize get() = Dimension(
            get(Key.Default.WINDOW_PREFERREDWIDTH).toInt(),
            get(Key.Default.WINDOW_PREFERREDHEIGHT).toInt()
    )

    /**
     * Example: `"client"`
     */
    val initialClass get() = get(Key.Default.INITIAL_CLASS).removeSuffix(".class")

    companion object {

        /**
         * @param[world] world component of the url: oldschool{world}.runescape.com, examples: `"2"`, `"6b"`, `""` for
         *               the default world
         */
        fun getUrl(world: String = ""): URL {
            return URL("http://oldschool$world.runescape.com/jav_config.ws")
        }

        /**
         * Request from <http://oldschool.runescape.com/jav_config.ws>.
         *
         * @param[world] world component of the url: oldschool{world}.runescape.com, examples: `"2"`, `"6b"`, `""` for
         *               the default world
         */
        @Throws(IOException::class)
        fun load(world: String = ""): JavConfig {
            val default = LinkedHashMap<String, String>()
            val parameters = LinkedHashMap<String, String>()
            val messages = LinkedHashMap<String, String>()
            getUrl(world).openStream().bufferedReader(Charsets.ISO_8859_1).useLines { lines ->
                lines.forEach { line ->
                    val split1 = line.split('=', limit = 2)
                    when(split1[0]) {
                        Key.Parameter.BASE -> {
                            val split2 = split1[1].split('=', limit = 2)
                            parameters[split2[0]] = split2[1]
                        }
                        Key.Message.BASE -> {
                            val split2 = split1[1].split('=', limit = 2)
                            messages[split2[0]] = split2[1]
                        }
                        else -> default[split1[0]] = split1[1]
                    }
                }
            }
            return JavConfig(default, parameters, messages)
        }
    }

    interface Key {

        val keyString: String

        enum class Parameter : Key {
            _1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18;

            override val keyString get() = name.removePrefix("_")

            companion object {
                const val BASE = "param"
            }
        }

        enum class Message : Key {
            LANG0,
            TANDC,
            LANGUAGE,
            ERR_CREATE_ADVERTISING,
            ERR_CREATE_TARGET,
            ERR_VERIFY_BC,
            ERR_SAVE_FILE,
            ERR_LOAD_BC,
            ERR_DOWNLOADING,
            OK,
            LOADING_APP,
            COPY_PASTE_URL,
            ERR_VERIFY_BC64,
            CANCEL,
            NEW_VERSION_LINK,
            NEW_VERSION,
            MESSAGE,
            ERR_GET_FILE,
            INFORMATION,
            OPTIONS,
            NEW_VERSION_LINKTEXT,
            LOADING_APP_RESOURCES,
            CHANGES_ON_RESTART;

            override val keyString get() = name.toLowerCase()

            companion object {
                const val BASE = "msg"
            }
        }

        enum class Default : Key {
            WINDOW_PREFERREDWIDTH,
            OTHER_SUB_VERSION,
            DOWNLOAD,
            TERMSURL,
            BROWSERCONTROL_WIN_AMD64_JAR,
            PRIVACYURL,
            BROWSERCONTROL_WIN_X86_JAR,
            INITIAL_JAR,
            ADVERTURL,
            APPLET_MAXHEIGHT,
            WIN_SUB_VERSION,
            TITLE,
            INITIAL_CLASS,
            APPLET_MAXWIDTH,
            VIEWERVERSION,
            WINDOW_PREFERREDHEIGHT,
            APPLET_MINWIDTH,
            CACHEDIR,
            ADVERT_HEIGHT,
            STOREBASE,
            CODEBASE,
            MAC_SUB_VERSION,
            APPLET_MINHEIGHT;

            override val keyString get() = name.toLowerCase()
        }
    }

    @Suppress("DEPRECATION")
    class AppletStub(val javConfig: JavConfig) : java.applet.AppletStub, java.applet.AppletContext {

        override fun getDocumentBase(): URL = codeBase

        override fun appletResize(width: Int, height: Int) {}

        override fun getParameter(name: String): String? = javConfig.parameters[name]

        override fun getCodeBase(): URL = try {
            javConfig.codebase
        } catch (e: MalformedURLException) {
            throw InvalidParameterException()
        }

        override fun getAppletContext() = this

        override fun isActive(): Boolean = true

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
}