package org.runestar.client.patch;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.runestar.client.raw.base.MethodEvent;
import org.runestar.client.raw.base.MethodExecution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

interface MethodAdvice {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Execution { }

    @SuppressWarnings({"unchecked", "ParameterCanBeLocal", "RedundantThrows"})
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onMethodEnter(
            @Execution MethodExecution.Implementation getExec,
            @Advice.Local("exec") MethodExecution.Implementation exec,
            @Advice.Local("event") MethodEvent event,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] arguments
    ) throws Throwable {
        exec = getExec;
        if (!exec.hasObservers()) return false;
        event = new MethodEvent(instance, arguments);
        exec._enter.accept(event);
        //noinspection UnusedAssignment
        arguments = event.arguments;
        return event.skipBody;
    }

    @SuppressWarnings({"unchecked", "RedundantThrows"})
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onMethodExit(
            @Advice.Local("exec") MethodExecution.Implementation exec,
            @Advice.Local("event") MethodEvent event,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Thrown(readOnly = false, typing = Assigner.Typing.DYNAMIC) Throwable thrown,
            @Advice.Enter boolean skipped
    ) throws Throwable {
        if (event == null) return;
        if (!skipped) {
            event.returned = returned;
            event.thrown = thrown;
        }
        exec._exit.accept(event);
        //noinspection UnusedAssignment
        returned = event.returned;
        //noinspection UnusedAssignment
        thrown = event.thrown;
    }
}