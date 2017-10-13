package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import java.applet.Applet
import java.awt.Canvas
import java.awt.EventQueue
import java.awt.Frame
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

    @MethodParameters
    @SinceVersion(141)
    @DependsOn(canvas::class)
    class replaceCanvas : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<canvas>().id } }
    }

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

    class checkHost : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "runescape.com" } }
    }
}