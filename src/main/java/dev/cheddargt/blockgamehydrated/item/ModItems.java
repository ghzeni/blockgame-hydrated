package dev.cheddargt.blockgamehydrated.item;

import dev.cheddargt.blockgamehydrated.BlockgameHydrated;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RUBY = registerItem("ruby", new Item(new FabricItemSettings()));
    public static final Item UM_REAL = registerItem("um_real", new Item(new FabricItemSettings()));
    public static final Item XIOFANA = registerItem("xiofana", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientsTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(RUBY);
        entries.add(UM_REAL);
        entries.add(XIOFANA);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(BlockgameHydrated.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BlockgameHydrated.LOGGER.info("Registering mod items for " + BlockgameHydrated.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsTabItemGroup);
    }
}
