package com.runesuite.client.inject;

import com.runesuite.client.game.raw.MethodEvent;
import com.runesuite.client.game.raw.MethodExecution;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface MethodAdvice {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Execution { }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodEnter
    static MethodEvent.Enter onMethodEnter(
            @Execution MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments Object[] arguments
    ) throws Throwable {
        MethodExecution.Implementation execImpl = (MethodExecution.Implementation) exec;
        MethodEvent.Enter enterEvent = new MethodEvent.Enter(execImpl.counter.getAndIncrement(), instance, arguments);
        execImpl.getEnter().accept(enterEvent);
        return enterEvent;
    }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodExit
    static void onMethodExit(
            @Execution MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter MethodEvent.Enter enterEvent
    ) throws Throwable {
        ((MethodExecution.Implementation) exec).getExit().accept(new MethodEvent.Exit(enterEvent.getIndex(), instance, enterEvent.getArguments(), returned));
    }
}