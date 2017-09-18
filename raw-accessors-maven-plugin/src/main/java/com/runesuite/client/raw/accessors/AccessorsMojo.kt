package com.runesuite.client.raw.accessors

import com.runesuite.client.updater.HOOKS
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.jetbrains.annotations.NotNull
import org.objectweb.asm.Type
import java.nio.file.Paths
import javax.lang.model.element.Modifier
import java.lang.reflect.Modifier as RModifier

@Mojo(name = "accessors")
class AccessorsMojo : AbstractMojo() {

    @Parameter(property = "outputPackage", required = true)
    lateinit var outputPackage: String

    @Parameter(property = "accessorClassName", required = true)
    lateinit var accessorClassName: String

    @Parameter(property = "methodClassName", required = true)
    lateinit var methodClassName: String

    @Parameter(property = "methodInitializer", required = true)
    lateinit var methodInitializer: String

    @Parameter(defaultValue = "\${project}")
    lateinit var project: MavenProject

    private val INDENT = "    "

    private val SETTER_PARAM_NAME = "value"

    private val ACCESSOR_NAME: ClassName by lazy { ClassName.bestGuess(accessorClassName) }

    private val METHOD_NAME: ClassName by lazy { ClassName.bestGuess(methodClassName) }

    private val OUTPUT_DIR by lazy { Paths.get(project.build.directory, "generated-sources") }

    private val TYPE_TRANSFORMS by lazy { HOOKS.associate { it.name to "X" + it.`class` } }

    override fun execute() {
        HOOKS.forEach { c ->
            val typeBuilder = TypeSpec.interfaceBuilder("X" + c.`class`)
                    .addSuperinterface(ACCESSOR_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc("\$L", classModifiersToString(c.access) + "\n")
            if (c.`super` in TYPE_TRANSFORMS) {
                typeBuilder.addSuperinterface(ClassName.get(outputPackage, TYPE_TRANSFORMS.getValue(c.`super`!!)))
            }
            c.interfaces?.forEach { i ->
                typeBuilder.addSuperinterface(poetType(Type.getObjectType(i).descriptor))
            }
            c.fields?.forEach { f ->
                val fName = poetType(f.descriptor)
                typeBuilder.addMethod(MethodSpec.methodBuilder("get${f.field.capitalize()}")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addJavadoc("\$L", fieldModifiersToString(f.access) + "\n")
                        .returns(fName).build())
                if (!RModifier.isFinal(f.access)) {
                    typeBuilder.addMethod(MethodSpec.methodBuilder("set${f.field.capitalize()}")
                            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .addParameter(fName, SETTER_PARAM_NAME)
                            .addJavadoc("\$L", fieldModifiersToString(f.access) + "\n").build())
                }
            }
            c.methods?.forEach { m ->
                if (m.parameters != null) {
                    val mType = Type.getMethodType(m.descriptor)
                    val method = MethodSpec.methodBuilder(m.method)
                            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .addJavadoc("\$L", methodModifiersToString(m.access) + "\n")
                            .returns(poetType(mType.returnType.descriptor))
                    mType.argumentTypes.take(m.parameters!!.size).forEachIndexed { i, arg ->
                        method.addParameter(poetType(arg.descriptor), m.parameters!![i])
                    }
                    typeBuilder.addMethod(method.build())
                }
                if (m.parameters != null && !RModifier.isInterface(c.access) && !RModifier.isAbstract(m.access)) {
                    typeBuilder.addField(
                            FieldSpec.builder(METHOD_NAME, m.method, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .initializer(methodInitializer)
                                    .addAnnotation(NotNull::class.java)
                                    .build()
                    )
                }
            }
            JavaFile.builder(outputPackage, typeBuilder.build()).indent(INDENT)
                    .build().writeTo(OUTPUT_DIR)
        }
        project.addCompileSourceRoot(OUTPUT_DIR.toString())
    }

    private fun poetType(desc: String): TypeName {
        val type = Type.getType(desc)
        val baseType = if (type.sort == Type.ARRAY) type.elementType else type
        var baseName: TypeName
        try {
            baseName = TypeName.get(ClassUtils.getClass(baseType.className))
        } catch (e: ClassNotFoundException) {
            baseName = ClassName.bestGuess(TYPE_TRANSFORMS[baseType.className]) ?: ClassName.OBJECT
        }
        var name = baseName
        if (type.sort == Type.ARRAY) {
            repeat(type.dimensions) {
                name = ArrayTypeName.of(name)
            }
        }
        return name
    }

    private fun classModifiersToString(modifiers: Int): String {
        return if (RModifier.isInterface(modifiers)) {
            RModifier.toString(RModifier.interfaceModifiers() and modifiers) + " interface"
        } else {
            RModifier.toString(RModifier.classModifiers() and modifiers) + " class"
        }
    }

    private fun fieldModifiersToString(modifiers: Int): String {
        return RModifier.toString(RModifier.fieldModifiers() and modifiers) + " field"
    }

    private fun methodModifiersToString(modifiers: Int): String {
        return RModifier.toString(RModifier.methodModifiers() and modifiers) + " method"
    }
}