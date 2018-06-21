package org.runestar.client.patch.create;

import org.runestar.client.game.raw.MethodEvent;
import org.runestar.client.game.raw.MethodExecution;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

interface MethodAdvice {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Execution { }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodEnter
    static MethodEvent.Implementation onMethodEnter(
            @Execution MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] arguments
    ) throws Throwable {
        MethodExecution.Implementation execImpl = (MethodExecution.Implementation) exec;
        if (execImpl.hasObservers()) {
            MethodEvent.Implementation event = new MethodEvent.Implementation(instance, arguments);
            execImpl._enter.accept(event);
            //noinspection UnusedAssignment
            arguments = event.arguments;
            return event;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodExit
    static void onMethodExit(
            @Execution MethodExecution exec,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter MethodEvent.Implementation event
    ) throws Throwable {
        if (event != null) {
            event.returned = returned;
            ((MethodExecution.Implementation) exec)._exit.accept(event);
            //noinspection UnusedAssignment
            returned = event.returned;
        }
    }
}