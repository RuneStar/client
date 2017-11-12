package com.runesuite.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import java.applet.Applet
import java.awt.*
import java.awt.Canvas
import java.awt.datatransfer.Clipboard

@DependsOn(Client::class)
class GameShell : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<Client>().superType == it.type }

    @SinceVersion(141)
    class canvas : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Canvas::class.type }
    }

    @SinceVersion(141)
    class frame : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Frame::class.type }
    }

    @SinceVersion(141)
    class eventQueue : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == EventQueue::class.type }
    }

    @SinceVersion(141)
    class clipboard : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Clipboard::class.type }
    }

    @SinceVersion(141)
    @DependsOn(MouseWheelHandler::class)
    class mouseWheelHandler : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseWheelHandler>() }
    }

    @MethodParameters("g")
    @SinceVersion(141)
    class paint : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Applet::paint.mark }
    }

    @MethodParameters("g")
    @SinceVersion(141)
    class update : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Applet::update.mark }
    }

    @MethodParameters()
    @SinceVersion(141)
    @DependsOn(canvas::class)
    class replaceCanvas : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<canvas>().id } }
    }

    @MethodParameters("s")
    @SinceVersion(141)
    class clipboardSetString : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodName == "setContents" } }
    }

    @SinceVersion(141)
    @MethodParameters()
    @DependsOn(MouseWheel::class)
    class mouseWheel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<MouseWheel>() }
    }

    @MethodParameters()
    class checkHost : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "runescape.com" } }
    }

    @MethodParameters()
    class container : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Container::class.type }
    }

    @MethodParameters("type")
    class error : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "error_game_" } }
    }

    @DependsOn(error::class)
    class hasErrored : UniqueMapper.InMethod.Field(error::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE }
    }

//    class setUpKeyboard : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 0..1 }
//                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "microsoft" } }
//    }

    @MethodParameters()
    @SinceVersion(141)
    class setUpClipboard : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == Toolkit::getSystemClipboard.id } }
    }

    @MethodParameters()
    @DependsOn(Client.setUp::class)
    class setUp : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Client.setUp>().mark }
    }

    @MethodParameters()
    @SinceVersion(141)
    @DependsOn(Client.setUp::class)
    class setUpKeyboard : OrderMapper.InMethod.Method(Client.setUp::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }

    @MethodParameters()
    @SinceVersion(141)
    @DependsOn(Client.setUp::class)
    class setUpMouse : OrderMapper.InMethod.Method(Client.setUp::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }
}