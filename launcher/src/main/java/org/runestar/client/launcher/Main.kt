@file:JvmName("Main")

package org.runestar.client.launcher

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
import org.runestar.client.common.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.JOptionPane

private val locator = MavenRepositorySystemUtils.newServiceLocator().apply {
    addService(TransporterFactory::class.java, FileTransporterFactory::class.java)
    addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
    addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
}

private val repoSystem = locator.getService(RepositorySystem::class.java)

private val localRepo =
        LocalRepository(Paths.get(System.getProperty("user.home"), ".m2", "repository").toFile())

private val session = MavenRepositorySystemUtils.newSession().apply {
    localRepositoryManager = repoSystem.newLocalRepositoryManager(this, localRepo)
}

private val remoteRepo =
        RemoteRepository.Builder("repo.runestar.org", "default", "http://repo.runestar.org")
                .setPolicy(
                        RepositoryPolicy(
                                true,
                                RepositoryPolicy.UPDATE_POLICY_ALWAYS,
                                RepositoryPolicy.CHECKSUM_POLICY_FAIL
                        )
                )
                .build()

fun main(args: Array<String>) {
    val frame = LaunchFrame()
    session.transferListener = frame
    try {
        Files.createDirectories(PLUGINS_DIR_PATH)
        updateArtifact(PLUGINS_STANDARD_ARTIFACT_ID, PLUGINS_STANDARD_PATH)
        updateArtifact(CLIENT_ARTIFACT_ID, CLIENT_PATH)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        frame.dispose()
    }
    ProcessBuilder("java", "-jar", CLIENT_PATH.toString())
            .redirectErrorStream(true)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .start()
}

private fun updateArtifact(artifactId: String, path: Path) {
    val mvnPath = updateArtifactLocalRepo(artifactId)
    Files.createDirectories(path.parent)
    Files.copy(mvnPath, path, StandardCopyOption.REPLACE_EXISTING)
}

private fun updateArtifactLocalRepo(artifactId: String): Path {
    val artifact = DefaultArtifact(GROUP_ID, artifactId, null, "jar", "[,)")
    val versionRangeRequest = VersionRangeRequest(artifact, listOf(remoteRepo), null)
    val versionRangeResponse = repoSystem.resolveVersionRange(session, versionRangeRequest)
    val artifactVersion = versionRangeResponse.highestVersion.toString()
    val artifact2 = DefaultArtifact(GROUP_ID, artifactId, null, "jar", artifactVersion)
    val artifactRequest = ArtifactRequest(artifact2, listOf(remoteRepo), null)
    val artifactResponse = repoSystem.resolveArtifact(session, artifactRequest)
    println("using artifact $artifactResponse")
    return artifactResponse.artifact.file.toPath()
}