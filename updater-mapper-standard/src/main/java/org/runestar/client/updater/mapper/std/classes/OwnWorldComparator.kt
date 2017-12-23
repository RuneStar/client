package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.objectweb.asm.Opcodes

@DependsOn(Client.worldId::class)
class OwnWorldComparator : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Comparator::class.type) }
            .and { it.instanceMethods.flatMap { it.instructions.asIterable() }
                    .any { it.opcode == Opcodes.GETSTATIC && it.fieldId == field<Client.worldId>().id } }
}