package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.String;
import java.lang.management.GarbageCollectorMXBean;
import org.jetbrains.annotations.NotNull;

public interface XClient extends Accessor, XGameShell {
    @Field
    int getBaseX();

    @Field
    void setBaseX(int value);

    @Field
    int getBaseY();

    @Field
    void setBaseY(int value);

    @Field
    int getCameraPitch();

    @Field
    void setCameraPitch(int value);

    @Field
    int getCameraX();

    @Field
    void setCameraX(int value);

    @Field
    int getCameraY();

    @Field
    void setCameraY(int value);

    @Field
    int getCameraYaw();

    @Field
    void setCameraYaw(int value);

    @Field
    int getCameraZ();

    @Field
    void setCameraZ(int value);

    @Field
    XClanMate[] getClanChat();

    @Field
    void setClanChat(XClanMate[] value);

    @Field
    String getClanChatOwner();

    @Field
    void setClanChatOwner(String value);

    @Field
    XNodeDeque2 getClassInfos();

    @Field
    void setClassInfos(XNodeDeque2 value);

    @Field
    XClientPreferences getClientPreferences();

    @Field
    void setClientPreferences(XClientPreferences value);

    @Field
    XCollisionMap[] getCollisionMaps();

    @Field
    void setCollisionMaps(XCollisionMap[] value);

    @Field
    int[] getCosineTable();

    @Field
    void setCosineTable(int[] value);

    @Field
    int[] getCurrentLevels();

    @Field
    void setCurrentLevels(int[] value);

    @Field
    int getCursorColor();

    @Field
    void setCursorColor(int value);

    @Field
    int getCycle();

    @Field
    void setCycle(int value);

    @Field
    boolean getDisplayFps();

    @Field
    void setDisplayFps(boolean value);

    @Field
    int[] getExperience();

    @Field
    void setExperience(int[] value);

    @Field
    int getFps();

    @Field
    void setFps(int value);

    @Field
    XFriend[] getFriendsList();

    @Field
    void setFriendsList(XFriend[] value);

    @Field
    int getGameDrawingMode();

    @Field
    void setGameDrawingMode(int value);

    @Field
    XGameShell getGameShell();

    @Field
    void setGameShell(XGameShell value);

    @Field
    int getGameState();

    @Field
    void setGameState(int value);

    @Field
    GarbageCollectorMXBean getGarbageCollector();

    @Field
    void setGarbageCollector(GarbageCollectorMXBean value);

    @Field
    XGrandExchangeOffer[] getGrandExchangeOffers();

    @Field
    void setGrandExchangeOffers(XGrandExchangeOffer[] value);

    @Field
    XAbstractGraphicsProvider getGraphicsProvider();

    @Field
    void setGraphicsProvider(XAbstractGraphicsProvider value);

    @Field
    XNodeDeque[][][] getGroundItems();

    @Field
    void setGroundItems(XNodeDeque[][][] value);

    @Field
    XGzipDecompressor getGzipDecompressor();

    @Field
    void setGzipDecompressor(XGzipDecompressor value);

    @Field
    XIgnored[] getIgnoreList();

    @Field
    void setIgnoreList(XIgnored[] value);

    @Field
    boolean getIsMembersWorld();

    @Field
    void setIsMembersWorld(boolean value);

    @Field
    boolean getIsMenuOpen();

    @Field
    void setIsMenuOpen(boolean value);

    @Field
    XNodeHashTable getItemContainers();

    @Field
    void setItemContainers(XNodeHashTable value);

    @Field
    XNodeCache getItemDefinitionCache();

    @Field
    void setItemDefinitionCache(XNodeCache value);

    @Field
    XKeyHandler getKeyHandler();

    @Field
    void setKeyHandler(XKeyHandler value);

    @Field
    XNodeCache getKitDefinitionCache();

    @Field
    void setKitDefinitionCache(XNodeCache value);

    @Field
    int[] getLevels();

    @Field
    void setLevels(int[] value);

    @Field
    XPlayer getLocalPlayer();

    @Field
    void setLocalPlayer(XPlayer value);

    @Field
    String[] getMenuActions();

    @Field
    void setMenuActions(String[] value);

    @Field
    int[] getMenuArguments0();

    @Field
    void setMenuArguments0(int[] value);

    @Field
    int[] getMenuArguments1();

    @Field
    void setMenuArguments1(int[] value);

    @Field
    int[] getMenuArguments2();

    @Field
    void setMenuArguments2(int[] value);

    @Field
    int getMenuHeight();

    @Field
    void setMenuHeight(int value);

    @Field
    int[] getMenuOpcodes();

    @Field
    void setMenuOpcodes(int[] value);

    @Field
    int getMenuOptionsCount();

    @Field
    void setMenuOptionsCount(int value);

    @Field
    int getMenuOptionsCount2();

    @Field
    void setMenuOptionsCount2(int value);

    @Field
    String[] getMenuTargetNames();

    @Field
    void setMenuTargetNames(String[] value);

    @Field
    int getMenuWidth();

    @Field
    void setMenuWidth(int value);

    @Field
    int getMenuX();

    @Field
    void setMenuX(int value);

    @Field
    int getMenuY();

    @Field
    void setMenuY(int value);

    @Field
    int getMinimapOrientation();

    @Field
    void setMinimapOrientation(int value);

    @Field
    int getMinimapOrientationOffset();

    @Field
    void setMinimapOrientationOffset(int value);

    @Field
    int getMinimapScale();

    @Field
    void setMinimapScale(int value);

    @Field
    XMouseHandler getMouseHandler();

    @Field
    void setMouseHandler(XMouseHandler value);

    @Field
    XMouseWheel getMouseWheel();

    @Field
    void setMouseWheel(XMouseWheel value);

    @Field
    int getMouseX();

    @Field
    void setMouseX(int value);

    @Field
    int getMouseY();

    @Field
    void setMouseY(int value);

    @Field
    XNodeCache getNpcDefinitionCache();

    @Field
    void setNpcDefinitionCache(XNodeCache value);

    @Field
    XNpc[] getNpcs();

    @Field
    void setNpcs(XNpc[] value);

    @Field
    XNodeCache getObjectDefinitionCache();

    @Field
    void setObjectDefinitionCache(XNodeCache value);

    @Field
    int getPlane();

    @Field
    void setPlane(int value);

    @Field
    XPlatformInfo getPlatformInfo();

    @Field
    void setPlatformInfo(XPlatformInfo value);

    @Field
    int[] getPlayerRegions();

    @Field
    void setPlayerRegions(int[] value);

    @Field
    XPlayer[] getPlayers();

    @Field
    void setPlayers(XPlayer[] value);

    @Field
    XNodeDeque getProjectiles();

    @Field
    void setProjectiles(XNodeDeque value);

    @Field
    int getRights();

    @Field
    void setRights(int value);

    @Field
    int getRunEnergy();

    @Field
    void setRunEnergy(int value);

    @Field
    XScene getScene();

    @Field
    void setScene(XScene value);

    @Field
    XNodeCache getSequenceDefinitionCache();

    @Field
    void setSequenceDefinitionCache(XNodeCache value);

    @Field
    int[] getSineTable();

    @Field
    void setSineTable(int[] value);

    @Field
    XNodeCache getSpotAnimationDefinitionCache();

    @Field
    void setSpotAnimationDefinitionCache(XNodeCache value);

    @Field
    XNodeCache getSpriteCache();

    @Field
    void setSpriteCache(XNodeCache value);

    @Field
    int[][][] getTileHeights();

    @Field
    void setTileHeights(int[][][] value);

    @Field
    byte[][][] getTileRenderFlags();

    @Field
    void setTileRenderFlags(byte[][][] value);

    @Field
    XNodeCache getVarbitDefinitionCache();

    @Field
    void setVarbitDefinitionCache(XNodeCache value);

    @Field
    int getViewportHeight();

    @Field
    void setViewportHeight(int value);

    @Field
    int getViewportScale();

    @Field
    void setViewportScale(int value);

    @Field
    int getViewportTempX();

    @Field
    void setViewportTempX(int value);

    @Field
    int getViewportTempY();

    @Field
    void setViewportTempY(int value);

    @Field
    int getViewportWidth();

    @Field
    void setViewportWidth(int value);

    @Field
    int getViewportX();

    @Field
    void setViewportX(int value);

    @Field
    int getViewportY();

    @Field
    void setViewportY(int value);

    @Field
    boolean[][] getVisibleTiles();

    @Field
    void setVisibleTiles(boolean[][] value);

    @Field
    int getWeight();

    @Field
    void setWeight(int value);

    @Field
    int[] getWidgetHeights();

    @Field
    void setWidgetHeights(int[] value);

    @Field
    XNodeHashTable getWidgetNodes();

    @Field
    void setWidgetNodes(XNodeHashTable value);

    @Field
    int[] getWidgetWidths();

    @Field
    void setWidgetWidths(int[] value);

    @Field
    int[] getWidgetXs();

    @Field
    void setWidgetXs(int[] value);

    @Field
    int[] getWidgetYs();

    @Field
    void setWidgetYs(int[] value);

    @Field
    XWidget[][] getWidgets();

    @Field
    void setWidgets(XWidget[][] value);

    @Field
    String getWorldHost();

    @Field
    void setWorldHost(String value);

    @Field
    int getWorldId();

    @Field
    void setWorldId(int value);

    @Field
    int getWorldProperties();

    @Field
    void setWorldProperties(int value);

    @Field
    XWorld[] getWorlds();

    @Field
    void setWorlds(XWorld[] value);

    @Field
    int getWorldsCount();

    @Field
    void setWorldsCount(int value);

    @Field
    String getWorldsUrl();

    @Field
    void setWorldsUrl(String value);

    @Method
    XNpcDefinition getNpcDefinition(int id);

    @Method
    void init();

    @Method
    void openMenu(int x, int y);

    @Method
    void updateGameState(int gameState);

    final class getNpcDefinition {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private getNpcDefinition() {
        }
    }

    final class init {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private init() {
        }
    }

    final class openMenu {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private openMenu() {
        }
    }

    final class updateGameState {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private updateGameState() {
        }
    }
}
