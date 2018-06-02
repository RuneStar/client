package org.runestar.client.patch

import io.sigpipe.jbsdiff.Patch
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Path

fun patch(gamepack: Path, patchedGamepack: Path) {
    val classLoader = MethodHandles.lookup().lookupClass().classLoader
    val patchBytes = classLoader.getResource("gamepack.diff.gz").readBytes()
    Files.newOutputStream(patchedGamepack).use { output ->
        Patch.patch(Files.readAllBytes(gamepack), patchBytes, output)
    }
}