package org.runestar.client

import io.sigpipe.jbsdiff.Patch
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.JavConfig
import org.runestar.client.common.REVISION
import org.runestar.client.game.raw.ClientProvider
import org.runestar.client.game.raw.access.XClient
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

class PatchedClientProvider : ClientProvider {

    override fun get(): XClient {
        val jc = JAV_CONFIG
        return patchGamePack(jc).loadClass(jc.initialClass).getDeclaredConstructor().newInstance() as XClient
    }

    private fun patchGamePack(javConfig: JavConfig): ClassLoader {
        val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
        val patchedGamepackPath = tmpdir.resolve("${codeSourceLastModifiedMillis()}.zip")
        if (!verifyJar(patchedGamepackPath)) {
            val gamepackPath = tmpdir.resolve("runescape-gamepack.$REVISION.jar")
            if (!verifyJar(gamepackPath)) {
                downloadFile(javConfig.gamepackUrl, gamepackPath)
            }
            patch(gamepackPath, patchedGamepackPath)
        }
        return URLClassLoader(arrayOf(patchedGamepackPath.toUri().toURL()))
    }

    private fun patch(gamepack: Path, patchedGamepack: Path) {
        val patchBytes = javaClass.classLoader.getResource("gamepack.diff.gz").readBytes()
        Files.newOutputStream(patchedGamepack).use { output ->
            Patch.patch(Files.readAllBytes(gamepack), patchBytes, output)
        }
    }

    private fun verifyJar(jar: Path): Boolean {
        return try {
            JarFile(jar.toFile(), true).close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun downloadFile(url: URL, destination: Path) {
        url.openStream().use { input ->
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    private fun codeSourceLastModifiedMillis(): Long {
        val codeSource = javaClass.protectionDomain.codeSource ?: return System.currentTimeMillis()
        val file = Paths.get(codeSource.location.toURI())
        return Files.getLastModifiedTime(file).toMillis()
    }
}