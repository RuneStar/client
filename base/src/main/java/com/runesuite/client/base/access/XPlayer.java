package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XPlayer extends Accessor, XActor {
    /**
     *  field
     */
    String[] getActions();

    /**
     *  field
     */
    void setActions(String[] value);

    /**
     *  field
     */
    XPlayerAppearance getAppearance();

    /**
     *  field
     */
    void setAppearance(XPlayerAppearance value);

    /**
     *  field
     */
    int getCombatLevel();

    /**
     *  field
     */
    void setCombatLevel(int value);

    /**
     *  field
     */
    XModel getModel0();

    /**
     *  field
     */
    void setModel0(XModel value);

    /**
     *  field
     */
    String getName();

    /**
     *  field
     */
    void setName(String value);

    /**
     *  field
     */
    int getPrayerIcon();

    /**
     *  field
     */
    void setPrayerIcon(int value);

    /**
     *  field
     */
    int getSkullIcon();

    /**
     *  field
     */
    void setSkullIcon(int value);

    /**
     *  field
     */
    int getTeam();

    /**
     *  field
     */
    void setTeam(int value);

    /**
     * protected final method
     */
    XModel getModel();

    /**
     * final method
     */
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
