package com.runesuite.client.updater.deob.jagex

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import mu.KotlinLogging
import org.objectweb.asm.Attribute
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.TreeSet

object JarInfo : Deobfuscator {

    private val logger = KotlinLogging.logger { }

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val versions = classNodes.map { it.version }.toSortedSet()
        logger.debug { "Class file version: $versions" }

        val signatures = TreeSet<String>()
        classNodes.mapNotNull { it.signature }.toCollection(signatures)
        classNodes.flatMap { it.fields }.mapNotNull { it.signature }.toCollection(signatures)
        classNodes.flatMap { it.methods }.mapNotNull { it.signature }.toCollection(signatures)
        logger.debug { "Signatures: $signatures" }

        val sourceDebug = classNodes.mapNotNull { it.sourceDebug }.toSortedSet()
        logger.debug { "Class source debug: $sourceDebug" }

        val outerMethods = classNodes.filter { it.outerMethod != null }.map { it.name + ":" + it.outerClass + "." + it.outerMethod + it.outerMethodDesc }.sorted()
        logger.debug { "Outer methods: $outerMethods" }

        val outerClasses = classNodes.filter { it.outerClass != null && it.outerMethod == null }.map { it.name + ":" + it.outerClass }.sorted()
        logger.debug { "Outer classes: $outerClasses" }

        val attributes = TreeSet<Attribute>()
        classNodes.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        classNodes.flatMap { it.fields }.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        classNodes.flatMap { it.methods }.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        logger.debug { "Attributes: $attributes" }

        val annotations = TreeSet<AnnotationNode>()
        classNodes.mapNotNull { it.visibleAnnotations }.flatten().toCollection(annotations)
        classNodes.mapNotNull { it.visibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.mapNotNull { it.invisibleAnnotations }.flatten().toCollection(annotations)
        classNodes.mapNotNull { it.invisibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.fields }.mapNotNull { it.visibleAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.fields }.mapNotNull { it.visibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.fields }.mapNotNull { it.invisibleAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.fields }.mapNotNull { it.invisibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.visibleAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.visibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.invisibleAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.invisibleTypeAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.visibleParameterAnnotations?.flatMap { it } }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.invisibleParameterAnnotations?.flatMap { it } }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.visibleLocalVariableAnnotations }.flatten().toCollection(annotations)
        classNodes.flatMap { it.methods }.mapNotNull { it.invisibleLocalVariableAnnotations }.flatten().toCollection(annotations)
        logger.debug { "Annotations: $annotations" }

        val localVariables = classNodes.flatMap { it.methods }.mapNotNull { it.localVariables }.flatten()
        logger.debug { "Local variables: $localVariables" }

        val params = classNodes.flatMap { it.methods }.mapNotNull { it.parameters }.flatten()
        logger.debug { "Parameters: $params" }

        val interesting = TreeSet<String>()
        classNodes.forEach { c ->
            if (c.access and Opcodes.ACC_SYNTHETIC != 0 || c.name.contains('$') || (c.name != "client" && c.name.length > 2)) {
                interesting.add(c.name)
            }
            c.fields.forEach { f ->
                if (f.access and Opcodes.ACC_SYNTHETIC != 0 || f.name.length > 2 || f.name.contains('$')) {
                    interesting.add(c.name + "." + f.name)
                }
            }
            c.methods.forEach { m ->
                if (m.access and (Opcodes.ACC_SYNTHETIC or Opcodes.ACC_BRIDGE) != 0 || m.name.contains('$')) {
                    interesting.add(c.name + "." + m.name + m.desc)
                }
            }
        }
        logger.debug { "Interesting members: $interesting" }

        if (source != destination) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}