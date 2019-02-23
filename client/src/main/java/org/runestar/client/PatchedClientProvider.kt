package org.runestar.client

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.warn
import org.runestar.client.api.ROOT_DIR_PATH
import org.runestar.client.common.DIFF_NAME
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.JavConfig
import org.runestar.client.common.REVISION
import org.runestar.client.common.xorAssign
import org.runestar.client.game.raw.ClientProvider
import org.runestar.client.game.raw.access.XClient
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class PatchedClientProvider : ClientProvider {

    override fun get(): XClient {
        return patchGamePack(JAV_CONFIG).loadClass(JAV_CONFIG.initialClass).getDeclaredConstructor().newInstance() as XClient
    }

    private fun patchGamePack(javConfig: JavConfig): ClassLoader {
        val tempDir = ROOT_DIR_PATH.resolve(".temp")
        tempDir.toFile().deleteRecursively()
        Files.createDirectories(tempDir)
        val patchedGamepackPath = tempDir.resolve(".patched-gamepack" + System.currentTimeMillis())
        val gamepacksDir = ROOT_DIR_PATH.resolve(".gamepacks")
        Files.createDirectories(gamepacksDir)
        val gamepackPath = gamepacksDir.resolve(REVISION.toString())
        if (!Files.exists(gamepackPath)) {
            downloadFile(javConfig.gamepackUrl, gamepackPath)
        }
        patch(gamepackPath, patchedGamepackPath)
        val cl = URLClassLoader(arrayOf(patchedGamepackPath.toUri().toURL()))
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                cl.close()
                Files.deleteIfExists(patchedGamepackPath)
            } catch (e: Exception) {
                getLogger().warn(e) { "Exception while cleaning up patched-gamepack" }
            }
        })
        return cl
    }

    private fun patch(gamepack: Path, patchedGamepack: Path) {
        val original = Files.readAllBytes(gamepack)
        val bytes = javaClass.classLoader.getResource(DIFF_NAME).readBytes()
        bytes.xorAssign(original)
        Files.write(patchedGamepack, bytes)
    }

    private fun downloadFile(url: URL, destination: Path) {
        try {
            url.openStream().use { Files.copy(it, destination, StandardCopyOption.REPLACE_EXISTING) }
        } catch (e: IOException) {
            try {
                Files.deleteIfExists(destination)
            } catch (e2: Exception) {
                e.addSuppressed(e2)
            }
            throw e
        }
    }
}