package dev.cheddargt.blockgamehydrated;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.minecraft.potion.PotionUtil.POTION_KEY;

public class BlockgameHydrated implements ModInitializer {
	public static final String MOD_ID = "blockgame-hydrated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//	String selectedItem = getWaterBottleStack().toString();

	private static final int WATER_SLOT = 8;

	@Override
	public void onInitialize() {
		System.out.println("Blockgame Hydrated initialized");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.currentScreen instanceof InventoryScreen) {
				if (GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS) {
					ItemStack waterBottleStack = getWaterBottleStack();

					int itemInInventory = findItemInInventory(client, waterBottleStack, 9, 36);

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

	private ItemStack getWaterBottleStack() {
		Item potionItem = Registries.ITEM.get(new Identifier("minecraft:potion"));
		final String POTION_KEY = "Potion";
		ItemStack waterBottleStack = potionItem.getDefaultStack();
		waterBottleStack.getOrCreateNbt().putString(POTION_KEY, "water");

        return waterBottleStack;
	}

	private int findItemInInventory(MinecraftClient client, ItemStack itemStack, int startSlot, int endSlot) {
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (stack.getItem() == itemStack.getItem()) {
				if ((stack.hasNbt() && itemStack.hasNbt()) && (Objects.equals(stack.getNbt().toString(), itemStack.getNbt().toString()))) {
					return i;
				}
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