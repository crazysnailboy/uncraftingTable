package org.jglrxavpok.mods.decraft.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jglrxavpok.mods.decraft.ModUncrafting;
import org.jglrxavpok.mods.decraft.client.config.ModGuiConfigEntries;
import org.jglrxavpok.mods.decraft.common.network.message.ConfigSyncMessage;

import cpw.mods.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


public class ModConfiguration 
{
	
	private static Configuration config = null;

	public static final String CATEGORY_UPDATES = "updates";
	
	public static int uncraftMethod;
	public static int maxUsedLevel;
	public static int standardLevel;
	public static String[] excludedItems;
	
	public static boolean checkForUpdates;
	public static boolean promptForLatest;
	public static boolean promptForRecommended;
	

	
	public static void preInit()
	{
		File configFile = new File(Loader.instance().getConfigDir(), ModUncrafting.MODID + ".cfg");
		
		config = new Configuration(configFile);
		config.load();
		
		syncFromFile();
		
		FMLCommonHandler.instance().bus().register(new ConfigEventHandler());
	}
	
	public static void clientPreInit() 
	{
		FMLCommonHandler.instance().bus().register(new ClientConfigEventHandler());
	}
	
	public static Configuration getConfig() 
	{
		return config;
	}
	
	
	public static void syncFromFile() 
	{
		syncConfig(true, true);
	}

	public static void syncFromGUI() 
	{
		syncConfig(false, true);
	}

	public static void syncFromFields() 
	{
		syncConfig(false, false);
	}
	
	
	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) 
	{
		
		if (loadConfigFromFile) config.load(); 
		
		Property propStandardLevel = config.get(Configuration.CATEGORY_GENERAL, "standardLevel", 5, "Minimum required level to uncraft an item", 0, 50);
		propStandardLevel.setLanguageKey("uncrafting.options.standardLevel");
		propStandardLevel.setRequiresMcRestart(false);
		
		Property propMaxLevel = config.get(Configuration.CATEGORY_GENERAL, "maxUsedLevel", 30, "Maximum required level to uncraft an item", 0, 50);
		propMaxLevel.setLanguageKey("uncrafting.options.maxUsedLevel");
		propMaxLevel.setRequiresMcRestart(false);

		Property propUncraftMethod = config.get(Configuration.CATEGORY_GENERAL, "uncraftMethod", 0, "ID of the used uncrafting equation.");
		propUncraftMethod.setLanguageKey("uncrafting.options.method");
		propUncraftMethod.setValidValues(new String[] { "jglrxavpok", "Xell75 & zenen" });
		propUncraftMethod.setRequiresMcRestart(false);

		Property propExcludedItems = config.get(Configuration.CATEGORY_GENERAL, "excludedItems", new String[] { }, "List of items which cannot be uncrafted");
		propExcludedItems.setLanguageKey("uncrafting.options.excludedItems");
		propExcludedItems.setRequiresMcRestart(false);
		
		
		Property propCheckForUpdates = config.get(ModConfiguration.CATEGORY_UPDATES, "checkForUpdates", true, "Should the mod check for updates on startup");
		propCheckForUpdates.setLanguageKey("uncrafting.options.updates.checkForUpdates");
		propCheckForUpdates.setRequiresMcRestart(true);
		
		Property propPromptForLatest = config.get(ModConfiguration.CATEGORY_UPDATES, "promptForLatest", false, "Alert the user when there is a new version");
		propPromptForLatest.setLanguageKey("uncrafting.options.updates.promptForLatest");
		propPromptForLatest.setRequiresMcRestart(true);
		
		Property propPromptForRecommended = config.get(ModConfiguration.CATEGORY_UPDATES, "promptForRecommended", true, "Alert the user when there is a new recommended version");
		propPromptForRecommended.setLanguageKey("uncrafting.options.updates.promptForRecommended");
		propPromptForRecommended.setRequiresMcRestart(true);

		
		try
		{
			propStandardLevel.setConfigEntryClass(NumberSliderEntry.class);
			propMaxLevel.setConfigEntryClass(NumberSliderEntry.class);
			propUncraftMethod.setConfigEntryClass(ModGuiConfigEntries.UncraftingMethodCycleEntry.class);
			propExcludedItems.setConfigEntryClass(ModGuiConfigEntries.ExcludedItemsArrayEntry.class);
			
			propCheckForUpdates.setConfigEntryClass(ModGuiConfigEntries.BooleanEntry.class);
			propPromptForLatest.setConfigEntryClass(ModGuiConfigEntries.BooleanEntry.class);
			propPromptForRecommended.setConfigEntryClass(ModGuiConfigEntries.BooleanEntry.class);
			
			List<String> propOrderGeneral = new ArrayList<String>();
			propOrderGeneral.add(propStandardLevel.getName());
			propOrderGeneral.add(propMaxLevel.getName());
			propOrderGeneral.add(propUncraftMethod.getName());
			propOrderGeneral.add(propExcludedItems.getName());
			config.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propOrderGeneral);
			
			List<String> propOrderUpdates = new ArrayList<String>();
			propOrderUpdates.add(propCheckForUpdates.getName());
			propOrderUpdates.add(propPromptForLatest.getName());
			propOrderUpdates.add(propPromptForRecommended.getName());
			config.setCategoryPropertyOrder(ModConfiguration.CATEGORY_UPDATES, propOrderUpdates);
			
		}
		catch(NoClassDefFoundError e) { }
		
		
		if (readFieldsFromConfig) 
		{
			standardLevel = propStandardLevel.getInt();
			maxUsedLevel = propMaxLevel.getInt();
			uncraftMethod = propUncraftMethod.getInt();
			excludedItems = propExcludedItems.getStringList();
			
			checkForUpdates = propCheckForUpdates.getBoolean();
			promptForLatest = propPromptForLatest.getBoolean();
			promptForRecommended = propPromptForRecommended.getBoolean();
		}
		
		
		propStandardLevel.set(standardLevel);
		propMaxLevel.set(maxUsedLevel);
		propUncraftMethod.set(uncraftMethod);
		propExcludedItems.set(excludedItems);
		
		propCheckForUpdates.set(checkForUpdates);
		propPromptForLatest.set(promptForLatest);
		propPromptForRecommended.set(promptForRecommended);
		

		if (config.hasChanged()) config.save();
	}

	

	public static class ConfigEventHandler 
	{
		@SubscribeEvent
		public void onPlayerLoggedIn(PlayerLoggedInEvent event)
		{
			if (!event.player.worldObj.isRemote)
			{
				ModUncrafting.instance.getNetwork().sendTo(new ConfigSyncMessage(), (EntityPlayerMP)event.player);
			}
		}
		
	}

	public static class ClientConfigEventHandler 
	{
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) 
		{
			if (ModUncrafting.MODID.equals(event.modID) && !event.isWorldRunning)
			{
				syncFromGUI();
			}
		}
	}
	
}