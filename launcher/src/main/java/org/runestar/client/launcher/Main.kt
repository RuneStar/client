@file:JvmName("Main")

package org.runestar.client.launcher

import org.eclipse.aether.resolution.ArtifactResult
import org.runestar.client.common.*
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.UIManager

private val klass = MethodHandles.lookup().lookupClass()

private val logger = LoggerFactory.getLogger(klass)

fun main(args: Array<String>) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        logger.warn("Failed to set system look and feel", e)
    }

    val frame = LaunchFrame()
    val aether = Aether(
            Paths.get(System.getProperty("user.home"), ".m2", "repository"),
            "repo.runestar.org"
    )
    aether.session.transferListener = frame

    try {
        Files.createDirectories(PLUGINS_DIR_PATH)
        updateArtifact(aether.updateArtifact(GROUP_ID, PLUGINS_STANDARD_ARTIFACT_ID), PLUGINS_STANDARD_PATH)
        updateArtifact(aether.updateArtifact(GROUP_ID, CLIENT_ARTIFACT_ID), CLIENT_PATH)
    } catch (e: Exception) {
        logger.warn("Error while updating", e)
    } finally {
        frame.dispose()
    }

    val cmd = ProcessBuilder("java", "-jar", CLIENT_PATH.toString())
    if (System.console() != null || isRunFromIde()) {
        cmd.inheritIO().start().waitFor()
    } else {
        cmd.start()
    }
}

private fun updateArtifact(artifactResult: ArtifactResult, file: Path) {
    logger.info("Found artifact $artifactResult")
    Files.createDirectories(file.parent)
    Files.copy(artifactResult.artifact.file.toPath(), file, StandardCopyOption.REPLACE_EXISTING)
}

private fun isRunFromIde(): Boolean {
     return klass.getResource(klass.simpleName + ".class").protocol == "file"
}