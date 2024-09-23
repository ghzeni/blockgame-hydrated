package dev.cheddargt.blockgamehydrated;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockgameHydrated implements ModInitializer {
	public static final String MOD_ID = "blockgame-hydrated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final int WATER_SLOT = 8;

	public static String selectedItem = "minecraft:potion"; //default selected item

	@Override
	public void onInitialize() {
		System.out.println("Blockgame Hydrated initialized");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.currentScreen instanceof InventoryScreen) {
				if (GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS) {
					Item item = Registries.ITEM.get(new Identifier(selectedItem));
					int itemInInventory = findItemInInventory(client, item, 9, 36);
					if (itemInInventory != -1) {
						int emptyHotbarSlot = findDedicatedHotbarSlot(client);
						BlockgameHydrated.LOGGER.info("empty HotbarSlot: " + emptyHotbarSlot);
						if (emptyHotbarSlot != -1) {
							client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, itemInInventory, emptyHotbarSlot - 36, SlotActionType.SWAP, client.player);
						}
					}
				}
			}
		});
	}

	private int findItemInInventory(MinecraftClient client, Item item, int startSlot, int endSlot) {
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (stack.getItem() == item) {
				return i;
			}
		}
		return -1;
	}

	private int findDedicatedHotbarSlot(MinecraftClient client) {
		int i = WATER_SLOT;
		if (client.player.getInventory().getStack(i).isEmpty()) {
			return i + 36;
		}
		return -1;
	}
}