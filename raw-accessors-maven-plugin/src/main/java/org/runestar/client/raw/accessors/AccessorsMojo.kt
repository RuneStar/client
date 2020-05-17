package org.runestar.client.raw.accessors

import com.squareup.javapoet.*
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.jetbrains.annotations.NotNull
import org.objectweb.asm.Type
import org.runestar.client.raw.base.Accessor
import org.runestar.client.raw.base.MethodExecution
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.MethodHook
import org.runestar.client.updater.readHooks
import java.nio.file.Paths
import javax.lang.model.element.Modifier
import kotlin.reflect.jvm.jvmName
import java.lang.reflect.Modifier as RModifier

@Mojo(
        name = "accessors",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
class AccessorsMojo : AbstractMojo() {

    private companion object {
        const val INDENT = "\t"
        const val SETTER_PARAM_NAME = "value"
        val VOID_OBJECT_TYPENAME: TypeName = TypeName.get(Void::class.java)
        const val CONSTRUCTOR_ARG_PREFIX = "arg"
        const val OUTPUT_PKG = "org.runestar.client.raw.access"
        const val CALLBACK_FIELD_INIT = "new MethodExecution.Implementation()"
        private val ACCESSOR_TYPENAME: ClassName = ClassName.get(Accessor::class.java)
        private val METHOD_EXECUTION_TYPENAME: ClassName = ClassName.get(MethodExecution::class.java)
    }

    @Parameter(defaultValue = "\${project}")
    private lateinit var project: MavenProject

    private val hooks = readHooks()

    private val typeTransforms = hooks.associate { it.name to "X" + it.`class` }

    override fun execute() {
        val outputDir = Paths.get(project.build.directory, "generated-sources")
        hooks.forEach { c ->
            val typeBuilder = TypeSpec.interfaceBuilder(typeTransforms[c.name])
                    .addSuperinterface(ACCESSOR_TYPENAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc(classModifiersToString(c.access))
            if (c.`super` in typeTransforms) {
                typeBuilder.addSuperinterface(ClassName.get(OUTPUT_PKG, typeTransforms[c.`super`]))
            } else if (c.`super` != Any::class.jvmName) {
                typeBuilder.addJavadoc(" extends {@link ${c.`super`}}")
            }
            c.interfaceDescriptors.forEach { desc ->
                typeBuilder.addSuperinterface(poetType(desc))
            }
            c.fields.forEach { f ->
                val fieldTypeName = poetType(f.descriptor)
                val fieldModifiersString = fieldModifiersToString(f.access)
                typeBuilder.addMethod(
                        MethodSpec.methodBuilder(f.getterMethod)
                                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                .addJavadoc(fieldModifiersString)
                                .returns(fieldTypeName)
                                .build()
                )
                if (!RModifier.isFinal(f.access)) {
                    typeBuilder.addMethod(
                            MethodSpec.methodBuilder(f.setterMethod)
                                    .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                    .addParameter(fieldTypeName, SETTER_PARAM_NAME)
                                    .addJavadoc(fieldModifiersString)
                                    .build()
                    )
                }
            }
            c.methods.filter { it.parameters != null }.forEach { m ->
                val returnDesc = m.returnDescriptor
                typeBuilder.addMethod(
                        MethodSpec.methodBuilder(m.method)
                                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                .addJavadoc(methodModifiersToString(m.access))
                                .returns(poetType(returnDesc))
                                .addParameters(
                                        m.actualParameters.map {
                                            ParameterSpec.builder(poetType(it.descriptor), it.name).build()
                                        }
                                )
                                .build()
                )
                if (!RModifier.isInterface(c.access) && !RModifier.isAbstract(m.access)) {
                    val instanceType = if (RModifier.isStatic(m.access)) VOID_OBJECT_TYPENAME else poetType(c.descriptor)
                    val returnType = poetType(returnDesc, true)
                    val callbackType = ParameterizedTypeName.get(METHOD_EXECUTION_TYPENAME, instanceType, returnType)
                    typeBuilder.addField(
                            FieldSpec.builder(callbackType, m.method)
                                    .initializer(CALLBACK_FIELD_INIT)
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .addAnnotation(NotNull::class.java)
                                    .addJavadoc(seeMethodTag(c, m))
                                    .build()
                    )
                }
            }
            if (c.name == "client") {
                hooks.filter { !RModifier.isAbstract(it.access) }.forEach { hc ->
                    hc.constructors.forEach { hcon ->
                        typeBuilder.addMethod(
                                MethodSpec.methodBuilder(hc.constructorName)
                                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                        .addJavadoc(constructorModifiersToString(hcon.access))
                                        .returns(poetType(hc.descriptor))
                                        .addParameters(
                                                hcon.argumentDescriptors.mapIndexed { i, desc ->
                                                    ParameterSpec.builder(poetType(desc), "$CONSTRUCTOR_ARG_PREFIX$i").build()
                                                }
                                        )
                                        .build()
                        )
                    }
                }
            }
            JavaFile.builder(OUTPUT_PKG, typeBuilder.build())
                    .indent(INDENT)
                    .build()
                    .writeTo(outputDir)
        }
        project.addCompileSourceRoot(outputDir.toString())
    }

    private fun poetType(descriptor: String, wrapPrimitives: Boolean = false): TypeName {
        val type = Type.getType(descriptor)
        val baseType = type.baseType
        val baseName: TypeName = if (typeTransforms.containsKey(baseType.className)) {
            ClassName.get(OUTPUT_PKG, typeTransforms[baseType.className])
        } else {
            val klass = ClassLoader.getSystemClassLoader().loadClassFromDescriptor(baseType.descriptor)
            val klassName = TypeName.get(klass)
            if (type.arrayDimensions == 0 && wrapPrimitives) {
                klassName.box()
            } else {
                klassName
            }
        }
        var name = baseName
        repeat(type.arrayDimensions) {
            name = ArrayTypeName.of(name)
        }
        return name
    }

    private fun seeMethodTag(classHook: ClassHook, methodHook: MethodHook): String {
        val argList = methodHook.actualParameters.map {
            val type = Type.getType(it.descriptor)
            val baseType = type.baseType
            if (baseType.className in typeTransforms) {
                typeTransforms.getValue(baseType.className) + ("[]".repeat(type.arrayDimensions))
            } else {
                ClassLoader.getSystemClassLoader().loadClassFromDescriptor(type.descriptor).simpleName
            }
        }
        return "@see X${classHook.`class`}#${methodHook.method}(${argList.joinToString()})"
    }
}