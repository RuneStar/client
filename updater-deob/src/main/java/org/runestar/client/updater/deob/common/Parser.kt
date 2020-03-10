package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.parse
import java.nio.file.Path

class Parser(val readOptions: Int, val writeOptions: Int) : Transformer {

    override fun transform(dir: Path, klasses: List<ByteArray>) = klasses.map { parse(it, readOptions, writeOptions) }
}