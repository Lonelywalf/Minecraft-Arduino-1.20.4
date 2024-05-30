package net.jamicah.arduinocraft.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    //public static final Item ARDUINO_BLOCK = registerItem("arduino_block", new Item(new FabricItemSettings()));
    private static void addItemsToRedstoneTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(ModBlocks.ARDUINO_BLOCK);
    }

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Arduinocraft.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Arduinocraft.LOGGER.info("Registering Mod items for " + Arduinocraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(ModItems::addItemsToRedstoneTabItemGroup);
    }
}
