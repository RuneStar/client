package com.runesuite.client.updater.testing

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.runesuite.client.updater.common.ClassHook
import com.runesuite.client.updater.common.FieldHook
import com.runesuite.client.updater.common.MethodHook
import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.common.*
import com.runesuite.client.updater.deob.common.controlflow.ControlFlowFixer
import com.runesuite.client.updater.deob.jagex.*
import com.runesuite.client.updater.mapper.std.classes.Client
import com.runesuite.mapper.IdClass
import com.runesuite.mapper.JarMapper
import com.runesuite.mapper.Mapper
import com.runesuite.mapper.buildIdHierarchy
import java.awt.datatransfer.Clipboard
import java.awt.event.KeyEvent
import java.nio.ByteBuffer
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

val WORK_DIR = Paths.get("updater-testing", "workspace")

val jsonMapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

fun main(args: Array<String>) {
    for (revision in 127..156) {
        if (revision in setOf(146, 147)) continue
        println(revision)

//        deob(revision)
        map(revision)
        mergeHooks(revision)
        rename(revision)
    }
}

fun deob(revision: Int) {
    Deobfuscator.All.deob(gamepack(revision), gamepackDeob(revision))
}

fun map(revision: Int) {
    val ctx = Mapper.Context()
    JarMapper(Client::class.java.`package`.name).map(gamepackDeob(revision), ctx, revision)
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
    Renamer(IdRenaming(names, gamepackDeob(revision))).deob(gamepackDeob(revision), renamed(revision))
}

fun gamepack(revision: Int) = Paths.get("updater-testing", "reference", "runescape-gamepack.$revision.jar")
fun gamepackDeob(revision: Int) = WORK_DIR.resolve("deob.$revision.jar")
fun namesJson(revision: Int) = WORK_DIR.resolve("names.$revision.json")
fun opJson(revision: Int) = WORK_DIR.resolve("deob.$revision.jar.op.json")
fun multJson(revision: Int) = WORK_DIR.resolve("deob.$revision.jar.mult.json")
fun hooksJson(revision: Int) = WORK_DIR.resolve("hooks.$revision.json")
fun renamed(revision: Int) = WORK_DIR.resolve("renamed.$revision.jar")