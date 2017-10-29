package com.runesuite.client.game.raw.accessors

import com.hunterwb.kxtra.lang.classloader.loadClassFromDescriptor
import com.runesuite.client.updater.HOOKS
import com.runesuite.client.updater.common.ClassHook
import com.runesuite.client.updater.common.MethodHook
import com.squareup.javapoet.*
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.jetbrains.annotations.NotNull
import org.objectweb.asm.Type
import java.nio.file.Paths
import javax.lang.model.element.Modifier
import kotlin.reflect.jvm.jvmName
import java.lang.reflect.Modifier as RModifier

@Mojo(name = "accessors")
class AccessorsMojo : AbstractMojo() {

    @Parameter(property = "outputPackage", required = true)
    lateinit var outputPackage: String

    @Parameter(property = "accessorClass", required = true)
    lateinit var accessorClass: String

    @Parameter(property = "callbackFieldClass", required = true)
    lateinit var callbackFieldClass: String

    @Parameter(property = "callbackFieldInitializer", required = true)
    lateinit var callbackFieldInitializer: String

    @Parameter(defaultValue = "\${project}")
    lateinit var project: MavenProject

    private val INDENT = "    "

    private val SETTER_PARAM_NAME = "value"

    private val VOID_OBJECT_TYPENAME = TypeName.get(Void::class.java)

    private val ACCESSOR_TYPENAME: ClassName by lazy { ClassName.bestGuess(accessorClass) }

    private val CALLBACK_FIELD_TYPENAME: ClassName by lazy { ClassName.bestGuess(callbackFieldClass) }

    private val OUTPUT_DIR by lazy { Paths.get(project.build.directory, "generated-sources") }

    private val TYPE_TRANSFORMS by lazy { HOOKS.associate { it.name to "X" + it.`class` } }

    override fun execute() {
        HOOKS.forEach { c ->
            val typeBuilder = TypeSpec.interfaceBuilder("X" + c.`class`)
                    .addSuperinterface(ACCESSOR_TYPENAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc(classModifiersToString(c.access))
            if (c.`super` in TYPE_TRANSFORMS) {
                typeBuilder.addSuperinterface(ClassName.get(outputPackage, TYPE_TRANSFORMS.getValue(c.`super`)))
            } else if (c.`super` != Any::class.jvmName) {
                typeBuilder.addJavadoc(" extends {@link ${c.`super`}}")
            }
            c.interfaces.forEach { i ->
                typeBuilder.addSuperinterface(poetType(Type.getObjectType(i).descriptor))
            }
            c.fields.forEach { f ->
                val fieldTypeName = poetType(f.descriptor)
                typeBuilder.addMethod(MethodSpec.methodBuilder(f.getterName)
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addJavadoc(fieldModifiersToString(f.access))
                        .returns(fieldTypeName)
                        .build())
                if (!RModifier.isFinal(f.access)) {
                    typeBuilder.addMethod(MethodSpec.methodBuilder(f.setterName)
                            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .addParameter(fieldTypeName, SETTER_PARAM_NAME)
                            .addJavadoc(fieldModifiersToString(f.access))
                            .build())
                }
            }
            c.methods.filter { it.parameters != null }.forEach { m ->
                typeBuilder.addMethod(MethodSpec.methodBuilder(m.method)
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addJavadoc(methodModifiersToString(m.access))
                        .returns(poetType(m.type.returnType.descriptor))
                        .addParameters(m.actualParameters.map {
                            ParameterSpec.builder(poetType(it.descriptor), it.name).build() })
                        .build()
                )
                if (!RModifier.isInterface(c.access) && !RModifier.isAbstract(m.access)) {
                    val instanceType = if (RModifier.isStatic(m.access)) VOID_OBJECT_TYPENAME else poetType(c.descriptor)
                    val returnType = poetType(m.type.returnType.descriptor, true)
                    val callbackType = ParameterizedTypeName.get(CALLBACK_FIELD_TYPENAME, instanceType, returnType)
                    typeBuilder.addField(FieldSpec.builder(callbackType, m.method)
                            .initializer(callbackFieldInitializer)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .addJavadoc(seeMethodTag(c, m))
                            .build()
                    )
                }
            }
            JavaFile.builder(outputPackage, typeBuilder.build())
                    .indent(INDENT)
                    .build()
                    .writeTo(OUTPUT_DIR)
        }
        project.addCompileSourceRoot(OUTPUT_DIR.toString())
    }

    private fun poetType(descriptor: String, wrapPrimitives: Boolean = false): TypeName {
        val type = Type.getType(descriptor)
        val baseType = type.baseType
        val baseName: TypeName = if (TYPE_TRANSFORMS.containsKey(baseType.className)) {
            ClassName.bestGuess(TYPE_TRANSFORMS[baseType.className])
        } else {
            var klass = ClassLoader.getSystemClassLoader().loadClassFromDescriptor(baseType.descriptor)
            if (wrapPrimitives && klass.isPrimitive) {
                klass = if (klass == Void.TYPE) Void::class.java else ClassUtils.primitiveToWrapper(klass)
            }
            TypeName.get(klass)
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
            if (baseType.className in TYPE_TRANSFORMS) {
                TYPE_TRANSFORMS.getValue(baseType.className) + ("[]".repeat(type.arrayDimensions))
            } else {
                ClassLoader.getSystemClassLoader().loadClassFromDescriptor(type.descriptor).simpleName
            }
        }
        return "@see X${classHook.`class`}#${methodHook.method}(${argList.joinToString()})"
    }
}