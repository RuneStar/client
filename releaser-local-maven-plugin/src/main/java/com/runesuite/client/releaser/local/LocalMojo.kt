package com.runesuite.client.releaser.local

import com.runesuite.client.common.CLIENT_PATH
import com.runesuite.client.common.PLUGINS_PATH
import com.runesuite.client.common.PLUGINS_SETTINGS_DIR_PATH
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.resolution.ArtifactRequest
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Mojo(name = "local", aggregator = true)
class LocalMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project.version}")
    lateinit var version: String

    @Parameter(defaultValue = "\${project.groupId}")
    lateinit var groupId: String

    @Parameter(defaultValue = "\${repositorySystemSession}")
    lateinit var session: RepositorySystemSession

    @Component
    lateinit var repositorySystem: RepositorySystem

    override fun execute() {

        val plugins = resolveArtifact("client-plugins-standard")
        Files.createDirectories(PLUGINS_PATH.parent)
        try {
            Files.copy(plugins.file.toPath(), PLUGINS_PATH, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            log.warn("Cannot write to $PLUGINS_PATH")
            log.warn(e)
        }

        val client = resolveArtifact("client")
        Files.createDirectories(CLIENT_PATH.parent)
        try {
            Files.copy(client.file.toPath(), CLIENT_PATH, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            log.warn("Cannot write to $CLIENT_PATH")
            log.warn(e)
        }

        Files.createDirectories(PLUGINS_SETTINGS_DIR_PATH)
    }

    private fun resolveArtifact(artifactId: String): Artifact {
        val ar = ArtifactRequest(DefaultArtifact(groupId, artifactId, "", "jar", version), null, null)
        return repositorySystem.resolveArtifact(session, ar).artifact
    }
}