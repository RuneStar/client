package com.runesuite.client.inject

import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.updater.GAMEPACK
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
    lateinit var project: MavenProject

    val cleanJar by lazy { Paths.get(project.build.directory, "gamepack.clean.jar") }

    val classesDir by lazy { Paths.get(project.build.directory, "classes") }

    val injectJar by lazy { Paths.get(project.build.directory, "gamepack.inject.jar") }

    val accessPkg = XClient::class.java.`package`.name

    override fun execute() {
        Files.createDirectories(cleanJar.parent)
        val inStream = GAMEPACK_CLEAN.openStream()
        Files.copy(inStream, cleanJar, StandardCopyOption.REPLACE_EXISTING)
        inStream.close()
        jarInject(cleanJar, injectJar)
        ZipUtil.unpack(injectJar.toFile(), classesDir.toFile())
    }

    private fun jarInject(sourceJar: Path, destinationJar: Path) {
        val classFileLocator = ClassFileLocator.Compound(
                ClassFileLocator.ForClassLoader.ofClassPath(),
                ClassFileLocator.ForJarFile.of(sourceJar.toFile()))
        val typePool = TypePool.Default.of(classFileLocator)
        Files.copy(sourceJar, destinationJar, StandardCopyOption.REPLACE_EXISTING)
        val classNames = HOOKS.flatMap { (it.methods.map { it.owner }) + it.name }.distinct()
        classNames.forEach { cn ->
            val typeDescription = typePool.describe(cn).resolve()
            var typeBuilder = ByteBuddy().with(TypeValidation.DISABLED).rebase<Any>(typeDescription, classFileLocator)
            HOOKS.forEach { ch ->
                if (cn == ch.name) {
                    val itfClass = Class.forName("$accessPkg.X${ch.`class`}")
                    typeBuilder = typeBuilder.implement(itfClass)
                    log.debug("Injected interface: ${itfClass.simpleName} -> ${ch.name}")
                    ch.fields.forEach { f ->
                        val fieldOwner = f.owner
                        val getterName = "get${f.field.capitalize()}"
                        val setterName = "set${f.field.capitalize()}"
                        val decoder = f.decoderNarrowed
                        typeBuilder = typeBuilder.method { it.name == getterName }
                                .intercept(createFieldAccessor(typePool, f.name, fieldOwner, getterName, decoder))
                        log.debug("Injected getter: $getterName -> ${ch.name} (${itfClass.simpleName})")
                        if (!Modifier.isFinal(f.access)) {
                            typeBuilder = typeBuilder.method { it.name == setterName }
                                    .intercept(createFieldAccessor(typePool, f.name, fieldOwner, setterName, decoder))
                            log.debug("Injected setter: $setterName -> ${ch.name} (${itfClass.simpleName})" )
                        }
                    }
                    ch.methods.forEach { m ->
                        if (m.parameters != null && !typeDescription.isInterface) {
                            val sourceArgumentsSize = Type.getMethodType(m.descriptor).argumentTypes.size
                            val sourceMethodOwner = m.owner
                            val sourceMethodDescription = typePool.describe(sourceMethodOwner).resolve().declaredMethods.first { it.name == m.name && it.descriptor == m.descriptor }
                            var methodCall = MethodCall.invoke(sourceMethodDescription).withAllArguments()
                            if (sourceArgumentsSize == m.parameters!!.size + 1) {
                                methodCall = methodCall.with(m.finalArgumentNarrowed ?: 0.toByte())
                            } else {
                                check(sourceArgumentsSize == m.parameters!!.size)
                            }
                            typeBuilder = typeBuilder.method { it.name == m.method }.intercept(methodCall.withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
                            log.debug("Injected method: ${m.method} -> ${ch.name} (${itfClass.simpleName})")
                        }
                    }
                }
                val xClass = Class.forName("$accessPkg.X${ch.`class`}")
                ch.methods.forEach { mh ->
                    val methodOwner = mh.owner
                    if (mh.parameters != null && methodOwner == cn && !typeDescription.isInterface) {
                        val methodDescription = typeDescription.declaredMethods.first { it.name == mh.name && it.descriptor == mh.descriptor }
                        if (!methodDescription.isAbstract) {
                            val executionField = xClass.getDeclaredField(mh.method)
                            typeBuilder = typeBuilder.method { it == methodDescription }
                                    .intercept(
                                            Advice.withCustomMapping()
                                                    .bind(MethodAdvice.Execution::class.java, executionField)
                                                    .to(MethodAdvice::class.java))
                            log.debug("Injected callbacks -> $methodOwner.${mh.name} (X${ch.`class`}.${mh.method})")
                        }
                    }
                }
            }
            typeBuilder.make().inject(destinationJar.toFile())
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