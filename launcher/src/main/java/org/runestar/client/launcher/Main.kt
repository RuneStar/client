@file:JvmName("Main")

package org.runestar.client.launcher

import org.eclipse.aether.resolution.ArtifactResult
import org.runestar.client.common.*
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.UIManager

fun main(args: Array<String>) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val frame = LaunchFrame()
    val aether = Aether(
            Paths.get(System.getProperty("user.home"), ".m2", "repository"),
            "repo.runestar.org"
    )
    aether.session.transferListener = frame

    try {
        Files.createDirectories(PLUGINS_DIR_PATH)
        moveArtifact(aether.updateArtifact(GROUP_ID, PLUGINS_STANDARD_ARTIFACT_ID), PLUGINS_STANDARD_PATH)
        moveArtifact(aether.updateArtifact(GROUP_ID, CLIENT_ARTIFACT_ID), CLIENT_PATH)
    } catch (e: Exception) {
        e.printStackTrace()
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

private fun moveArtifact(artifactResult: ArtifactResult, file: Path) {
    Files.createDirectories(file.parent)
    Files.copy(artifactResult.artifact.file.toPath(), file, StandardCopyOption.REPLACE_EXISTING)
}

private fun isRunFromIde(): Boolean {
     return MethodHandles.lookup().lookupClass().let { c ->
        c.getResource(c.simpleName + ".class").protocol == "file"
     }
}