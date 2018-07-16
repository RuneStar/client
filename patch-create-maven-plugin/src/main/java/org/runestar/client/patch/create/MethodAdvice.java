package org.runestar.client.patch.create;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.runestar.client.game.raw.base.MethodEvent;
import org.runestar.client.game.raw.base.MethodExecution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

interface MethodAdvice {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Execution { }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodEnter(skipOn = Object[].class)
    static Object onMethodEnter(
            @Execution MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] arguments
    ) throws Throwable {
        MethodExecution.Implementation execImpl = (MethodExecution.Implementation) exec;
        if (!execImpl.hasObservers()) return null;
        MethodEvent event = new MethodEvent(instance, arguments);
        execImpl._enter.accept(event);
        //noinspection UnusedAssignment
        arguments = event.arguments;
        return MethodEvent.toSkippable(event);
    }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onMethodExit(
            @Execution MethodExecution exec,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Thrown(readOnly = false, typing = Assigner.Typing.DYNAMIC) Throwable thrown,
            @Advice.Enter Object enter
    ) throws Throwable {
        if (enter == null) return;
        MethodEvent event = MethodEvent.fromSkippable(enter, returned, thrown);
        ((MethodExecution.Implementation) exec)._exit.accept(event);
        //noinspection UnusedAssignment
        returned = event.returned;
        //noinspection UnusedAssignment
        thrown = event.thrown;
    }
}