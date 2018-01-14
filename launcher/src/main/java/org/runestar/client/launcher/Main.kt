@file:JvmName("Main")

package org.runestar.client.launcher

import org.eclipse.aether.resolution.ArtifactResult
import org.runestar.client.common.*
import org.slf4j.LoggerFactory
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.zip.ZipFile
import javax.swing.UIManager

private val klass = MethodHandles.lookup().lookupClass()

private val logger = LoggerFactory.getLogger(klass)

fun main(args: Array<String>) {
    setup()
    update()
    launch(getArguments())
}

private fun setup() {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        logger.warn("Failed to set system look and feel", e)
    }
}

private fun update() {
    val frame = LaunchFrame()
    val aether = Aether(
            Paths.get(System.getProperty("user.home"), ".m2", "repository"),
            "repo.runestar.org"
    )
    aether.session.transferListener = frame

    try {
        Files.createDirectories(PLUGINS_DIR_PATH)
        installArtifact(aether.updateArtifact(GROUP_ID, PLUGINS_STANDARD_ARTIFACT_ID), PLUGINS_STANDARD_PATH)
        installArtifact(aether.updateArtifact(GROUP_ID, CLIENT_ARTIFACT_ID), CLIENT_PATH)
    } catch (e: Exception) {
        logger.warn("Error while updating", e)
    } finally {
        frame.dispose()
    }
}

private fun installArtifact(artifactResult: ArtifactResult, file: Path) {
    logger.info("Found artifact $artifactResult")
    Files.createDirectories(file.parent)
    Files.copy(artifactResult.artifact.file.toPath(), file, StandardCopyOption.REPLACE_EXISTING)
}

private fun getArguments(): List<String> {
    if (Files.notExists(CLIENT_PRM_PATH)) {
        ZipFile(CLIENT_PATH.toFile()).use { zf ->
            zf.getInputStream(zf.getEntry(CLIENT_PRM_PATH.fileName.toString())).use { input ->
                FileOutputStream(CLIENT_PRM_PATH.toFile()).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
    FileReader(CLIENT_PRM_PATH.toFile()).use { fr ->
        return fr.readLines()
                .filter { !it.startsWith('#') && it.isNotBlank() }
                .map { it.trim() }
    }
}

private fun launch(conf: List<String>) {
    val cmd = ArrayList<String>(conf.size + 3).apply {
        add("java")
        addAll(conf)
        add("-jar")
        add(CLIENT_PATH.toString())
    }
    logger.info("Using command \"${cmd.joinToString(" ")}\"")
    val processBuilder = ProcessBuilder(cmd)
    if (System.console() != null || isRunFromIde()) {
        System.gc()
        processBuilder.inheritIO().start().waitFor()
    } else {
        processBuilder.start()
    }
}

private fun isRunFromIde(): Boolean {
    return klass.getResource(klass.simpleName + ".class").protocol == "file"
}