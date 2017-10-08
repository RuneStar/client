package com.runesuite.client.inject;

import com.runesuite.client.game.raw.*;
import net.bytebuddy.asm.*;
import net.bytebuddy.implementation.bytecode.assign.*;

import java.lang.annotation.*;

public interface MethodAdvice {

    @Retention(RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target(ElementType.PARAMETER)
    @interface Execution { }

    @Advice.OnMethodEnter
    static void onMethodEnter(
            @Execution Accessor.MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments Object[] arguments
    ) throws Throwable {
        exec.enter.accept(new MethodEvent.Enter(instance, arguments));
    }

    @Advice.OnMethodExit
    static void onMethodExit(
            @Execution Accessor.MethodExecution exec,
            @Advice.This(optional = true) Object instance,
            @Advice.AllArguments Object[] arguments,
            @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returned
    ) throws Throwable {
        exec.exit.accept(new MethodEvent.Exit(instance, arguments, returned));
    }
}
