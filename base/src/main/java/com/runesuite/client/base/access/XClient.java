package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

import java.lang.management.GarbageCollectorMXBean;

/**
 * public final class
 */
public interface XClient extends Accessor, XGameShell {
    @NotNull
    MethodExecution getNpcDefinition = new MethodExecution();

    @NotNull
    MethodExecution init = new MethodExecution();

    @NotNull
    MethodExecution openMenu = new MethodExecution();

    @NotNull
    MethodExecution updateGameState = new MethodExecution();

    /**
     * static field
     */
    int getBaseX();

    /**
     * static field
     */
    void setBaseX(int value);

    /**
     * static field
     */
    int getBaseY();

    /**
     * static field
     */
    void setBaseY(int value);

    /**
     * static field
     */
    int getCameraPitch();

    /**
     * static field
     */
    void setCameraPitch(int value);

    /**
     * static field
     */
    int getCameraX();

    /**
     * static field
     */
    void setCameraX(int value);

    /**
     * static field
     */
    int getCameraY();

    /**
     * static field
     */
    void setCameraY(int value);

    /**
     * static field
     */
    int getCameraYaw();

    /**
     * static field
     */
    void setCameraYaw(int value);

    /**
     * static field
     */
    int getCameraZ();

    /**
     * static field
     */
    void setCameraZ(int value);

    /**
     * static field
     */
    XClanMate[] getClanChat();

    /**
     * static field
     */
    void setClanChat(XClanMate[] value);

    /**
     * static field
     */
    String getClanChatOwner();

    /**
     * static field
     */
    void setClanChatOwner(String value);

    /**
     * public static field
     */
    XNodeDeque2 getClassInfos();

    /**
     * public static field
     */
    void setClassInfos(XNodeDeque2 value);

    /**
     * static field
     */
    XClientPreferences getClientPreferences();

    /**
     * static field
     */
    void setClientPreferences(XClientPreferences value);

    /**
     * static field
     */
    XCollisionMap[] getCollisionMaps();

    /**
     * static field
     */
    void setCollisionMaps(XCollisionMap[] value);

    /**
     * public static field
     */
    int[] getCosineTable();

    /**
     * public static field
     */
    void setCosineTable(int[] value);

    /**
     * static field
     */
    int[] getCurrentLevels();

    /**
     * static field
     */
    void setCurrentLevels(int[] value);

    /**
     * static field
     */
    int getCursorColor();

    /**
     * static field
     */
    void setCursorColor(int value);

    /**
     * static field
     */
    int getCycle();

    /**
     * static field
     */
    void setCycle(int value);

    /**
     * static field
     */
    boolean getDisplayFps();

    /**
     * static field
     */
    void setDisplayFps(boolean value);

    /**
     * static field
     */
    int[] getExperience();

    /**
     * static field
     */
    void setExperience(int[] value);

    /**
     * protected static field
     */
    int getFps();

    /**
     * protected static field
     */
    void setFps(int value);

    /**
     * static field
     */
    XFriend[] getFriendsList();

    /**
     * static field
     */
    void setFriendsList(XFriend[] value);

    /**
     * static field
     */
    int getGameDrawingMode();

    /**
     * static field
     */
    void setGameDrawingMode(int value);

    /**
     * static field
     */
    XGameShell getGameShell();

    /**
     * static field
     */
    void setGameShell(XGameShell value);

    /**
     * static field
     */
    int getGameState();

    /**
     * static field
     */
    void setGameState(int value);

    /**
     * static field
     */
    GarbageCollectorMXBean getGarbageCollector();

    /**
     * static field
     */
    void setGarbageCollector(GarbageCollectorMXBean value);

    /**
     * static field
     */
    XGrandExchangeOffer[] getGrandExchangeOffers();

    /**
     * static field
     */
    void setGrandExchangeOffers(XGrandExchangeOffer[] value);

    /**
     * public static field
     */
    XAbstractGraphicsProvider getGraphicsProvider();

    /**
     * public static field
     */
    void setGraphicsProvider(XAbstractGraphicsProvider value);

    /**
     * static field
     */
    XNodeDeque[][][] getGroundItems();

    /**
     * static field
     */
    void setGroundItems(XNodeDeque[][][] value);

    /**
     * static field
     */
    XGzipDecompressor getGzipDecompressor();

    /**
     * static field
     */
    void setGzipDecompressor(XGzipDecompressor value);

    /**
     * static field
     */
    XIgnored[] getIgnoreList();

    /**
     * static field
     */
    void setIgnoreList(XIgnored[] value);

    /**
     * public static field
     */
    boolean getIsMembersWorld();

    /**
     * public static field
     */
    void setIsMembersWorld(boolean value);

    /**
     * static field
     */
    boolean getIsMenuOpen();

    /**
     * static field
     */
    void setIsMenuOpen(boolean value);

    /**
     * static field
     */
    XNodeHashTable getItemContainers();

    /**
     * static field
     */
    void setItemContainers(XNodeHashTable value);

    /**
     * public static field
     */
    XNodeCache getItemDefinitionCache();

    /**
     * public static field
     */
    void setItemDefinitionCache(XNodeCache value);

    /**
     * public static field
     */
    XKeyHandler getKeyHandler();

    /**
     * public static field
     */
    void setKeyHandler(XKeyHandler value);

    /**
     * static field
     */
    XNodeCache getKitDefinitionCache();

    /**
     * static field
     */
    void setKitDefinitionCache(XNodeCache value);

    /**
     * static field
     */
    int[] getLevels();

    /**
     * static field
     */
    void setLevels(int[] value);

    /**
     * static field
     */
    XPlayer getLocalPlayer();

    /**
     * static field
     */
    void setLocalPlayer(XPlayer value);

    /**
     * static field
     */
    String[] getMenuActions();

    /**
     * static field
     */
    void setMenuActions(String[] value);

    /**
     * static field
     */
    int[] getMenuArguments0();

    /**
     * static field
     */
    void setMenuArguments0(int[] value);

    /**
     * static field
     */
    int[] getMenuArguments1();

    /**
     * static field
     */
    void setMenuArguments1(int[] value);

    /**
     * static field
     */
    int[] getMenuArguments2();

    /**
     * static field
     */
    void setMenuArguments2(int[] value);

    /**
     * static field
     */
    int getMenuHeight();

    /**
     * static field
     */
    void setMenuHeight(int value);

    /**
     * static field
     */
    int[] getMenuOpcodes();

    /**
     * static field
     */
    void setMenuOpcodes(int[] value);

    /**
     * static field
     */
    int getMenuOptionsCount();

    /**
     * static field
     */
    void setMenuOptionsCount(int value);

    /**
     * static field
     */
    int getMenuOptionsCount2();

    /**
     * static field
     */
    void setMenuOptionsCount2(int value);

    /**
     * static field
     */
    String[] getMenuTargetNames();

    /**
     * static field
     */
    void setMenuTargetNames(String[] value);

    /**
     * static field
     */
    int getMenuWidth();

    /**
     * static field
     */
    void setMenuWidth(int value);

    /**
     * static field
     */
    int getMenuX();

    /**
     * static field
     */
    void setMenuX(int value);

    /**
     * static field
     */
    int getMenuY();

    /**
     * static field
     */
    void setMenuY(int value);

    /**
     * static field
     */
    int getMinimapOrientation();

    /**
     * static field
     */
    void setMinimapOrientation(int value);

    /**
     * static field
     */
    int getMinimapOrientationOffset();

    /**
     * static field
     */
    void setMinimapOrientationOffset(int value);

    /**
     * static field
     */
    int getMinimapScale();

    /**
     * static field
     */
    void setMinimapScale(int value);

    /**
     * public static field
     */
    XMouseHandler getMouseHandler();

    /**
     * public static field
     */
    void setMouseHandler(XMouseHandler value);

    /**
     * static field
     */
    XMouseWheel getMouseWheel();

    /**
     * static field
     */
    void setMouseWheel(XMouseWheel value);

    /**
     * public static field
     */
    int getMouseX();

    /**
     * public static field
     */
    void setMouseX(int value);

    /**
     * public static field
     */
    int getMouseY();

    /**
     * public static field
     */
    void setMouseY(int value);

    /**
     * static field
     */
    XNodeCache getNpcDefinitionCache();

    /**
     * static field
     */
    void setNpcDefinitionCache(XNodeCache value);

    /**
     * static field
     */
    XNpc[] getNpcs();

    /**
     * static field
     */
    void setNpcs(XNpc[] value);

    /**
     * public static field
     */
    XNodeCache getObjectDefinitionCache();

    /**
     * public static field
     */
    void setObjectDefinitionCache(XNodeCache value);

    /**
     * static field
     */
    int getPlane();

    /**
     * static field
     */
    void setPlane(int value);

    /**
     * static field
     */
    XPlatformInfo getPlatformInfo();

    /**
     * static field
     */
    void setPlatformInfo(XPlatformInfo value);

    /**
     * static field
     */
    int[] getPlayerRegions();

    /**
     * static field
     */
    void setPlayerRegions(int[] value);

    /**
     * static field
     */
    XPlayer[] getPlayers();

    /**
     * static field
     */
    void setPlayers(XPlayer[] value);

    /**
     * static field
     */
    XNodeDeque getProjectiles();

    /**
     * static field
     */
    void setProjectiles(XNodeDeque value);

    /**
     * static field
     */
    int getRights();

    /**
     * static field
     */
    void setRights(int value);

    /**
     * static field
     */
    int getRunEnergy();

    /**
     * static field
     */
    void setRunEnergy(int value);

    /**
     * static field
     */
    XScene getScene();

    /**
     * static field
     */
    void setScene(XScene value);

    /**
     * public static field
     */
    XNodeCache getSequenceDefinitionCache();

    /**
     * public static field
     */
    void setSequenceDefinitionCache(XNodeCache value);

    /**
     * public static field
     */
    int[] getSineTable();

    /**
     * public static field
     */
    void setSineTable(int[] value);

    /**
     * public static field
     */
    XNodeCache getSpotAnimationDefinitionCache();

    /**
     * public static field
     */
    void setSpotAnimationDefinitionCache(XNodeCache value);

    /**
     * public static field
     */
    XNodeCache getSpriteCache();

    /**
     * public static field
     */
    void setSpriteCache(XNodeCache value);

    /**
     * static field
     */
    int[][][] getTileHeights();

    /**
     * static field
     */
    void setTileHeights(int[][][] value);

    /**
     * static field
     */
    byte[][][] getTileRenderFlags();

    /**
     * static field
     */
    void setTileRenderFlags(byte[][][] value);

    /**
     * public static field
     */
    XNodeCache getVarbitDefinitionCache();

    /**
     * public static field
     */
    void setVarbitDefinitionCache(XNodeCache value);

    /**
     * static field
     */
    int getViewportHeight();

    /**
     * static field
     */
    void setViewportHeight(int value);

    /**
     * static field
     */
    int getViewportScale();

    /**
     * static field
     */
    void setViewportScale(int value);

    /**
     * static field
     */
    int getViewportTempX();

    /**
     * static field
     */
    void setViewportTempX(int value);

    /**
     * static field
     */
    int getViewportTempY();

    /**
     * static field
     */
    void setViewportTempY(int value);

    /**
     * static field
     */
    int getViewportWidth();

    /**
     * static field
     */
    void setViewportWidth(int value);

    /**
     * static field
     */
    int getViewportX();

    /**
     * static field
     */
    void setViewportX(int value);

    /**
     * static field
     */
    int getViewportY();

    /**
     * static field
     */
    void setViewportY(int value);

    /**
     * static field
     */
    boolean[][] getVisibleTiles();

    /**
     * static field
     */
    void setVisibleTiles(boolean[][] value);

    /**
     * static field
     */
    int getWeight();

    /**
     * static field
     */
    void setWeight(int value);

    /**
     * static field
     */
    int[] getWidgetHeights();

    /**
     * static field
     */
    void setWidgetHeights(int[] value);

    /**
     * static field
     */
    XNodeHashTable getWidgetNodes();

    /**
     * static field
     */
    void setWidgetNodes(XNodeHashTable value);

    /**
     * static field
     */
    int[] getWidgetWidths();

    /**
     * static field
     */
    void setWidgetWidths(int[] value);

    /**
     * static field
     */
    int[] getWidgetXs();

    /**
     * static field
     */
    void setWidgetXs(int[] value);

    /**
     * static field
     */
    int[] getWidgetYs();

    /**
     * static field
     */
    void setWidgetYs(int[] value);

    /**
     * public static field
     */
    XWidget[][] getWidgets();

    /**
     * public static field
     */
    void setWidgets(XWidget[][] value);

    /**
     * static field
     */
    String getWorldHost();

    /**
     * static field
     */
    void setWorldHost(String value);

    /**
     * static field
     */
    int getWorldId();

    /**
     * static field
     */
    void setWorldId(int value);

    /**
     * static field
     */
    int getWorldProperties();

    /**
     * static field
     */
    void setWorldProperties(int value);

    /**
     * static field
     */
    XWorld[] getWorlds();

    /**
     * static field
     */
    void setWorlds(XWorld[] value);

    /**
     * static field
     */
    int getWorldsCount();

    /**
     * static field
     */
    void setWorldsCount(int value);

    /**
     * static field
     */
    String getWorldsUrl();

    /**
     * static field
     */
    void setWorldsUrl(String value);

    /**
     * public static method
     */
    XNpcDefinition getNpcDefinition(int id);

    /**
     * public final method
     */
    void init();

    /**
     * final method
     */
    void openMenu(int x, int y);

    /**
     * static method
     */
    void updateGameState(int gameState);
}
