package com.runesuite.client.updater.create

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.runesuite.client.updater.common.ClassHook
import com.runesuite.client.updater.common.FieldHook
import com.runesuite.client.updater.common.MethodHook
import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.mapper.std.classes.Client
import com.runesuite.general.downloadGamepack
import com.runesuite.general.updateRevision
import com.runesuite.mapper.IdClass
import com.runesuite.mapper.JarMapper
import com.runesuite.mapper.Mapper
import com.runesuite.mapper.buildIdHierarchy
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.jar.JarFile

@Mojo(name = "create")
class CreateMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    lateinit var project: MavenProject

    private val jsonMapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    private val targetDir by lazy { Paths.get(project.build.directory) }
    private val gamepackJar by lazy { targetDir.resolve("gamepack.jar") }
    private val deobJar by lazy { targetDir.resolve("gamepack.deob.jar") }
    private val namesJson by lazy { deobJar.appendFileName(".names.json") }
    private val opJson by lazy { deobJar.appendFileName(".op.json") }
    private val multJson by lazy { deobJar.appendFileName(".mult.json") }
    private val hooksJson by lazy { targetDir.resolve("hooks.json") }

    override fun execute() {
        if (Files.notExists(gamepackJar) || !verifyJar(gamepackJar)) {
            dl()
            deob()
            map()
            mergeHooks()
        } else if (Files.notExists(deobJar) || !verifyJar(deobJar) || Files.notExists(opJson) || Files.notExists(multJson)) {
            deob()
            map()
            mergeHooks()
        } else if (Files.notExists(namesJson) || Files.notExists(hooksJson)) {
            map()
            mergeHooks()
        }
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
        Deobfuscator.Faster.deob(gamepackJar, deobJar)
    }

    private fun map() {
        val ctx = Mapper.Context()
        JarMapper(Client::class.java.`package`.name).map(deobJar, ctx, updateRevision())
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
}