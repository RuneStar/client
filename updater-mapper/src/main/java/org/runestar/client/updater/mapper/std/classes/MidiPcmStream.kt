package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(PcmStream::class)
class MidiPcmStream : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<PcmStream>() }
            .and { it.instanceFields.size == 27 }

    @DependsOn(NodeHashTable::class)
    class musicPatches : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeHashTable>() }
    }

    @MethodParameters()
    @DependsOn(Node.remove::class)
    class removeAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodMark == method<Node.remove>().mark } }
    }

    @MethodParameters()
    @DependsOn(MusicPatch.clear::class)
    class clearAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<MusicPatch.clear>().id } }
    }

    @DependsOn(MidiFileReader::class)
    class midiFile : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MidiFileReader>() }
    }

    @DependsOn(MusicPatchPcmStream::class)
    class patchStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MusicPatchPcmStream>() }
    }

    @MethodParameters("musicTrack", "i", "s", "frequency")
    @DependsOn(MusicTrack::class, AbstractArchive::class)
    class loadMusicTrack : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<MusicTrack>(), type<AbstractArchive>()) }
    }

    @MethodParameters("musicTrack", "b")
    @DependsOn(MusicTrack::class)
    class setMusicTrack : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments == listOf(type<MusicTrack>(), BOOLEAN_TYPE) }
    }

    @DependsOn(setMusicTrack::class)
    class track : OrderMapper.InMethod.Field(setMusicTrack::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(setMusicTrack::class)
    class trackLength : OrderMapper.InMethod.Field(setMusicTrack::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(MidiFileReader.clear::class)
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.count() < 15 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<MidiFileReader.clear>().id } }
    }

    @MethodParameters()
    class isReady : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == BOOLEAN_TYPE }
    }
}