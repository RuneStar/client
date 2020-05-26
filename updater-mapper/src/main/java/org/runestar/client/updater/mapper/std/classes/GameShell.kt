package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.id
import org.runestar.client.updater.mapper.mark
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.updater.mapper.Predicate
import org.runestar.client.updater.mapper.type
import java.applet.Applet
import java.awt.*
import java.awt.Canvas
import java.awt.datatransfer.Clipboard
import java.lang.reflect.Modifier

@DependsOn(Client::class)
class GameShell : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<Client>().superType == it.type }

    class canvas : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Canvas::class.type }
    }

    class frame : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Frame::class.type }
    }

    class eventQueue : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == EventQueue::class.type }
    }

    class clipboard : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Clipboard::class.type }
    }

    @DependsOn(MouseWheelHandler::class)
    class mouseWheelHandler : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseWheelHandler>() }
    }

    @MethodParameters("g")
    class paint : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Applet::paint.mark }
    }

    @MethodParameters("g")
    class update : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Applet::update.mark }
    }

    @MethodParameters()
    @DependsOn(canvas::class)
    class addCanvas : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<canvas>().id } }
    }

    @MethodParameters("s")
    class clipboardSetString : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodName == "setContents" } }
    }

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

    @MethodParameters()
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
    @DependsOn(Client.setUp::class)
    class setUpKeyboard : OrderMapper.InMethod.Method(Client.setUp::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }

    @MethodParameters()
    @DependsOn(Client.setUp::class)
    class setUpMouse : OrderMapper.InMethod.Method(Client.setUp::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }

    @MethodParameters()
    @DependsOn(Bounds::class)
    class getFrameContentBounds : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Bounds>() }
    }

    class focusGained : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "focusGained" }
    }

    @MethodParameters()
    @DependsOn(frame::class)
    class hasFrame : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<frame>().id } }
    }

    @DependsOn(getFrameContentBounds::class)
    class contentWidth0 : OrderMapper.InMethod.Field(getFrameContentBounds::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(getFrameContentBounds::class)
    class contentHeight0 : OrderMapper.InMethod.Field(getFrameContentBounds::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(addCanvas::class)
    class canvasSetTimeMs : OrderMapper.InMethod.Field(addCanvas::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    @DependsOn(addCanvas::class)
    class isCanvasInvalid : OrderMapper.InMethod.Field(addCanvas::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    @DependsOn(addCanvas::class)
    class replaceCanvas : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<addCanvas>().id } }
                .and { it.name != "run" }
    }

    @MethodParameters("width", "height", "revision")
    class startThread : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters()
    class kill : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == INVOKESTATIC && it.methodName == "exit" } }
    }

    @MethodParameters()
    @DependsOn(kill::class)
    class kill0 : OrderMapper.InMethod.Method(kill::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<GameShell>() }
    }

    class run : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "run" }
    }

    @DependsOn(run::class)
    class stopTimeMs : UniqueMapper.InMethod.Field(run::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == LONG_TYPE }
    }

    @MethodParameters()
    class clearBackground : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodId == Graphics::fillRect.id } }
    }

    @DependsOn(clearBackground::class)
    class canvasX : OrderMapper.InMethod.Field(clearBackground::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(clearBackground::class)
    class canvasY : OrderMapper.InMethod.Field(clearBackground::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(clearBackground::class)
    class contentWidth : OrderMapper.InMethod.Field(clearBackground::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(clearBackground::class)
    class contentHeight : OrderMapper.InMethod.Field(clearBackground::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class drawInitial : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, String::class.type, BOOLEAN_TYPE) }
    }

    @MethodParameters("width", "height")
    class setMaxCanvasSize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { Modifier.isProtected(it.access) }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE) }
    }

    @DependsOn(setMaxCanvasSize::class)
    class maxCanvasWidth : OrderMapper.InMethod.Field(setMaxCanvasSize::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(setMaxCanvasSize::class)
    class maxCanvasHeight : OrderMapper.InMethod.Field(setMaxCanvasSize::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(replaceCanvas::class)
    class render : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodMark == method<replaceCanvas>().mark } }
    }

    @DependsOn(Client.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Client.draw>().mark }
    }

    @MethodParameters()
    @DependsOn(render::class)
    class checkResize : OrderMapper.InMethod.Method(render::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodType.argumentTypes.isEmpty() }
    }

    @MethodParameters()
    @DependsOn(checkResize::class)
    class onResize : OrderMapper.InMethod.Method(checkResize::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }
}