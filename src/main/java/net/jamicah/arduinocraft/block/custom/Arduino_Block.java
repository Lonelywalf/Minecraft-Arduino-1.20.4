package net.jamicah.arduinocraft.block.custom;

import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.io.IOException;

public class Arduino_Block extends Block {
    public Arduino_Block(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            // whatever happens if the block is powered
            Arduinocraft.LOGGER.info("Redstone ON");
            SerialCom.sendToArduino(1, Arduinocraft.comPort.comPort);
        } else {
            Arduinocraft.LOGGER.info("Redstone OFF");
            SerialCom.sendToArduino(0, Arduinocraft.comPort.comPort);
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

}
