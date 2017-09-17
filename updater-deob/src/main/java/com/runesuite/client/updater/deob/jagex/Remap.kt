package com.runesuite.client.updater.deob.jagex

import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper

object Remap : Remapper() {

    override fun map(typeName: String): String {
        val isJdk = typeName.length > 2 && typeName != "client"
        if (isJdk) return typeName
        return typeName.capitalize()
    }

    override fun mapMethodName(owner: String, name: String, desc: String): String {
        val isJdk = name.length > 2
        if (isJdk) return name
        val type = Type.getMethodType(desc).returnType
        val klass = type.className.split('.').last().replace("[]", "").capitalize()
        val dim = type.descriptor.count { it == '[' }.takeIf { it > 0 } ?: ""
        return "$name$klass$dim"
    }

    override fun mapFieldName(owner: String, name: String, desc: String): String {
        val isJdk = name.length > 2
        if (isJdk) return name
        val klass = Type.getType(desc).className.split('.').last().replace("[]", "").capitalize()
        val dim = desc.count { it == '[' }.takeIf { it > 0 } ?: ""
        return "$name$klass$dim"
    }
}