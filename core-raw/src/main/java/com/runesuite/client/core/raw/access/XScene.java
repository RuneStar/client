package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XScene extends Accessor {
    @NotNull
    MethodExecution getBoundaryObject = new MethodExecution();

    @NotNull
    MethodExecution getFloorDecoration = new MethodExecution();

    @NotNull
    MethodExecution getWallDecoration = new MethodExecution();

    @NotNull
    MethodExecution removeGroundItemPile = new MethodExecution();

    @NotNull
    MethodExecution setGroundItemPile = new MethodExecution();

    /**
     *  field
     */
    XGameObject[] getGameObjects();

    /**
     *  field
     */
    void setGameObjects(XGameObject[] value);

    /**
     *  field
     */
    XTile[][][] getTiles();

    /**
     *  field
     */
    void setTiles(XTile[][][] value);

    /**
     * public method
     */
    XBoundaryObject getBoundaryObject(int plane, int x, int y);

    /**
     * public method
     */
    XFloorDecoration getFloorDecoration(int plane, int x, int y);

    /**
     * public method
     */
    XWallDecoration getWallDecoration(int plane, int x, int y);

    /**
     * public method
     */
    void removeGroundItemPile(int plane, int x, int y);

    /**
     * public method
     */
    void setGroundItemPile(int plane, int x, int y, int int0, XEntity bottom, int int1,
                           XEntity middle, XEntity top);
}
