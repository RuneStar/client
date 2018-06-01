package org.runestar.client.updater.create

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.maven.model.Resource
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.mapper.IdClass
import org.runestar.client.updater.mapper.JarMapper
import org.runestar.client.updater.mapper.Mapper
import org.runestar.client.updater.mapper.buildIdHierarchy
import org.runestar.client.updater.mapper.std.classes.Client
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

@Mojo(
        name = "create",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
class CreateMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    lateinit var project: MavenProject

    private val jsonMapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

    private val targetDir: Path get() =  Paths.get(project.build.directory)

    private val generatedResourcesDir: Path get() = targetDir.resolve("generated-resources")

    private val gamepackJar: Path get() = targetDir.resolve("gamepack.jar")

    private val deobJar: Path get() = targetDir.resolve("gamepack.deob.jar")

    private val cleanJar: Path get() = targetDir.resolve("gamepack.clean.jar")

    private val namesJson: Path get() = deobJar.appendFileName(".names.json")

    private val opJson: Path get() = deobJar.appendFileName(".op.json")

    private val multJson: Path get() = deobJar.appendFileName(".mult.json")

    private val hooksJson: Path get() = targetDir.resolve("hooks.json")

    override fun execute() {
        if (Files.notExists(gamepackJar) || !verifyJar(gamepackJar)) {
            dl()
        }
        deob()
        clean()
        map()
        mergeHooks()

        addResources()
    }

    private fun verifyJar(jar: Path): Boolean {
        return try {
            JarFile(jar.toFile(), true).close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun Path.appendFileName(string: String): Path {
        return resolveSibling(this.fileName.toString() + string)
    }

    private fun dl() {
        downloadGamepack(gamepackJar)
    }

    private fun deob() {
        Transformer.DEFAULT.transform(gamepackJar, deobJar)
    }

    private fun clean() {
        Transformer.CLEAN.transform(gamepackJar, cleanJar)
    }

    private fun map() {
        val ctx = Mapper.Context()
        val clientClass = Client::class.java
        JarMapper(clientClass.`package`.name, clientClass.classLoader).map(deobJar, ctx, updateRevision())
        jsonMapper.writeValue(namesJson.toFile(), ctx.buildIdHierarchy())
    }

    private fun mergeHooks() {
        val ops = jsonMapper.readValue<Map<String, Int>>(opJson.toFile())
        val mults = jsonMapper.readValue<Map<String, Long>>(multJson.toFile())
        val names = jsonMapper.readValue<List<IdClass>>(namesJson.toFile())
        val hooks = names.map { c ->
            val fields = c.fields.map { f ->
                FieldHook(f.field, f.owner, f.name, f.access, f.descriptor, mults["${f.owner}.${f.name}"])
            }
            val methods = c.methods.map { m ->
                MethodHook(m.method, m.owner, m.name, m.access, m.parameters, m.descriptor, ops["${m.owner}.${m.name}${m.descriptor}"])
            }
            ClassHook(c.`class`, c.name, c.`super`, c.access, c.interfaces, fields, methods)
        }
        jsonMapper.writeValue(hooksJson.toFile(), hooks)
    }

    private fun addResources() {
        Files.createDirectories(generatedResourcesDir)
        project.addResource(Resource().apply {
            directory = generatedResourcesDir.toString()
        })
        Files.copy(gamepackJar, generatedResourcesDir.resolve(gamepackJar.fileName), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(cleanJar, generatedResourcesDir.resolve(cleanJar.fileName), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(hooksJson, generatedResourcesDir.resolve(hooksJson.fileName), StandardCopyOption.REPLACE_EXISTING)
    }
}