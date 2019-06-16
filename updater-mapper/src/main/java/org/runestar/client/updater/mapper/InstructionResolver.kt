package org.runestar.client.updater.mapper

interface InstructionResolver<T> {

    fun resolve(instruction: Instruction2): T

    interface Class : InstructionResolver<Class2> {
        override fun resolve(instruction: Instruction2): Class2 {
            return instruction.jar[instruction.classId]
        }
    }

    interface Field : InstructionResolver<Field2> {
        override fun resolve(instruction: Instruction2): Field2 {
            return instruction.jar[instruction.fieldId]
        }
    }

    interface Method : InstructionResolver<Method2> {
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[instruction.methodId]
        }
    }
}