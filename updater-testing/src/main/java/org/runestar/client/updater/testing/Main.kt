package org.runestar.client.updater.testing

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.common.*
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.std.classes.Client
import java.nio.file.Paths

val WORK_DIR = Paths.get("updater-testing", "workspace")

val jsonMapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

fun main(args: Array<String>) {
    // 127+
    for (revision in 141..161) {
        if (revision in setOf(146, 147)) continue
        println(revision)

//        deob(revision)
        map(revision)
        mergeHooks(revision)
        rename(revision)
    }
}

fun deob(revision: Int) {
    Transformer.TESTING.transform(gamepack(revision), gamepackDeob(revision))
}

fun map(revision: Int) {
    val ctx = Mapper.Context()
    val clientClass = Client::class.java
    JarMapper(clientClass.`package`.name, clientClass.classLoader).map(gamepackDeob(revision), ctx, revision)
    jsonMapper.writeValue(namesJson(revision).toFile(), ctx.buildIdHierarchy())
}

fun mergeHooks(revision: Int) {
    val ops = jsonMapper.readValue<Map<String, Int>>(opJson(revision).toFile())
    val mults = jsonMapper.readValue<Map<String, Long>>(multJson(revision).toFile())
    val names = jsonMapper.readValue<List<IdClass>>(namesJson(revision).toFile())
    val hooks = names.map { c ->
        val fields = c.fields.map { f ->
            FieldHook(f.field, f.owner, f.name, f.access, f.descriptor, mults["${f.owner}.${f.name}"])
        }
        val methods = c.methods.map { m ->
            MethodHook(m.method, m.owner, m.name, m.access, m.parameters, m.descriptor, ops["${m.owner}.${m.name}${m.descriptor}"])
        }
        ClassHook(c.`class`, c.name, c.`super`, c.access, c.interfaces, fields, methods)
    }
    jsonMapper.writeValue(hooksJson(revision).toFile(), hooks)
}

fun rename(revision: Int) {
    val names = jsonMapper.readValue<List<IdClass>>(namesJson(revision).toFile())
    val remapper = IdRemapper(names, readJar(gamepackDeob(revision)))
    Renamer(remapper).transform(gamepackDeob(revision), renamed(revision))
}

fun gamepack(revision: Int) = Paths.get("updater-testing", "reference", "runescape-gamepack.$revision.jar")
fun gamepackDeob(revision: Int) = WORK_DIR.resolve("deob.$revision.jar")
fun namesJson(revision: Int) = WORK_DIR.resolve("names.$revision.json")
fun opJson(revision: Int) = WORK_DIR.resolve("deob.$revision.jar.op.json")
fun multJson(revision: Int) = WORK_DIR.resolve("deob.$revision.jar.mult.json")
fun hooksJson(revision: Int) = WORK_DIR.resolve("hooks.$revision.json")
fun renamed(revision: Int) = WORK_DIR.resolve("renamed.$revision.jar")