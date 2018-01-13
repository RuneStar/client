package org.runestar.client.launcher

import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.repository.RepositoryPolicy
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.VersionRangeRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import java.nio.file.Path

class Aether(
        localRepoPath: Path,
        remoteRepoHost: String
) {

    private val locator = MavenRepositorySystemUtils.newServiceLocator().apply {
        addService(TransporterFactory::class.java, FileTransporterFactory::class.java)
        addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
        addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
    }

    private val repoSystem = locator.getService(RepositorySystem::class.java)

    private val localRepo = LocalRepository(localRepoPath.toFile())

    val session = MavenRepositorySystemUtils.newSession().apply {
        localRepositoryManager = repoSystem.newLocalRepositoryManager(this, localRepo)
    }

    private val remoteRepo =
            RemoteRepository.Builder(remoteRepoHost, "default", "http://$remoteRepoHost")
                    .setPolicy(
                            RepositoryPolicy(
                                    true,
                                    RepositoryPolicy.UPDATE_POLICY_ALWAYS,
                                    RepositoryPolicy.CHECKSUM_POLICY_FAIL
                            )
                    )
                    .build()

    fun updateArtifact(
            groupId: String,
            artifactId: String
    ): ArtifactResult {
        val artifact = DefaultArtifact(groupId, artifactId, null, "jar", "[,)")
        val versionRangeRequest = VersionRangeRequest(artifact, listOf(remoteRepo), null)
        val versionRangeResponse = repoSystem.resolveVersionRange(session, versionRangeRequest)
        val artifactVersion = versionRangeResponse.highestVersion.toString()
        val artifact2 = DefaultArtifact(groupId, artifactId, null, "jar", artifactVersion)
        val artifactRequest = ArtifactRequest(artifact2, listOf(remoteRepo), null)
        val artifactResponse = repoSystem.resolveArtifact(session, artifactRequest)
        return artifactResponse
    }
}