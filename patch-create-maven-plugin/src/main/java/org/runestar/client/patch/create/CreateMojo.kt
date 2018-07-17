package org.runestar.client.patch.create

import io.sigpipe.jbsdiff.Diff
import org.apache.maven.model.Resource
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.runestar.client.updater.cleanGamepackUrl
import org.runestar.client.updater.gamepackUrl
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Mojo(
        name = "create",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
class CreateMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    private lateinit var project: MavenProject

    override fun execute() {
        val dir = Paths.get(project.build.directory)

        val cleanJar = dir.resolve("gamepack.clean.jar")
        cleanGamepackUrl.openStream().use { input ->
            Files.copy(input, cleanJar, StandardCopyOption.REPLACE_EXISTING)
        }

        val injectedJar = dir.resolve("gamepack.inject.jar")
        inject(cleanJar, injectedJar)

        val diffFile = dir.resolve("gamepack.diff.gz")
        Files.newOutputStream(diffFile).use { output ->
            Diff.diff(gamepackUrl.readBytes(), Files.readAllBytes(injectedJar), output)
        }

        val generatedResourcesDir = dir.resolve("generated-resources")
        Files.createDirectories(generatedResourcesDir)
        Files.copy(diffFile, generatedResourcesDir.resolve(diffFile.fileName), StandardCopyOption.REPLACE_EXISTING)
        project.addResource(Resource().apply {
            directory = generatedResourcesDir.toString()
        })
    }
}