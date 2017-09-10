package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XGrandExchangeOffer extends Accessor {
    @NotNull
    MethodExecution status = new MethodExecution();

    @NotNull
    MethodExecution type = new MethodExecution();

    /**
     * public field
     */
    int getCurrentPrice();

    /**
     * public field
     */
    void setCurrentPrice(int value);

    /**
     * public field
     */
    int getCurrentQuantity();

    /**
     * public field
     */
    void setCurrentQuantity(int value);

    /**
     * public field
     */
    int getId();

    /**
     * public field
     */
    void setId(int value);

    /**
     *  field
     */
    byte getState();

    /**
     *  field
     */
    void setState(byte value);

    /**
     * public field
     */
    int getTotalQuantity();

    /**
     * public field
     */
    void setTotalQuantity(int value);

    /**
     * public field
     */
    int getUnitPrice();

    /**
     * public field
     */
    void setUnitPrice(int value);

    /**
     * public method
     */
    int status();

    /**
     * public method
     */
    int type();
}
