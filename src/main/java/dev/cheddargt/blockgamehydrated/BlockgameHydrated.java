package dev.cheddargt.blockgamehydrated;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.block.BlockState;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import static net.minecraft.potion.PotionUtil.POTION_KEY;

public class BlockgameHydrated implements ModInitializer {
	public static final String MOD_ID = "blockgame-hydrated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//	String selectedItem = getWaterBottleStack().toString();

	private static final boolean TEST_ENV = true;
	private static final int WATER_SLOT = 8;
	private static final int INV_START = 9;
	private static final int INV_END = 36;
	private static final int HOTBAR_START = 0;
	private static final int HOTBAR_END = 7; // excludes last position (dedicated for water)

	@Override
	public void onInitialize() {
		System.out.println("Blockgame Hydrated initialized");

		ClientPickBlockGatherCallback.EVENT.register((player, result) -> {

			return ItemStack.EMPTY;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_CAPS_LOCK) == GLFW.GLFW_PRESS) {
				ItemStack waterBottleStack = getWaterBottleStack();

				moveItemsFromHotbarToInventory(client, waterBottleStack, true);
				int itemInInventory = findItemInInventory(client, waterBottleStack, 9, 35);
				int emptyHotbarSlot = findDedicatedHotbarSlot(client);
				refillHotbarWaterBottle(client, itemInInventory, emptyHotbarSlot);
			}
		});
	}

	private void refillHotbarWaterBottle(MinecraftClient client, int itemInInventory, int dedicatedHotbarSlot) {
		if (itemInInventory != -1 && dedicatedHotbarSlot != -1) {
			client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, itemInInventory, 8, SlotActionType.SWAP, client.player);
		}
	}

	private ItemStack getWaterBottleStack() {
		Item potionItem = Registries.ITEM.get(new Identifier("minecraft:potion"));
		final String POTION_KEY = "Potion";
		ItemStack waterBottleStack = potionItem.getDefaultStack();
		waterBottleStack.getOrCreateNbt().putString(POTION_KEY, "water");

        return waterBottleStack;
	}

	private ArrayList<Integer> getEmptyInventorySlots(MinecraftClient client) {
		int startSlot = 9;
		int endSlot = 35;
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		Item emptySlot = Registries.ITEM.get(new Identifier("minecraft:air"));

		for (int i = startSlot; i <= endSlot; i++) {
            assert client.player != null; // pq client.player pode retornar nulo?
            ItemStack stack = client.player.getInventory().getStack(i);
			if (stack.getItem() == emptySlot) {
				emptySlots.add(i);
			}
		}

		return emptySlots;
	}

	private void moveItemsFromHotbarToInventory(MinecraftClient client, ItemStack itemStack, boolean descending) {
		ArrayList<Integer> emptyInventorySlots = getEmptyInventorySlots(client);
		if (descending) emptyInventorySlots.sort(Comparator.reverseOrder());

		System.out.println("emptyInventorySlots: " + emptyInventorySlots);
		Iterator<Integer> iterator = emptyInventorySlots.iterator();
		while (iterator.hasNext()) {
			int emptySlot = iterator.next();
			int itemInHotbar = findItemInInventory(client, itemStack, 0, 8); // EXCLUINDO A ULTIMA POSICAO
			if (itemInHotbar == 7) System.out.println("emptySlot: " + emptySlot + " // itemInHotbar: " + itemInHotbar);
			if (itemInHotbar != -1) {
				client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, emptySlot, itemInHotbar, SlotActionType.SWAP, client.player);
				iterator.remove();
			}
		}
	}

	private int findItemInInventory(MinecraftClient client, ItemStack itemStack, int startSlot, int endSlot) {
		for (int i = startSlot; i <= endSlot; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (stack.getItem() == itemStack.getItem()) {
				if (stack.hasNbt() && stack.getNbt().toString().contains("BOTTLE_WATER_CLEAN")) {
//				if ((stack.hasNbt() && itemStack.hasNbt()) && (Objects.equals(stack.getNbt().toString(), itemStack.getNbt().toString()))) {
					return i;
				}
			}
		}
		return -1;
	}

	private int findDedicatedHotbarSlot(MinecraftClient client) {
		if (client.player.getInventory().getStack(8).isEmpty()) {
			return 8;
		}
		return -1;
	}
}