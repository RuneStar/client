package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

class WorldMapCacheName : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == LDC && it.ldcCst == "compositetexture" } }

    class name : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}