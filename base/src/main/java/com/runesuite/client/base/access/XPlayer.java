package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

public interface XPlayer extends Accessor, XActor {
    @Field
    String[] getActions();

    @Field
    void setActions(String[] value);

    @Field
    XPlayerAppearance getAppearance();

    @Field
    void setAppearance(XPlayerAppearance value);

    @Field
    int getCombatLevel();

    @Field
    void setCombatLevel(int value);

    @Field
    XModel getModel0();

    @Field
    void setModel0(XModel value);

    @Field
    String getName();

    @Field
    void setName(String value);

    @Field
    int getPrayerIcon();

    @Field
    void setPrayerIcon(int value);

    @Field
    int getSkullIcon();

    @Field
    void setSkullIcon(int value);

    @Field
    int getTeam();

    @Field
    void setTeam(int value);

    @Method
    XModel getModel();

    @Method
    boolean isVisible();

    final class getModel {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private getModel() {
        }
    }

    final class isVisible {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private isVisible() {
        }
    }
}
