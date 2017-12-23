package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.StaticUniqueMapper
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.nextWithin
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Type

@SinceVersion(141)
class BoundingBox3DDrawMode : StaticUniqueMapper.Class() {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "aabb" }
            .nextWithin(10) { it.opcode == GETSTATIC && it.fieldType.sort == Type.OBJECT }

    override fun resolve(instruction: Instruction2): Class2 {
        return instruction.jar[instruction.fieldId].klass
    }
}