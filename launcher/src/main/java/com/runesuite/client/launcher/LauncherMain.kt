@file:JvmName("LauncherMain")

package com.runesuite.client.launcher

import com.runesuite.client.common.*
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.repository.RepositoryPolicy
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.VersionRangeRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import java.awt.Dimension
import java.awt.Window
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.JDialog
import javax.swing.JLabel

private val locator =  MavenRepositorySystemUtils.newServiceLocator().apply {
    addService(TransporterFactory::class.java, FileTransporterFactory::class.java)
    addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
    addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
}

private val repoSystem = locator.getService(RepositorySystem::class.java)

private val localRepo = LocalRepository(Paths.get(System.getProperty("user.home"), ".m2", "repository").toFile())

private val session = MavenRepositorySystemUtils.newSession().apply {
    localRepositoryManager = repoSystem.newLocalRepositoryManager(this, localRepo)
}

private val remoteRepo = RemoteRepository.Builder("repo.runesuite.com", "default", "http://repo.runesuite.com")
        .setPolicy(RepositoryPolicy(true, RepositoryPolicy.UPDATE_POLICY_ALWAYS, RepositoryPolicy.CHECKSUM_POLICY_FAIL))
        .build()

fun main(args: Array<String>) {
    val window = startLoadingWindow()
    Files.createDirectories(PLUGINS_DIR_PATH)
    try {
        updateArtifact(PLUGINS_STANDARD_ARTIFACT_ID, PLUGINS_STANDARD_PATH)
        updateArtifact(CLIENT_ARTIFACT_ID, CLIENT_PATH)
    } catch (e: Exception) {
        e.printStackTrace()
        System.exit(1)
    }
    window.dispose()
    ProcessBuilder("java", "-jar", CLIENT_PATH.toString(), *args)
            .inheritIO().start().waitFor()
}

private fun startLoadingWindow(): Window {
    return JDialog().apply {
        isUndecorated = true
        setLocationRelativeTo(null)
        add(JLabel("Loading RuneSuite...", JLabel.CENTER).apply {
            font = font.deriveFont(20f)
        })
        size = Dimension(250, 80)
        isModal = false
        isVisible = true
    }
}

private fun updateArtifact(artifactId: String, path: Path) {
    val mvnPath = updateArtifactLocalRepo(artifactId)
    Files.createDirectories(path.parent)
    Files.copy(mvnPath, path, StandardCopyOption.REPLACE_EXISTING)
}

private fun updateArtifactLocalRepo(artifactId: String): Path {
    val artifact = DefaultArtifact(GROUP_ID, artifactId, null, "jar", "(,]")
    val versionRangeRequest = VersionRangeRequest(artifact, listOf(remoteRepo), null)
    val versionRangeResponse = repoSystem.resolveVersionRange(session, versionRangeRequest)
    val artifactVersion = versionRangeResponse.highestVersion.toString()
    val artifact2 = DefaultArtifact(GROUP_ID, artifactId, null, "jar", artifactVersion)
    val artifactRequest = ArtifactRequest(artifact2, listOf(remoteRepo), null)
    val artifactResponse = repoSystem.resolveArtifact(session, artifactRequest)
    println(artifactResponse)
    return artifactResponse.artifact.file.toPath()
}