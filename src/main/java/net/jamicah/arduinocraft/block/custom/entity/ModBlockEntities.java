package net.jamicah.arduinocraft.block.custom.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlockEntities {
    // FIXME: Block has no block entity, it is invisible
    public static final BlockEntityType<Arduino_BlockEntity> ARDUINO_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Arduinocraft.MOD_ID, "arduino_block_entity"),
                    FabricBlockEntityTypeBuilder.create(Arduino_BlockEntity::new, ModBlocks.ARDUINO_BLOCK).build());

    public static void registerModBlockEntities() {
        Arduinocraft.LOGGER.info("Registering Block Entities for " + Arduinocraft.MOD_ID);
    }
}
