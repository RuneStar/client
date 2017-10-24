package com.runesuite.client.inject

import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

internal fun copyJarClearManifest(source: Path, destination: Path) {
    Files.deleteIfExists(destination)
    JarInputStream(FileInputStream(source.toFile())).use { input ->
        JarOutputStream(FileOutputStream(destination.toFile())).use { output ->
            var e = input.nextJarEntry
            while (e != null) {
                output.putNextEntry(e)
                input.copyTo(output)
                output.closeEntry()
                input.closeEntry()
                e = input.nextJarEntry
            }
        }
    }
}