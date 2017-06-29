package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XGrandExchangeOffer extends Accessor {
    @NotNull
    MethodExecution state = new MethodExecution();

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
     *  field
     */
    byte getFlags();

    /**
     *  field
     */
    void setFlags(byte value);

    /**
     * public field
     */
    int getId();

    /**
     * public field
     */
    void setId(int value);

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
    int state();

    /**
     * public method
     */
    int type();
}
