package foxie.bettersleeping;

import foxie.bettersleeping.modules.Modules;
import foxie.bettersleeping.proxy.ProxyCommon;
import foxie.lib.FoxieSavedData;
import foxie.lib.IFoxieMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping implements IFoxieMod {
   public static final String MODID   = "bettersleeping";
   public static final String NAME    = "Better Sleeping 2";
   public static final String AUTHOR  = "CallMeFoxie";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "foxie.bettersleeping.proxy.ProxyClient", serverSide = "foxie.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance(MODID)
   public static BetterSleeping INSTANCE;

   FoxieSavedData playerData;

   private foxie.lib.Config libConfig;

   private Modules modules;

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      //Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      // TODO fix oldschool config loading

      libConfig = new foxie.lib.Config(event.getSuggestedConfigurationFile());
      proxy.preinit(event);
      modules = new Modules();
      modules.preinit(event);
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
      modules.init(event);
   }

   @Mod.EventHandler
   public void postinit(FMLPostInitializationEvent event) {
      modules.postinit(event);
   }


   @Mod.EventHandler
   public void onServerStarted(FMLServerStartedEvent event) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return;

      World world = MinecraftServer.getServer().worldServers[0];

      playerData = (FoxieSavedData) world.loadItemData(FoxieSavedData.class, BetterSleeping.MODID);
      if (playerData == null) {
         playerData = new FoxieSavedData();
         world.setItemData(BetterSleeping.MODID, playerData);
      }
   }


   @Override
   public foxie.lib.Config getConfig() {
      return libConfig;
   }

   @Override
   public String getModId() {
      return MODID;
   }

   @SubscribeEvent
   public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
      modules.playerJoined(event);
   }

   @SubscribeEvent
   public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
      modules.playerLeft(event);
   }

   @Mod.EventHandler
   public void onServerStarting(FMLServerStartingEvent event) {
      modules.serverStarting(event);
   }
}
