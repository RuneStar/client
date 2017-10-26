package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.StaticUniqueMapper
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
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