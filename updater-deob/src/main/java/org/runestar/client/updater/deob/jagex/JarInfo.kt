package org.runestar.client.updater.deob.jagex

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Attribute
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*

object JarInfo : Transformer {

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val versions = classNodes.map { it.version }.toSortedSet()
        logger.info { "Class file version: $versions" }

        val signatures = TreeSet<String>()
        classNodes.mapNotNull { it.signature }.toCollection(signatures)
        classNodes.flatMap { it.fields }.mapNotNull { it.signature }.toCollection(signatures)
        classNodes.flatMap { it.methods }.mapNotNull { it.signature }.toCollection(signatures)
        logger.info { "Signatures: $signatures" }

        val sourceDebug = classNodes.mapNotNull { it.sourceDebug }.toSortedSet()
        logger.info { "Class source debug: $sourceDebug" }

        val outerMethods = classNodes.filter { it.outerMethod != null }.map { it.name + ":" + it.outerClass + "." + it.outerMethod + it.outerMethodDesc }.sorted()
        logger.info { "Outer methods: $outerMethods" }

        val outerClasses = classNodes.filter { it.outerClass != null && it.outerMethod == null }.map { it.name + ":" + it.outerClass }.sorted()
        logger.info { "Outer classes: $outerClasses" }

        val attributes = TreeSet<Attribute>()
        classNodes.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        classNodes.flatMap { it.fields }.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        classNodes.flatMap { it.methods }.mapNotNull { it.attrs }.flatten().toCollection(attributes)
        logger.info { "Attributes: $attributes" }

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
        logger.info { "Annotations: $annotations" }

        val localVariables = classNodes.flatMap { it.methods }.mapNotNull { it.localVariables }.flatten()
        logger.info { "Local variables: $localVariables" }

        val params = classNodes.flatMap { it.methods }.mapNotNull { it.parameters }.flatten()
        logger.info { "Parameters: $params" }

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
        logger.info { "Interesting members: $interesting" }

        if (source != destination) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}