package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object JarPrinter : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach { c ->
            c.accept(TraceClassVisitor(PrintWriter(System.out)))
        }
        if (source != destination) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}