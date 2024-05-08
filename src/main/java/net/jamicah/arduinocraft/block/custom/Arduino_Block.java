package net.jamicah.arduinocraft.block.custom;

import net.jamicah.arduinocraft.Arduinocraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class Arduino_Block extends Block {
    public Arduino_Block(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            // whatever happens if the block is powered
            Arduinocraft.LOGGER.info("Redstone ON");
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

}
