package com.runesuite.client.releaser.local

import com.runesuite.client.common.CORE
import com.runesuite.client.common.PLUGINS
import com.runesuite.client.common.PLUGINS_SETTINGS_DIR
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

        val core = resolveArtifact("client-core")
        Files.createDirectories(CORE.parent)
        Files.copy(core.file.toPath(), CORE, StandardCopyOption.REPLACE_EXISTING)

//        val gamepack = resolveArtifact("client-gamepack")
//        Files.createDirectories(GAMEPACK.parent)
//        Files.copy(gamepack.file.toPath(), GAMEPACK, StandardCopyOption.REPLACE_EXISTING)

        val plugins = resolveArtifact("client-plugins")
        Files.createDirectories(PLUGINS.parent)
        Files.copy(plugins.file.toPath(), PLUGINS, StandardCopyOption.REPLACE_EXISTING)

        Files.createDirectories(PLUGINS_SETTINGS_DIR)
    }

    private fun resolveArtifact(artifactId: String): Artifact {
        val ar = ArtifactRequest(DefaultArtifact(groupId, artifactId, "", "jar", version), null, null)
        return repositorySystem.resolveArtifact(session, ar).artifact
    }
}