package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.INVOKEINTERFACE
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import java.util.LinkedList

class WorldMapArea : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == LinkedList::class.type } == 1 }
            .and { it.instanceFields.count { it.type == String::class.type } == 2 }

    @DependsOn(WorldMapSection::class)
    class readWorldMapSection : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WorldMapSection>() }
    }

    @DependsOn(Coord::class)
    class origin0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Coord>() }
    }

    @MethodParameters()
    @DependsOn(origin0::class, Coord::class)
    class origin : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Coord>() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<origin0>().id } }
    }

    class sections : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == LinkedList::class.type }
    }

    @DependsOn(Packet::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Packet>(), INT_TYPE) }
    }

    @DependsOn(read::class)
    class archiveName0 : OrderMapper.InMethod.Field(read::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @MethodParameters()
    @DependsOn(archiveName0::class)
    class archiveName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<archiveName0>().id } }
    }

    @DependsOn(read::class)
    class name0 : OrderMapper.InMethod.Field(read::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @MethodParameters()
    @DependsOn(name0::class)
    class name : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<name0>().id } }
    }

    class isMain0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    @MethodParameters()
    @DependsOn(isMain0::class)
    class isMain : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<isMain0>().id } }
    }

    @DependsOn(read::class)
    class id0 : OrderMapper.InMethod.Field(read::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(id0::class)
    class id : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<id0>().id } }
    }

    class minX0 : OrderMapper.InConstructor.Field(WorldMapArea::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(minX0::class)
    class minX : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<minX0>().id } }
    }

    class maxX0 : OrderMapper.InConstructor.Field(WorldMapArea::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(maxX0::class)
    class maxX : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<maxX0>().id } }
    }

    class minY0 : OrderMapper.InConstructor.Field(WorldMapArea::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(minY0::class)
    class minY : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<minY0>().id } }
    }

    class maxY0 : OrderMapper.InConstructor.Field(WorldMapArea::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(maxY0::class)
    class maxY : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<maxY0>().id } }
    }

    class zoom0 : OrderMapper.InConstructor.Field(WorldMapArea::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(zoom0::class)
    class zoom : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<zoom0>().id } }
    }

    @MethodParameters()
    @DependsOn(Coord.x::class)
    class originX : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Coord.x>().id } }
    }

    @MethodParameters()
    @DependsOn(Coord.z::class)
    class originY : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Coord.z>().id } }
    }

    @MethodParameters()
    @DependsOn(Coord.y::class)
    class originPlane : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Coord.y>().id } }
    }

    @MethodParameters()
    class setBounds : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
    }

    @MethodParameters("plane", "x", "y")
    class position : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
    }

    @MethodParameters("x", "y")
    @DependsOn(Coord::class)
    class coord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Coord>() }
                .and { it.arguments.size == 2 }
    }

    @MethodParameters("x", "y")
    @DependsOn(WorldMapSection.containsPosition::class)
    class containsPosition : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.opcode == INVOKEINTERFACE && it.methodId == method<WorldMapSection.containsPosition>().id } }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(WorldMapSection.containsCoord::class)
    class containsCoord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.opcode == INVOKEINTERFACE && it.methodId == method<WorldMapSection.containsCoord>().id } }
    }
}