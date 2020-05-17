package org.runestar.client.plugins.chatlog

import org.runestar.client.api.forms.DateTimeFormatterForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.Message
import org.runestar.client.api.game.live.Chat
import org.runestar.client.api.game.unescapeAngleBrackets
import org.runestar.client.api.game.unescapeSpaces
import org.runestar.client.api.plugins.PluginSettings
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant

class ChatLog : DisposablePlugin<ChatLog.Settings>() {

    override val name = "Chat Log"

    override val defaultSettings = Settings()

    override fun onStart() {
        val writer = Files.newBufferedWriter(
                ctx.directory.resolve(settings.saveFileName),
                StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE
        )

        Chat.messageAdditions
                .subscribe {
                    val line = toLine(it)
                    writer.write(line)
                    if (settings.print) print(line)
                }
                .add()

        add { writer.close() }
    }

    private fun toLine(msg: Message): String {
        return settings.formatString.format(
                settings.dateTimeFormatter.value.format(Instant.now()),
                msg.type,
                msg.prefix.orEmpty(),
                unescapeSpaces(msg.sender),
                unescapeAngleBrackets(msg.text)
        )
    }

    class Settings(
            val dateTimeFormatter: DateTimeFormatterForm = DateTimeFormatterForm("yyyy-MM-dd HH:mm:ss", null),
            val saveFileName: String = "chat.txt",
            val formatString: String = "%s\t%s\t[%s]\t<%s>\t%s%n",
            val print: Boolean = false
    ) : PluginSettings()
}