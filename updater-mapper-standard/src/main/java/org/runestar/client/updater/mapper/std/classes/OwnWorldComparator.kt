package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.objectweb.asm.Opcodes

@DependsOn(Client.worldId::class)
class OwnWorldComparator : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Comparator::class.type) }
            .and { it.instanceMethods.flatMap { it.instructions.asIterable() }
                    .any { it.opcode == Opcodes.GETSTATIC && it.fieldId == field<Client.worldId>().id } }
}