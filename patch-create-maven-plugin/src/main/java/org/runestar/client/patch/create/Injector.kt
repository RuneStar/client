package org.runestar.client.patch.create

import com.google.common.io.Files
import net.bytebuddy.ByteBuddy
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.method.ParameterList
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.scaffold.TypeValidation
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.implementation.bytecode.assign.Assigner
import net.bytebuddy.jar.asm.Type
import net.bytebuddy.pool.TypePool
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.updater.readHooks
import org.zeroturnaround.zip.ZipUtil
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.zip.Deflater

private val ACCESS_PKG = XClient::class.java.`package`.name

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
    val byteBuddy = ByteBuddy().with(TypeValidation.DISABLED)
    val methodNameTransformer = MethodNameTransformer.Suffixing("0")
    hookClassNames(hooks).forEach { cn ->
        val typeDescription = typePool.describe(cn).resolve()
        var typeBuilder = byteBuddy.rebase<Any>(typeDescription, classFileLocator, methodNameTransformer)
        hooks.forEach { ch ->
            val accessorInterface = Class.forName("$ACCESS_PKG.X${ch.`class`}")
            if (cn == ch.name) {
                typeBuilder = typeBuilder.implement(accessorInterface)
                ch.fields.forEach { f ->
                    val fieldDescription = typePool.describe(f.owner).resolve().declaredFields.filter(f.matcher()).only
                    val fieldAccess = EncodedFieldAccess.of(fieldDescription, f.decoderNarrowed)
                    typeBuilder = typeBuilder.method(f.getterMatcher()).intercept(fieldAccess.getter)
                    if (!Modifier.isFinal(f.access)) {
                        typeBuilder = typeBuilder.method(f.setterMatcher()).intercept(fieldAccess.setter)
                    }
                }
                ch.methods.forEach { m ->
                    if (m.parameters != null && !Modifier.isAbstract(m.access)) {
                        val invokedMethodDescription = typePool.describe(m.owner).resolve().declaredMethods.filter(m.matcher()).only
                        typeBuilder = typeBuilder.method(m.proxyMatcher()).intercept(
                                MethodCall.invoke(invokedMethodDescription).withAllArgumentsThenDefaultValues().withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
                        )
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
                    val execField = accessorInterface.getDeclaredField(m.method)
                    typeBuilder = typeBuilder.method(m.matcher())
                            .intercept(
                                    Advice.withCustomMapping()
                                            .bind(MethodAdvice.Execution::class.java, execField, Assigner.Typing.DYNAMIC)
                                            .to(MethodAdvice::class.java)
                            )
                }
            }
        }
        typeBuilder.make().saveIn(tempDir.toFile())
    }
    ZipUtil.pack(tempDir.toFile(), destinationJar.toFile(), Deflater.NO_COMPRESSION)
}

// todo
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