package dev.cheddargt.blockgamehydrated;

import dev.cheddargt.blockgamehydrated.block.ModBlocks;
import dev.cheddargt.blockgamehydrated.item.ModItemGroups;
import dev.cheddargt.blockgamehydrated.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockgameHydrated implements ModInitializer {
	public static final String MOD_ID = "blockgame-hydrated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}