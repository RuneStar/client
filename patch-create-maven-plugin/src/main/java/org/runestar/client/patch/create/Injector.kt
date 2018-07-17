package org.runestar.client.patch.create

import com.google.common.io.Files
import net.bytebuddy.ByteBuddy
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.method.ParameterList
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.scaffold.TypeValidation
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer
import net.bytebuddy.implementation.FieldAccessor
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.implementation.bytecode.Multiplication
import net.bytebuddy.implementation.bytecode.StackManipulation
import net.bytebuddy.implementation.bytecode.assign.Assigner
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant
import net.bytebuddy.implementation.bytecode.constant.LongConstant
import net.bytebuddy.implementation.bytecode.member.FieldAccess
import net.bytebuddy.implementation.bytecode.member.MethodReturn
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess
import net.bytebuddy.jar.asm.Type
import net.bytebuddy.pool.TypePool
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook
import org.runestar.client.updater.readHooks
import org.zeroturnaround.zip.ZipUtil
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.zip.Deflater

private val ACCESS_PKG = XClient::class.java.`package`.name

private val BYTEBUDDY = ByteBuddy().with(TypeValidation.DISABLED)

private val METHOD_NAME_TRANSFORMER = MethodNameTransformer.Suffixing("0")

fun inject(sourceJar: Path, destinationJar: Path) {
    val tempDir = Files.createTempDir().toPath()
    val classFileLocator = ClassFileLocator.Compound(
            ClassFileLocator.ForClassLoader.ofClassPath(),
            ClassFileLocator.ForJarFile.of(sourceJar.toFile())
    )
    val typePool = TypePool.Default.of(classFileLocator)
    ZipUtil.unpack(sourceJar.toFile(), tempDir.toFile())
    val hooks = readHooks()
    val classNameMap = hooks.associate { it.name to it.`class` }
    hookClassNames(hooks).forEach { cn ->
        val typeDescription = typePool.describe(cn).resolve()
        var typeBuilder = BYTEBUDDY.rebase<Any>(typeDescription, classFileLocator, METHOD_NAME_TRANSFORMER)
        hooks.forEach { ch ->
            val xClass = Class.forName("$ACCESS_PKG.X${ch.`class`}")
            if (cn == ch.name) {
                typeBuilder = typeBuilder.implement(xClass)
//                        log.info("Injected interface: ${xClass.simpleName} -> ${ch.name}")
                ch.fields.forEach { f ->
                    typeBuilder = typeBuilder.method(f.getterMatcher())
                            .intercept(createFieldGetter(typePool, f))
//                            log.info("Injected getter: ${f.getterName} -> ${ch.name} (${xClass.simpleName})")
                    if (!Modifier.isFinal(f.access)) {
                        typeBuilder = typeBuilder.method(f.setterMatcher())
                                .intercept(createFieldSetter(typePool, f))
//                                log.info("Injected setter: ${f.setterName} -> ${ch.name} (${xClass.simpleName})" )
                    }
                }
                ch.methods.forEach { m ->
                    if (m.parameters != null && !Modifier.isAbstract(m.access)) {
                        typeBuilder = typeBuilder.method(m.proxyMatcher())
                                .intercept(createMethodProxy(typePool, m))
//                                log.info("Injected method: ${m.method} -> ${ch.name} (${xClass.simpleName})")
                    }
                }
                if (cn == "client") {
                    hooks.filter { !Modifier.isAbstract(it.access) }.forEach { hc ->
                        hc.constructors.forEach { hcon ->
                            val conOwner = typePool.describe(hc.name).resolve()
                            val con = conOwner.declaredMethods.first { it.isConstructor && it.descriptor == hcon.descriptor }
                            typeBuilder = typeBuilder.method { it.name == hc.constructorName && descMatches(hcon.descriptor, it.parameters, classNameMap) }
                                    .intercept(MethodCall.construct(con).withAllArguments().withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
                        }
                    }
                }
            }
            ch.methods.forEach { m ->
                if (m.parameters != null && m.owner == cn && !Modifier.isAbstract(m.access)) {
                    val execField = xClass.getDeclaredField(m.method)
                    typeBuilder = typeBuilder.method(m.matcher())
                            .intercept(
                                    Advice.withCustomMapping()
                                            .bind(MethodAdvice.Execution::class.java, execField, Assigner.Typing.DYNAMIC)
                                            .to(MethodAdvice::class.java)
                            )
//                            log.info("Injected callbacks -> ${m.owner}.${m.name} (X${ch.`class`}.${m.method})")
                }
            }
        }
        typeBuilder.make().saveIn(tempDir.toFile())
    }
    ZipUtil.pack(tempDir.toFile(), destinationJar.toFile(), Deflater.NO_COMPRESSION)
}

private fun descMatches(desc: String, params: ParameterList<*>, classNameMap: Map<String, String>): Boolean {
    val args = Type.getArgumentTypes(desc)
    if (args.size != params.size) return false
    for (i in 0 until args.size) {
        val at = args[i]
        val bt = Type.getType((params[i].type.asErasure() as TypeDescription.AbstractBase).descriptor)
        if (!typesEq(at, bt, classNameMap)) return false
    }
    return true
}

private fun typesEq(hookType: Type, accesorType: Type, classNameMap: Map<String, String>): Boolean {
    if (hookType.sort != accesorType.sort) return false
    var an = hookType.className
    var bn = accesorType.className
    if (hookType.sort == Type.ARRAY) {
        if (hookType.dimensions != accesorType.dimensions) return false
        an = hookType.elementType.className
        bn = accesorType.elementType.className
    }
    if (an in classNameMap) {
        an = ACCESS_PKG + ".X" + classNameMap[an]
    }
    return an == bn
}

private fun createMethodProxy(typePool: TypePool, methodHook: MethodHook): Implementation {
    val sourceMethodDescription = typePool.describe(methodHook.owner).resolve().declaredMethods.filter(methodHook.matcher()).only
    var methodCall = MethodCall.invoke(sourceMethodDescription).withAllArguments()
    if (methodHook.argumentsCount == methodHook.actualArgumentsCount + 1) {
        methodCall = methodCall.with(methodHook.finalArgumentNarrowed ?: 0.toByte())
    } else {
        check(methodHook.argumentsCount == methodHook.actualArgumentsCount)
    }
    return methodCall.withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
}

private fun createFieldGetter(typePool: TypePool, fieldHook: FieldHook): Implementation {
    val fieldOwnerDescription = typePool.describe(fieldHook.owner).resolve()
    val fieldDescription = fieldOwnerDescription.declaredFields.filter(fieldHook.matcher()).only
    val fieldAccess = FieldAccess.forField(fieldDescription)
    val decoder = fieldHook.decoderNarrowed
    return when(decoder) {
        is Int -> {
            Implementation.Simple(
                    if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                    fieldAccess.read(),
                    IntegerConstant.forValue(decoder),
                    Multiplication.INTEGER,
                    MethodReturn.INTEGER
            )
        }
        is Long -> {
            Implementation.Simple(
                    if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                    fieldAccess.read(),
                    LongConstant.forValue(decoder),
                    Multiplication.LONG,
                    MethodReturn.LONG
            )
        }
        null -> {
            FieldAccessor.ofField(fieldHook.name).`in`(fieldOwnerDescription)
                    .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
        }
        else -> error(decoder)
    }
}

private fun createFieldSetter(typePool: TypePool, fieldHook: FieldHook): Implementation {
    val fieldOwnerDescription = typePool.describe(fieldHook.owner).resolve()
    val fieldDescription = fieldOwnerDescription.declaredFields.filter(fieldHook.matcher()).only
    val fieldAccess = FieldAccess.forField(fieldDescription)
    val encoder = fieldHook.encoderNarrowed
    return when(encoder) {
        is Int -> {
            Implementation.Simple(
                    if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                    IntegerConstant.forValue(encoder),
                    MethodVariableAccess.INTEGER.loadFrom(1),
                    Multiplication.INTEGER,
                    fieldAccess.write(),
                    MethodReturn.VOID
            )
        }
        is Long -> {
            Implementation.Simple(
                    if (fieldDescription.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                    LongConstant.forValue(encoder),
                    MethodVariableAccess.LONG.loadFrom(1),
                    Multiplication.LONG,
                    fieldAccess.write(),
                    MethodReturn.VOID
            )
        }
        null -> {
            FieldAccessor.ofField(fieldHook.name).`in`(fieldOwnerDescription)
                    .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
        }
        else -> error(encoder)
    }
}