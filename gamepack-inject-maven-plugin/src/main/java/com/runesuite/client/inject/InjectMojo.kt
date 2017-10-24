package com.runesuite.client.inject

import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.updater.GAMEPACK_CLEAN
import com.runesuite.client.updater.HOOKS
import com.runesuite.client.updater.common.decoderNarrowed
import com.runesuite.client.updater.common.finalArgumentNarrowed
import net.bytebuddy.ByteBuddy
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.scaffold.TypeValidation
import net.bytebuddy.implementation.FieldAccessor
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.implementation.bytecode.StackManipulation
import net.bytebuddy.implementation.bytecode.assign.Assigner
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant
import net.bytebuddy.implementation.bytecode.constant.LongConstant
import net.bytebuddy.implementation.bytecode.member.FieldAccess
import net.bytebuddy.implementation.bytecode.member.MethodReturn
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess
import net.bytebuddy.jar.asm.Type
import net.bytebuddy.pool.TypePool
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.zeroturnaround.zip.ZipUtil
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Mojo(name = "inject")
class InjectMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    private lateinit var project: MavenProject

    private val cleanJar by lazy { Paths.get(project.build.directory, "gamepack.clean.jar") }

    private val classesDir by lazy { Paths.get(project.build.directory, "classes") }

    private val accessPkg = XClient::class.java.`package`.name

    override fun execute() {
        Files.createDirectories(cleanJar.parent)
        GAMEPACK_CLEAN.openStream().use { input ->
            Files.copy(input, cleanJar, StandardCopyOption.REPLACE_EXISTING)
        }
        inject(cleanJar, classesDir)
    }

    private fun inject(sourceJar: Path, destinationFolder: Path) {
        val classFileLocator = ClassFileLocator.Compound(
                ClassFileLocator.ForClassLoader.ofClassPath(),
                ClassFileLocator.ForJarFile.of(sourceJar.toFile()))
        val typePool = TypePool.Default.of(classFileLocator)
        ZipUtil.unpack(sourceJar.toFile(), destinationFolder.toFile())
        val classNames = HOOKS.flatMap { (it.methods.map { it.owner }).plus(it.name) }.distinct()
        classNames.forEach { cn ->
            val typeDescription = typePool.describe(cn).resolve()
            var typeBuilder = ByteBuddy().with(TypeValidation.DISABLED).rebase<Any>(typeDescription, classFileLocator)
            HOOKS.forEach { ch ->
                val xClass = Class.forName("$accessPkg.X${ch.`class`}")
                if (cn == ch.name) {
                    typeBuilder = typeBuilder.implement(xClass)
                    log.debug("Injected interface: ${xClass.simpleName} -> ${ch.name}")
                    ch.fields.forEach { f ->
                        typeBuilder = typeBuilder.method { it.name == f.getterName }
                                .intercept(createFieldAccessor(typePool, f.name, f.owner, f.getterName, f.decoderNarrowed))
                        log.debug("Injected getter: ${f.getterName} -> ${ch.name} (${xClass.simpleName})")
                        if (!Modifier.isFinal(f.access)) {
                            typeBuilder = typeBuilder.method { it.name == f.setterName }
                                    .intercept(createFieldAccessor(typePool, f.name, f.owner, f.setterName, f.decoderNarrowed))
                            log.debug("Injected setter: ${f.setterName} -> ${ch.name} (${xClass.simpleName})" )
                        }
                    }
                    ch.methods.forEach { m ->
                        if (m.parameters != null && !typeDescription.isInterface) {
                            val sourceArgumentsSize = Type.getMethodType(m.descriptor).argumentTypes.size
                            val sourceMethodDescription = typePool.describe(m.owner).resolve().declaredMethods.first { it.name == m.name && it.descriptor == m.descriptor }
                            var methodCall = MethodCall.invoke(sourceMethodDescription).withAllArguments()
                            if (sourceArgumentsSize == m.parameters!!.size + 1) {
                                methodCall = methodCall.with(m.finalArgumentNarrowed ?: 0.toByte())
                            } else {
                                check(sourceArgumentsSize == m.parameters!!.size)
                            }
                            typeBuilder = typeBuilder.method { it.name == m.method }.intercept(methodCall.withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
                            log.debug("Injected method: ${m.method} -> ${ch.name} (${xClass.simpleName})")
                        }
                    }
                }
                ch.methods.forEach { m ->
                    if (m.parameters != null && m.owner == cn && !typeDescription.isInterface) {
                        val methodDescription = typeDescription.declaredMethods.first { it.name == m.name && it.descriptor == m.descriptor }
                        if (!methodDescription.isAbstract) {
                            typeBuilder = typeBuilder.method { it == methodDescription }
                                    .intercept(Advice.withCustomMapping()
                                            .bind(MethodAdvice.Execution::class.java, xClass.getDeclaredField(m.method))
                                            .to(MethodAdvice::class.java))
                            log.debug("Injected callbacks -> ${m.owner}.${m.name} (X${ch.`class`}.${m.method})")
                        }
                    }
                }
            }
            typeBuilder.make().saveIn(destinationFolder.toFile())
        }
    }

    private fun createFieldAccessor(typePool: TypePool, fieldName: String, fieldOwner: String, methodName: String, decoder: Number? = null): Implementation {
        val fieldDescription = typePool.describe(fieldOwner).resolve().declaredFields.first { it.name == fieldName }
        val fieldAccess = FieldAccess.forField(fieldDescription)
        val encoder = invert(decoder)
        return if (methodName.startsWith("get")) {
            when(decoder) {
                is Int -> Implementation.Simple(
                        if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                        fieldAccess.read(),
                        IntegerConstant.forValue(decoder),
                        Multiplication.INTEGER,
                        MethodReturn.INTEGER)
                is Long -> Implementation.Simple(
                        if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                        fieldAccess.read(),
                        LongConstant.forValue(decoder),
                        Multiplication.LONG,
                        MethodReturn.LONG)
                null -> FieldAccessor.ofField(fieldName).`in`(typePool.describe(fieldOwner).resolve())
                        .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
                else -> error(decoder)
            }
        } else if (methodName.startsWith("set")) {
            when(encoder) {
                is Int -> Implementation.Simple(
                        if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                        IntegerConstant.forValue(encoder),
                        MethodVariableAccess.INTEGER.loadFrom(1),
                        Multiplication.INTEGER,
                        fieldAccess.write(),
                        MethodReturn.VOID)
                is Long -> Implementation.Simple(
                        if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                        LongConstant.forValue(encoder),
                        MethodVariableAccess.LONG.loadFrom(1),
                        Multiplication.LONG,
                        fieldAccess.write(),
                        MethodReturn.VOID)
                null -> FieldAccessor.ofField(fieldName).`in`(typePool.describe(fieldOwner).resolve())
                        .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
                else -> error(encoder)
            }
        } else {
            error(methodName)
        }
    }
}