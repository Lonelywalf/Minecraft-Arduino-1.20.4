package net.jamicah.arduinocraft.block.custom.entity;

import net.jamicah.arduinocraft.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Arduino_BlockEntity extends BlockEntity {

    public Arduino_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARDUINO_BLOCK_ENTITY, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state, Arduino_BlockEntity blockEntity) {
        world.updateNeighbors(pos, state.getBlock());
    }
}
