package org.runestar.client.game.inject;

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
            @Advice.AllArguments Object[] arguments
    ) throws Throwable {
        MethodExecution.Implementation execImpl = (MethodExecution.Implementation) exec;
        if (execImpl.getEnter().hasObservers() || execImpl.getExit().hasObservers()) {
            MethodEvent.Implementation event = new MethodEvent.Implementation(execImpl.counter.getAndIncrement(), instance, arguments);
            execImpl.getEnter().accept(event);
            return event;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodExit
    static void onMethodExit(
            @Execution MethodExecution exec,
            @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter MethodEvent.Implementation event
    ) throws Throwable {
        if (event != null) {
            event.returned = returned;
            ((MethodExecution.Implementation) exec).getExit().accept(event);
        }
    }
}